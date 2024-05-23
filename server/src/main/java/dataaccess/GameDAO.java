package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.Collection;

public interface GameDAO {
    void createGame(String gameName);

    GameData getGame(int gameID) throws DataAccessException;

    Collection<GameData> listGames();

    void updateGame(int gameID, String playerColorField, String username) throws DataAccessException;

    void updateGame(int gameID, ChessGame newChessGame) throws DataAccessException;

    void clear();
}
