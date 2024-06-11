package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private CommandType commandType = CommandType.MAKE_MOVE;
    private Integer gameID;
    private ChessMove chessMove;

    public MakeMove(String authToken, Integer gameID, ChessMove chessMove) {
        super(authToken);
        this.gameID = gameID;
        this.chessMove = chessMove;
    }
}
