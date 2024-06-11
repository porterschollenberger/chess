package websocket.commands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand {
    private CommandType commandType;
    private Integer gameID;
    private ChessMove chessMove;

    public MakeMove(String authToken, CommandType commandType, Integer gameID, ChessMove chessMove) {
        super(authToken);
        this.commandType = commandType;
        this.gameID = gameID;
        this.chessMove = chessMove;
    }
}
