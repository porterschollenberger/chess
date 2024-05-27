package memory;

import dataaccess.AuthDAO;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {
    private final HashMap<String, AuthData> authMap;

    public MemoryAuthDAO() {
        this.authMap = new HashMap<>();
    }

    @Override
    public String createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        authMap.put(authToken, new AuthData(authToken, username));
        return authToken;
    }

    @Override
    public AuthData getAuth(String authToken) {
        return authMap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        authMap.remove(authToken);
    }

    @Override
    public void clear() {
        authMap.clear();
    }

    public boolean isEmpty() {
        return authMap.isEmpty();
    }
}
