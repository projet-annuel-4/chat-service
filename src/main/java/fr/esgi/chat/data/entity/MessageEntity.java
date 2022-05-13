package fr.esgi.chat.data.entity;


import fr.esgi.chat.domain.model.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages", indexes =  {@Index(name = "i_conversation",  columnList="conversationId", unique = false)})
public class MessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "messages_id_seq")
    @SequenceGenerator(name = "messages_id_seq", sequenceName = "messages_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private Long senderId;
    private Long conversationId;
    private String content;
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL)
    private Set<FileEntity> files = new HashSet<>();
    private ContentType contentType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean read;
}
