package fr.esgi.chat.data.repository;

import fr.esgi.chat.data.entity.ChatEntity;
import fr.esgi.chat.data.entity.ChatGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatGroupRepository extends JpaRepository<ChatGroupEntity,Long> {
}