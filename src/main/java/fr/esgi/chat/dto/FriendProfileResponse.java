package fr.esgi.chat.dto;

import lombok.Data;

@Data
public class FriendProfileResponse {
    private String id;
    private String friendId;
    private String email;
    private String firstName;
    private String lastName;
    private String imgUrl;
    private String blockedBy;
}
