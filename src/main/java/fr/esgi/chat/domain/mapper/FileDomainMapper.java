package fr.esgi.chat.domain.mapper;


import fr.esgi.chat.data.entity.FileEntity;
import fr.esgi.chat.domain.model.FileModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileDomainMapper {
    private final ModelMapper modelMapper;

    public FileModel convertToModel(FileEntity FileEntity){
        return modelMapper.map(FileEntity,FileModel.class);
    }

    public FileEntity convertToEntity(FileModel FileModel){
        return modelMapper.map(FileModel,FileEntity.class);
    }

}
