package fr.esgi.chat.service;

import fr.esgi.chat.domain.model.MessageModel;
import fr.esgi.chat.exception.ResourceNotFoundException;
import fr.esgi.chat.data.entity.MessageEntity;
import fr.esgi.chat.data.repository.ChatRepository;
import fr.esgi.chat.data.repository.MessageRepository;
import fr.esgi.chat.domain.mapper.FileDomainMapper;
import fr.esgi.chat.domain.mapper.MessageDomainMapper;
import fr.esgi.chat.domain.model.ContentType;
import fr.esgi.chat.domain.model.FileModel;
import fr.esgi.chat.domain.socket.SocketModel;
import fr.esgi.chat.domain.socket.SocketType;
import fr.esgi.chat.dto.UserResponse;
import fr.esgi.chat.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
public class MessageService {
    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;
    private final MessageDomainMapper messageDomainMapper;
    private final FileDomainMapper fileDomainMapper;

    @Transactional
    public List<MessageModel> getAllMessages(List<Long> ids, LocalDateTime updatedAt) {
        var user = getCurrentUser();
        var msgs = new ArrayList<MessageModel>();
        var chats = chatRepository.findAllById(ids);
        chats.forEach(chat -> {
            if (Objects.equals(chat.getUser1(), user.getId()) || Objects.equals(chat.getUser2(), user.getId())) {
                var conversations = messageRepository.findAllByConversationIdAndUpdatedAtAfter(chat.getId(), updatedAt);
                conversations.stream().map(messageDomainMapper::convertToModel).forEach(msgs::add);
            }
        });
        return msgs;
    }

    @Transactional
    public List<MessageModel> getMessagesByChat(Long id, LocalDateTime updatedAt) {
        var user = getCurrentUser();
        var chat = chatRepository.findById(id);
        if (chat.isPresent() && (Objects.equals(chat.get().getUser1(), user.getId()) || Objects.equals(chat.get().getUser2(), user.getId()))) {
            var conversations = messageRepository.findAllByConversationIdAndUpdatedAtAfter(id, updatedAt);
            return conversations.stream().map(messageDomainMapper::convertToModel).collect(Collectors.toList());
        }
        return emptyList();
    }

    public MessageModel createMessage(Long chatId, String content, List<FileModel> files, ContentType contentType) {
        var user = getCurrentUser();
        var chat = chatRepository.findById(chatId).orElseThrow(() -> new ResourceNotFoundException("Conversation", "id", String.valueOf(chatId)));
        if ((Objects.equals(chat.getUser1(), user.getId()) || Objects.equals(chat.getUser2(), user.getId())) && chat.getBlockedBy() == null) {
            var msg = MessageEntity.builder()
                    .senderId(user.getId())
                    .conversationId(chatId)
                    .contentType(contentType)
                    .files(files.stream().map(fileDomainMapper::convertToEntity).collect(Collectors.toSet()))
                    .content(content)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .read(false)
                    .build();
            messageRepository.saveAndFlush(msg);
            simpMessagingTemplate.convertAndSend("/notifications/" + (Objects.equals(chat.getUser1(), user.getId()) ? chat.getUser2() : chat.getUser1()),
                    new SocketModel(SocketType.USER_MESSAGE_ADDED, messageDomainMapper.convertToModel(msg)));
            return messageDomainMapper.convertToModel(msg);
        } else {
            throw new BadRequestException("Sorry you're blocked by user");
        }
    }

    public List<MessageModel> updateMessages(List<Long> ids, Long chatId) {
        var user = getCurrentUser();
        var updatedMessages = new ArrayList<MessageModel>();
        var chat = chatRepository.findById(chatId).orElseThrow(()->new ResourceNotFoundException("Conversation", "id", String.valueOf(chatId)));
        if (!Objects.equals(chat.getUser1(), user.getId()) && Objects.equals(chat.getUser2(), user.getId())) return emptyList();
        var friendId = Objects.equals(chat.getUser1(), user.getId()) ? chat.getUser2() : chat.getUser1();
        var messages = messageRepository.findAllById(ids).stream().filter(msg->!Objects.equals(msg.getSenderId(),user.getId())).collect(Collectors.toSet());
        messages.stream().map(this::updateMessage).forEach(msg->updatedMessages.add(messageDomainMapper.convertToModel(msg)));
        simpMessagingTemplate.convertAndSend("/notifications/" + friendId,
                new SocketModel(SocketType.USER_MESSAGE_UPDATED, updatedMessages));
        return updatedMessages;
    }
    private MessageEntity updateMessage(MessageEntity message){
        message.setRead(true);
        message.setUpdatedAt(LocalDateTime.now());
        return message;
    }
    private UserResponse getCurrentUser() {
        var user = restTemplate.getForObject("http://localhost:9100/api/v1/users/current-user", UserResponse.class);
        if (user == null) {
            throw new ResourceNotFoundException("Current user not found");
        }
        return user;
    }
}
