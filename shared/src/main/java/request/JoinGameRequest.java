package request;

public record JoinGameRequest(String playerColor, int gameID, boolean isLeaving) {
}
