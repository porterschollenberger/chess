package server;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;
import request.*;
import response.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final int port;
    private String authToken;

    public ServerFacade(int port) {
        this.port = port;
    }

    public RegisterResponse register(UserData user) throws ResponseException {
        var path = "/user";
        var response = makeRequest("POST", path, user, RegisterResponse.class);
        this.authToken = response.getAuthToken();
        return response;
    }

    public LoginResponse login(String username, String password) throws ResponseException {
        var path = "/session";
        var response = makeRequest("POST", path, new LoginRequest(username, password), LoginResponse.class);
        this.authToken = response.getAuthToken();
        return response;
    }

    public GenericResponse logout() throws ResponseException {
        var path = "/session";
        var response = makeRequest("DELETE", path, null, GenericResponse.class);
        this.authToken = null;
        return response;
    }

    public CreateGameResponse createGame(String gameName) throws ResponseException {
        var path = "/game";
        return makeRequest("POST", path, new CreateGameRequest(gameName), CreateGameResponse.class);
    }

    public ListGamesResponse listGames() throws ResponseException {
        var path = "/game";
        return makeRequest("GET", path, null, ListGamesResponse.class);
    }

    public GenericResponse joinGame(String playerColor, int gameID) throws ResponseException {
//        var path = "/game";
//        return makeRequest("PUT", path, new JoinGameRequest(playerColor, gameID), GenericResponse.class);
        return new GenericResponse("This has not been implemented");
    }

    public GenericResponse clear() {
        var path = "/db";
        try {
            return makeRequest("DELETE", path, null, GenericResponse.class);
        } catch (ResponseException e) {
            throw new RuntimeException("Clear failed");
        }
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            String serverUrl = "http://localhost:" + port;
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.setRequestProperty("authorization", authToken);
            }

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}