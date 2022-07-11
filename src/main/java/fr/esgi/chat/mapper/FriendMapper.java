package fr.esgi.chat.mapper;

import fr.esgi.chat.domain.model.FriendModel;
import fr.esgi.chat.dto.FriendProfileResponse;
import fr.esgi.chat.service.ChatService;
import fr.esgi.chat.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FriendMapper {
    private final ModelMapper modelMapper;
    private final ChatService chatService;

    private FriendProfileResponse convertToResponseDto(FriendModel friendModel) {
        return modelMapper.map(friendModel, FriendProfileResponse.class);
    }

    public List<FriendProfileResponse> getFriends(String userEmail,String from){
        var updatedAfter = LocalDateTime.now();
        if(from != null && !from.isEmpty()) DateTimeUtil.getDateFromString(from);
        var friends = chatService.getConversations(userEmail,updatedAfter);
        return friends.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    public FriendProfileResponse newConversation(String userEmail,String friendEmail) {
        var friend = chatService.newConversation(userEmail,friendEmail);
        return convertToResponseDto(friend);
    }

    public FriendProfileResponse blockConversation(String userEmail,Long id) {
        var friend = chatService.blockConversation(userEmail,id);
        return convertToResponseDto(friend);
    }
    public FriendProfileResponse unblockConversation(String userEmail,Long id) {
        var friend = chatService.unblockConversation(userEmail,id);
        return convertToResponseDto(friend);
    }

    public Set<FriendProfileResponse> newGroupConversation(String groupName, Set<String> friendsEmail) {
        var friends = chatService.newGroupConversation(groupName,friendsEmail);
        return friends.stream().map(this::convertToResponseDto).collect(Collectors.toSet());
    }

}
