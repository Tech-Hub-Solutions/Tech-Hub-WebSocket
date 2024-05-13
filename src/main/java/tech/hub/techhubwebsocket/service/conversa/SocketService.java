package tech.hub.techhubwebsocket.service.conversa;

import tech.hub.techhubwebsocket.service.conversa.dto.MensagemASerEnviadaDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SocketService {

    private final SimpMessagingTemplate template;

    public void enviarMensagem(String roomCode, MensagemASerEnviadaDto mensagemASerEnviadaDto) {
        String routeWebSocket = String.format("/topic/sala/%s", roomCode);
        template.convertAndSend(routeWebSocket, mensagemASerEnviadaDto);
    }

    public void recarregarConversas(Integer id) {
        String routeWebSocket = String.format("/topic/usuario/%s", id);
        template.convertAndSend(routeWebSocket,"");
    }
}
