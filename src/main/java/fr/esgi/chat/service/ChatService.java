package fr.esgi.chat.service;

import fr.esgi.chat.exception.ResourceNotFoundException;
import fr.esgi.chat.data.entity.ChatEntity;
import fr.esgi.chat.data.repository.ChatRepository;
import fr.esgi.chat.domain.model.FriendModel;
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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {
    private static final String NOTIFICATIONS_URL="/notifications/";

    private final RestTemplate restTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;

    @Transactional
    public List<FriendModel> getConversations(LocalDateTime date) {
        var user = getCurrentUser();
        var friends = new HashMap<Long, ChatEntity>();
        var users = new ArrayList<UserResponse>();
        var chats = chatRepository.findAllByUser1OrUser2AndUpdatedAtAfter(user.getId(), user.getId(), date);

        chats.forEach(chat->{
            if (Objects.equals(chat.getUser1(),user.getId())){
                friends.put(chat.getUser2(), chat);
                users.add(getUserById(chat.getUser2()));
            }
            if (Objects.equals(chat.getUser2(),user.getId())){
                friends.put(chat.getUser1(), chat);
                users.add(getUserById(chat.getUser1()));
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
        var user = getUserByEmail(userEmail);
        var friend = getUserByEmail(friendEmail);
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
    public FriendModel blockConversation(long id) {
        var user = getCurrentUser();
        var chat = chatRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Conversation", "id", String.valueOf(id)));
        if ((Objects.equals(chat.getUser1() , user.getId())|| Objects.equals(chat.getUser2() , user.getId())) && Objects.equals(chat.getBlockedBy(),null)){
            chat.setBlockedBy(user.getId());
            return notifyChat(user, chat);

        } else{
            throw new BadRequestException("Sorry you can block this conversation");
        }
    }

    @Transactional
    public FriendModel unblockConversation(Long id) {
        var user = getCurrentUser();
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
        var friend = getUserById(Objects.equals(chat.getUser1() , user.getId()) ? chat.getUser2() : chat.getUser1());
        simpMessagingTemplate.convertAndSend(NOTIFICATIONS_URL+friend.getId(), new SocketModel<>(SocketType.USER_CONVERSATION_UPDATED,
                new FriendModel(chat.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), null, chat.getBlockedBy())));
        return new FriendModel(chat.getId(), friend.getEmail(), friend.getFirstName(), friend.getLastName(), friend.getImgUrl(), chat.getBlockedBy());
    }

    private UserResponse getUserById(Long id){
        var user =  restTemplate.getForObject("http://localhost:9100/api/v1/users/{id}",UserResponse.class,id);
        if(user == null){
            throw new ResourceNotFoundException("User", "id", String.valueOf(id));
        }
        return user;
    }
    private UserResponse getUserByEmail(String email){
        /*var user =  restTemplate.getForObject("http://localhost:8072/auth/api/v1/users/{email}",UserResponse.class,email);
        if(user == null){
            throw new ResourceNotFoundException("User", "email", email);
        }*/
        var user = UserResponse.builder().id(1L).email("admin@gmail.com").firstName("admin").build();
        return user;
    }
    private UserResponse getCurrentUser() {
        /*var user = restTemplate.getForObject("http://localhost:8072/auth/api/v1/users/current-user",UserResponse.class);
        if(user == null){
            throw new ResourceNotFoundException("Current user not found");
        }*/
        var user = UserResponse.builder().id(1L).email("admin@gmail.com").firstName("admin").build();
        return user;
    }
}
