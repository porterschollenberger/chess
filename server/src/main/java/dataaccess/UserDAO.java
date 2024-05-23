package dataaccess;

import model.AuthData;
import model.UserData;

public interface UserDAO {
    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;

    void clear();
}
