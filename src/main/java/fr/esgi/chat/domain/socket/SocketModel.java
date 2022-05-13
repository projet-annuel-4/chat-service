package fr.esgi.chat.domain.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SocketModel<T> {
    private SocketType type;
    private T data;
}
