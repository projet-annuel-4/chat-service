package fr.esgi.chat.dto;

import fr.esgi.chat.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupEvent {
     private Long id;
     private String name;
     private Set<UserDto> members;
}
