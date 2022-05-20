package fr.esgi.chat.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEvent {
    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String city;
    private String address;
    private String phoneNumber;
    private String postIndex;
    private String imgUrl;
}
