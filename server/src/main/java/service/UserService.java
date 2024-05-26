package service;

import dataaccess.*;
import model.*;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        userDAO.createUser(user);
        String authToken = authDAO.createAuth(user.username());
        return new AuthData(authToken, user.username());
    }

    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = userDAO.getUser(username);
        if (!user.password().equals(password)) {
            throw new DataAccessException("Incorrect password");
        }
        String authToken = authDAO.createAuth(username);
        return new AuthData(authToken, username);
    }

    public void logout(String authToken) throws DataAccessException {
        authDAO.deleteAuth(authToken);
    }
}
