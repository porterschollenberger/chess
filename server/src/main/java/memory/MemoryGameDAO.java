package memory;

import chess.ChessGame;
import dataaccess.GameDAO;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO {
    private final HashMap<Integer, GameData> gameMap;
    private int gameID;

    public MemoryGameDAO() {
        this.gameMap = new HashMap<>();
        this.gameID = 1;
    }

    @Override
    public int createGame(String gameName) {
        gameMap.put(gameID, new GameData(gameID, null, null, gameName, new ChessGame()));
        gameID++;
        return gameID - 1;
    }

    @Override
    public GameData getGame(int gameID) {
        return gameMap.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return gameMap.values();
    }

    @Override
    public void updateGame(int gameID, String playerColorField, String username) {
        GameData game = gameMap.get(gameID);
        GameData updatedGame = switch (playerColorField) {
            case "whiteUsername" ->
                    new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
            case "blackUsername" ->
                    new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
            default -> throw new RuntimeException("Invalid field name: " + playerColorField);
        };
        gameMap.replace(gameID, updatedGame);
    }

    public void updateGame(int gameID, ChessGame newChessGame) {
        GameData game = gameMap.get(gameID);
        GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), newChessGame);
        gameMap.replace(gameID, updatedGame);
    }

    @Override
    public void clear() {
        gameMap.clear();
    }

    public boolean isEmpty() {
        return gameMap.isEmpty();
    }
}
