package fr.esgi.chat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatModel {
    private Long id;
    private Long user1;
    private Long user2;
    private Long blockedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
