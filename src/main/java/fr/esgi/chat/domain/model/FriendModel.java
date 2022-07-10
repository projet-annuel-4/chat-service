package fr.esgi.chat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FriendModel {
    private Long id;
    private Long friendId;
    private String email;
    private String firstName;
    private String lastName;
    private String imgUrl;
    private Long blockedBy;
}
