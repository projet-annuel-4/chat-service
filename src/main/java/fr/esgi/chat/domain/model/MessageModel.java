package fr.esgi.chat.domain.model;

import fr.esgi.chat.data.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageModel {
    private Long id;
    private Long senderId;
    private Long conversationId;
    private String content;
    private Set<FileEntity> files;
    private ContentType contentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean read;
}
