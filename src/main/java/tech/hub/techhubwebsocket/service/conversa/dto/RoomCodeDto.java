package tech.hub.techhubwebsocket.service.conversa.dto;

public record RoomCodeDto(
        String roomCode
) {
    public RoomCodeDto(String roomCode) {
        this.roomCode = roomCode;
    }
}
