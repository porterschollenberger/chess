package memory;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
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
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData auth = authMap.get(authToken);
        if (auth == null) {
            throw new DataAccessException("authToken " + authToken + "does not exist");
        }
        return auth;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        AuthData removedAuth = authMap.remove(authToken);
        if (removedAuth == null) {
            throw new DataAccessException("authToken " + authToken + "does not exist");
        }
    }

    @Override
    public void clear() {
        authMap.clear();
    }
}
