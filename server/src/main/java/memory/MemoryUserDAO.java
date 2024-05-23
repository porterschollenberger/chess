package memory;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userMap;

    public MemoryUserDAO() {
        this.userMap = new HashMap<>();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (userMap.containsKey(user.username())) {
            throw new DataAccessException("username " + user.username() + " already exists.");
        }
        userMap.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData user = userMap.get(username);
        if (user == null) {
            throw new DataAccessException("username " + username + " does not exist.");
        }
        return user;
    }

    @Override
    public void clear() {
        userMap.clear();
    }
}
