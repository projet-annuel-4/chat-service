package fr.esgi.chat.service;

import fr.esgi.chat.data.entity.GroupEntity;
import fr.esgi.chat.data.entity.UserEntity;
import fr.esgi.chat.data.repository.GroupRepository;
import fr.esgi.chat.dto.GroupEvent;
import fr.esgi.chat.mapper.GroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    public GroupEntity createGroup(GroupEvent groupEvent) {
        var group = groupMapper.convertToEntity(groupEvent);
        return groupRepository.save(group);
    }
}
