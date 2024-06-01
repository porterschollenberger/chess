package sql;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;

public class SQLGameDAO implements GameDAO {
    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(int gameID, String playerColorField, String username) {

    }

    @Override
    public void updateGame(int gameID, ChessGame newChessGame) {

    }

    @Override
    public void clear() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
