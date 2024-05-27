package memory;

import dataaccess.UserDAO;
import model.UserData;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {
    private final HashMap<String, UserData> userMap;

    public MemoryUserDAO() {
        this.userMap = new HashMap<>();
    }

    @Override
    public void createUser(UserData user) {
        userMap.put(user.username(), user);
    }

    @Override
    public UserData getUser(String username) {
        return userMap.get(username);
    }

    @Override
    public void clear() {
        userMap.clear();
    }
}
