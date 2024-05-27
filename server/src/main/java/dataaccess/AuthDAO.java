package dataaccess;

import model.AuthData;

public interface AuthDAO {
    String createAuth(String username);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void clear();

    boolean isEmpty();
}
