package fr.esgi.chat.mapper;

import fr.esgi.chat.dto.FriendProfileResponse;
import fr.esgi.chat.service.ChatService;
import fr.esgi.chat.util.DateTimeUtil;
import fr.esgi.chat.domain.model.FriendModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FriendMapper {
    private final ModelMapper modelMapper;
    private final ChatService chatService;

    private FriendProfileResponse convertToResponseDto(FriendModel friendModel) {
        return modelMapper.map(friendModel, FriendProfileResponse.class);
    }

    public List<FriendProfileResponse> getFriends(String from){
        var updatedAfter = LocalDateTime.now();
        if(from != null && !from.isEmpty()) DateTimeUtil.getDateFromString(from);
        var friends = chatService.getConversations(updatedAfter);
        return friends.stream().map(this::convertToResponseDto).collect(Collectors.toList());
    }

    public FriendProfileResponse newConversation(String userEmail,String friendEmail) {
        var friend = chatService.newConversation(userEmail,friendEmail);
        return convertToResponseDto(friend);
    }

    public FriendProfileResponse blockConversation(Long id) {
        var friend = chatService.blockConversation(id);
        return convertToResponseDto(friend);
    }
    public FriendProfileResponse unblockConversation(Long id) {
        var friend = chatService.unblockConversation(id);
        return convertToResponseDto(friend);
    }


}
