package fr.esgi.chat.domain.mapper;

import fr.esgi.chat.data.entity.GroupEntity;
import fr.esgi.chat.domain.model.GroupModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupDomainMapper {
    private final ModelMapper modelMapper;

    public GroupModel convertToModel(GroupEntity groupEntity){
        return modelMapper.map(groupEntity,GroupModel.class);
    }

    public GroupEntity convertToEntity(GroupModel groupModel){
        return modelMapper.map(groupModel,GroupEntity.class);
    }

}
