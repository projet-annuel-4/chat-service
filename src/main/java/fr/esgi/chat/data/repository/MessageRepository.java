package fr.esgi.chat.data.repository;

import fr.esgi.chat.data.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
    List<MessageEntity> findAllByConversationIdAndUpdatedAtAfter(Long id , LocalDateTime date);
    List<MessageEntity> findAllByConversationId(Long id);
}
