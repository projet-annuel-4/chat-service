package fr.esgi.chat.mapper;

import fr.esgi.chat.domain.model.ChatModel;
import fr.esgi.chat.dto.ChatResponse;
import fr.esgi.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatMapper {
    private final ModelMapper modelMapper;
    private final ChatService chatService;

    private ChatResponse convertToResponseDto(ChatModel chatModel) {
        return modelMapper.map(chatModel, ChatResponse.class);
    }

    public SortedSet<ChatResponse> getOrdredUserConvbersations(String userEmail) {
        var conversations = chatService.getOrdredUserConvbersations(userEmail);
        return conversations.stream().map(this::convertToResponseDto).collect(Collectors.toCollection(TreeSet::new));
    }
}
