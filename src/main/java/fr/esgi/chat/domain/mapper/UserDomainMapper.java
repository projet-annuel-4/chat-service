package fr.esgi.chat.domain.mapper;

import fr.esgi.chat.data.entity.UserEntity;
import fr.esgi.chat.domain.model.UserModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDomainMapper {
    private final ModelMapper modelMapper;

    public UserModel convertToModel(UserEntity userEntity){
        return modelMapper.map(userEntity,UserModel.class);
    }

    public UserEntity convertToEntity(UserModel userModel){
        return modelMapper.map(userModel,UserEntity.class);
    }

}
