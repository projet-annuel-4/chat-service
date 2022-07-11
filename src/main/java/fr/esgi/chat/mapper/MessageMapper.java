package fr.esgi.chat.mapper;

import fr.esgi.chat.domain.model.ContentType;
import fr.esgi.chat.domain.model.MessageModel;
import fr.esgi.chat.dto.MessageResponse;
import fr.esgi.chat.service.FileStorageService;
import fr.esgi.chat.service.MessageService;
import fr.esgi.chat.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final ModelMapper modelMapper;
    private final MessageService messageService;
    private final FileStorageService fileStorageService;

    private MessageResponse convertToResponseDto(MessageModel messageModel) {
        return modelMapper.map(messageModel, MessageResponse.class);
    }

    public List<MessageResponse> getAllMessages(String userEmail,List<Long> ids, String from) {
        var updatedAfter = LocalDateTime.now();
        if(from != null && !from.isEmpty()) DateTimeUtil.getDateFromString(from);
        var messages = messageService.getAllMessages(userEmail,ids,updatedAfter);
        return messages.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    public List<MessageResponse> getMessagesByChat(String userEmail,Long id, String from) {
        var updatedAfter = LocalDateTime.now();
        if(from != null && !from.isEmpty()) DateTimeUtil.getDateFromString(from);
        var messages = messageService.getMessagesByChat(userEmail,id,updatedAfter);
        return messages.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    public MessageResponse createMessage(String userEmail,Long id, String content,List<MultipartFile> files) {
        MessageModel messageModel;
        if(files.isEmpty()){
            messageModel = messageService.createMessage(userEmail,id,content, emptyList(), ContentType.TEXT);
        }else{
            var uploadedFiles = fileStorageService.store(files, id);
            messageModel = messageService.createMessage(userEmail,id,content, uploadedFiles, ContentType.FILE);
        }
        return convertToResponseDto(messageModel);
    }

    public List<MessageResponse> updateMessages(String userEmail,List<Long> ids, Long convId) {
        var messages = messageService.updateMessages(userEmail,ids,convId);
        return messages.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    public MessageResponse lastConversationMessage(Long convId) {
        var lastMessage = messageService.lastConversationMessage(convId);
        return convertToResponseDto(lastMessage);
    }
}
