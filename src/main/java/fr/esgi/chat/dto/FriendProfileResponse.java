package fr.esgi.chat.dto;

import lombok.Data;

@Data
public class FriendProfileResponse {
    private String id;
    private String email;
    private String name;
    private String imgUrl;
    private String blockedBy;
}
