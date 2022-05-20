package fr.esgi.chat.service;

import fr.esgi.chat.data.repository.UserRepository;
import fr.esgi.chat.exception.ResourceNotFoundException;
import fr.esgi.chat.data.entity.ChatEntity;
import fr.esgi.chat.data.repository.ChatRepository;
import fr.esgi.chat.domain.model.FriendModel;
import fr.esgi.chat.domain.socket.SocketModel;
import fr.esgi.chat.domain.socket.SocketType;
import fr.esgi.chat.dto.user.UserResponse;
import fr.esgi.chat.exception.BadRequestException;
import fr.esgi.chat.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final String NOTIFICATIONS_URL="/notifications/";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    private final UserService userService;;

    @Transactional
    public List<FriendModel> getConversations(String userEmail,LocalDateTime date) {
        var user = userService.getUserByEmail(userEmail);
        var friends = new HashMap<Long, ChatEntity>();
        var users = new ArrayList<UserResponse>();
        var chats = chatRepository.findAllByUser1OrUser2AndUpdatedAtAfter(user.getId(), user.getId(), date);

        chats.forEach(chat->{
            if (Objects.equals(chat.getUser1(),user.getId())){
                friends.put(chat.getUser2(), chat);
                users.add(userService.getUserById(chat.getUser2()));
            }
            if (Objects.equals(chat.getUser2(),user.getId())){
                friends.put(chat.getUser1(), chat);
                users.add(userService.getUserById(chat.getUser1()));
            }

        });

        return users.stream().map(u->{
            var profile = friends.get(u.getId());
            return FriendModel
                    .builder()
                    .id(u.getId())
                    .firstName(u.getFirstName())
                    .lastName(u.getLastName())
                    .email(u.getEmail())
                    .imgUrl(u.getImgUrl())
                    .blockedBy(profile.getBlockedBy())
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public FriendModel newConversation(String userEmail,String friendEmail) {
        var user = userService.getUserByEmail(userEmail);
        var friend = userService.getUserByEmail(friendEmail);
        if(chatRepository.findByUser1AndUser2OrUser1AndUser2(user.getId(), friend.getId(), friend.getId(), user.getId()).isPresent()){
            throw new BadRequestException("User already exist by email:"+friendEmail);
        }
        var chat=ChatEntity
                .builder()
                .user1(user.getId())
                .user2(friend.getId())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        chatRepository.saveAndFlush(chat);
        simpMessagingTemplate.convertAndSend(NOTIFICATIONS_URL+friend.getId(), new SocketModel<>(SocketType.USER_CONVERSATION_ADDED,
                new FriendModel(chat.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null, chat.getBlockedBy())));
        return new FriendModel(chat.getId(), friend.getEmail(), friend.getFirstName(), friend.getLastName(), friend.getImgUrl(), chat.getBlockedBy());

    }

    @Transactional
    public FriendModel blockConversation(String userEmail,long id) {
        var user = userService.getUserByEmail(userEmail);
        var chat = chatRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Conversation", "id", String.valueOf(id)));
        if ((Objects.equals(chat.getUser1() , user.getId())|| Objects.equals(chat.getUser2() , user.getId())) && Objects.equals(chat.getBlockedBy(),null)){
            chat.setBlockedBy(user.getId());
            return notifyChat(user, chat);

        } else{
            throw new BadRequestException("Sorry you can block this conversation");
        }
    }

    @Transactional
    public FriendModel unblockConversation(String userEmail,Long id) {
        var user = userService.getUserByEmail(userEmail);
        var chat = chatRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Conversation", "id", String.valueOf(id)));
        if ((Objects.equals(chat.getUser1() , user.getId())|| Objects.equals(chat.getUser2() , user.getId())) && Objects.equals(chat.getBlockedBy(),user.getId())){
            chat.setBlockedBy(null);
            return notifyChat(user, chat);
        }else{
            throw new BadRequestException("Sorry you can unblock this conversation");
        }
    }

    private FriendModel notifyChat(UserResponse user, ChatEntity chat) {
        chat.setUpdatedAt(LocalDateTime.now());
        chatRepository.saveAndFlush(chat);
        var friend = userService.getUserById(Objects.equals(chat.getUser1() , user.getId()) ? chat.getUser2() : chat.getUser1());
        simpMessagingTemplate.convertAndSend(NOTIFICATIONS_URL+friend.getId(), new SocketModel<>(SocketType.USER_CONVERSATION_UPDATED,
                new FriendModel(chat.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null, chat.getBlockedBy())));
        return new FriendModel(chat.getId(), friend.getEmail(), friend.getFirstName(), friend.getLastName(), friend.getImgUrl(), chat.getBlockedBy());
    }



}
