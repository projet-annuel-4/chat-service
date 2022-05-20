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
@Table(name = "chats", indexes = {@Index(name = "i_user1_user2", columnList = "user1,user2")})
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chats_id_seq")
    @SequenceGenerator(name = "chats_id_seq", sequenceName = "chats_id_seq", initialValue = 1, allocationSize = 1)
    private Long id;
    private Long user1;
    private Long user2;
    private Long blockedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
