package fr.esgi.chat.data.repository;

import fr.esgi.chat.data.entity.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity,Long> {
    List<ChatEntity> findAllByUser1OrUser2AndUpdatedAtAfter(Long user1 , Long user2, LocalDateTime date);
    List<ChatEntity> findAllByUser1OrUser2(Long user1 , Long user2);
    Optional<ChatEntity> findByUser1AndUser2OrUser1AndUser2(Long user1, Long friend2, Long friend1, Long user2);
}