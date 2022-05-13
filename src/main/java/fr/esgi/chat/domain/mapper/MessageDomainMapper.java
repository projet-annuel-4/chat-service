package fr.esgi.chat.domain.mapper;


import fr.esgi.chat.domain.model.MessageModel;
import fr.esgi.chat.data.entity.MessageEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageDomainMapper {
    private final ModelMapper modelMapper;

    public MessageModel convertToModel(MessageEntity MessageEntity){
        return modelMapper.map(MessageEntity,MessageModel.class);
    }

    public MessageEntity convertToEntity(MessageModel MessageModel){
        return modelMapper.map(MessageModel,MessageEntity.class);
    }

}
