package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    int createGame(String gameName);

    GameData getGame(int gameID);

    Collection<GameData> listGames();

    void updateGame(int gameID, String playerColorField, String username);

    void updateGame(int gameID, ChessGame newChessGame);

    void clear();
}
