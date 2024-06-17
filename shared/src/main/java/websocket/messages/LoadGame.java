package websocket.messages;

import chess.ChessGame;

public class LoadGame extends ServerMessage {
    ChessGame game;

    public LoadGame(ChessGame chessGame) {
        super(ServerMessageType.LOAD_GAME);
        this.game = chessGame;
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game=game;
    }
}
