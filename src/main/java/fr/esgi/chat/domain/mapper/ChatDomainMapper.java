package fr.esgi.chat.domain.mapper;

import fr.esgi.chat.domain.model.ChatModel;
import fr.esgi.chat.data.entity.ChatEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatDomainMapper {
    private final ModelMapper modelMapper;

    public ChatModel convertToModel(ChatEntity chatEntity){
        return modelMapper.map(chatEntity,ChatModel.class);
    }

    public ChatEntity convertToEntity(ChatModel chatModel){
        return modelMapper.map(chatModel,ChatEntity.class);
    }

}
