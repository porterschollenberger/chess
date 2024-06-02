package service;

import dataaccess.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private final AuthDAO authDAO;
    private final UserDAO userDAO;

    public UserService(AuthDAO authDAO, UserDAO userDAO) {
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public AuthData register(UserData user) throws DataAccessException {
        if (user.username() == null || user.password() == null || user.email() == null) {
            throw new BadRequestException("request did not contain correct information.");
        }
        if (userDAO.getUser(user.username()) != null) {
            throw new AlreadyTakenException("username " + user.username() + " already taken.");
        }
        userDAO.createUser(user);
        String authToken = authDAO.createAuth(user.username());
        return new AuthData(authToken, user.username());
    }

    public AuthData login(String username, String password) throws DataAccessException {
        UserData user = userDAO.getUser(username);
        if (user == null) {
            throw new UnauthorizedException("user does not exist.");
        }
        if (!BCrypt.checkpw(password, user.password())) {
            throw new UnauthorizedException("incorrect password.");
        }
        String authToken = authDAO.createAuth(username);
        return new AuthData(authToken, username);
    }

    public void logout(String authToken) throws DataAccessException {
        AuthData auth = authDAO.getAuth(authToken);
        if (auth == null) {
            throw new UnauthorizedException("authToken " + authToken + " does not exist.");
        }
        authDAO.deleteAuth(authToken);
    }
}
