package fr.esgi.chat.mapper;


import fr.esgi.chat.data.entity.User;
import fr.esgi.chat.dto.user.UserEvent;
import fr.esgi.chat.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse convertToResponseDto(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public User convertToEntity(UserEvent user) {
        return modelMapper.map(user, User.class);
    }
}
