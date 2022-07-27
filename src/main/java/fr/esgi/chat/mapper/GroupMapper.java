package fr.esgi.chat.mapper;


import fr.esgi.chat.data.entity.GroupEntity;
import fr.esgi.chat.dto.GroupEvent;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class GroupMapper {

    private final ModelMapper modelMapper;

    public GroupEntity convertToEntity(GroupEvent Group) {
        return modelMapper.map(Group, GroupEntity.class);
    }
}
