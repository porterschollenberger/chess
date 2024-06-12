package websocket.messages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    ChessGame chessGame;

    public LoadGame(ChessGame chessGame) {
        super(ServerMessageType.LOAD_GAME);
        this.chessGame = chessGame;
    }
}
