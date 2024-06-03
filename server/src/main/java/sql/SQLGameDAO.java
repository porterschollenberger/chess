package sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static sql.DBHelper.*;

public class SQLGameDAO implements GameDAO {
    private int gameID = 1;

    public SQLGameDAO() {
        try {
            configureDatabase(createStatements);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int createGame(String gameName) {
        var statement = "INSERT INTO gameData (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        try {
            ChessGame game = new ChessGame();
            String jsonGame = new Gson().toJson(game);
            String gameIDString = executeUpdate(statement, null, null, gameName, jsonGame);
            assert gameIDString != null;
            return Integer.parseInt(gameIDString);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GameData getGame(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return new GameData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        var result = new ArrayList<GameData>();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        result.add(new GameData(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), new Gson().fromJson(rs.getString(5), ChessGame.class)));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public void updateGame(int gameID, String playerColorField, String username) {
        String statement;
        if (playerColorField.equals("whiteUsername")) {
            statement = "UPDATE gameData SET whiteUsername=? WHERE gameID=?";
        } else if (playerColorField.equals("blackUsername")) {
            statement = "UPDATE gameData SET blackUsername=? WHERE gameID=?";
        } else {
            throw new IllegalArgumentException();
        }
        try {
            executeUpdate(statement, username, gameID);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void updateGame(int gameID, ChessGame newChessGame) {
        var statement = "UPDATE gameData SET game = newChessGame WHERE gameID=?";
        try {
            executeUpdate(statement);
        } catch (DataAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void clear() {
        var statement = "TRUNCATE TABLE gameData";
        try {
            executeUpdate(statement);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isEmpty() {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameData";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    return !rs.next();
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData (
              `gameId` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` varchar(256) DEFAULT NULL,
              `blackUsername` varchar(256) DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT NOT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(whiteUsername),
              INDEX(blackUsername),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };
}
