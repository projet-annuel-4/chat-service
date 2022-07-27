package fr.esgi.chat.mapper;


import fr.esgi.chat.data.entity.UserEntity;
import fr.esgi.chat.dto.user.UserEvent;
import fr.esgi.chat.dto.user.UserResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserResponse convertToResponseDto(UserEntity user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public UserEntity convertToEntity(UserEvent user) {
        return modelMapper.map(user, UserEntity.class);
    }
}
