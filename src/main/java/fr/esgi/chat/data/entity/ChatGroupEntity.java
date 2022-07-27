package fr.esgi.chat.data.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "chat_group", indexes = {@Index(name = "i_chat_group", columnList = "group")})
public class ChatGroupEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_id_seq")
    @SequenceGenerator(name = "chat-group_id_seq", sequenceName = "chat-group_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private Long group;
    private Long createdBy;
    private Long blockedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
