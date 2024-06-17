package client;

public class ClientInfo {
    private String authToken;
    private String username;
    private Integer gameID;
    private String playerColor;

    public ClientInfo(String authToken, String username, Integer gameID, String playerColor) {
        this.authToken = authToken;
        this.username = username;
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken=authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public Integer getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID=gameID;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor=playerColor;
    }
}
