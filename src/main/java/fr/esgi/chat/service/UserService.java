package fr.esgi.chat.service;

import fr.esgi.chat.data.entity.UserEntity;
import fr.esgi.chat.data.repository.UserRepository;
import fr.esgi.chat.dto.user.UserEvent;
import fr.esgi.chat.dto.user.UserResponse;
import fr.esgi.chat.exception.ResourceNotFoundException;
import fr.esgi.chat.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserEntity createUser(UserEvent userEvent) {
        var user = userMapper.convertToEntity(userEvent);
        return userRepository.save(user);
    }

    public UserResponse getUserById(Long id) {
        var user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User", "id", String.valueOf(id));
        }
        return userMapper.convertToResponseDto(user.get());
    }

    public UserResponse getUserByEmail(String email) {
        var user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User", "email", email);
        }
        return userMapper.convertToResponseDto(user.get());
    }
}
