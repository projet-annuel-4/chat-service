package fr.esgi.chat.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatModel chatModel = (ChatModel) o;
        return id.equals(chatModel.id) && Objects.equals(user1, chatModel.user1) && Objects.equals(user2, chatModel.user2) && Objects.equals(blockedBy, chatModel.blockedBy) && Objects.equals(createdAt, chatModel.createdAt) && Objects.equals(updatedAt, chatModel.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user1, user2, blockedBy, createdAt, updatedAt);
    }
}
