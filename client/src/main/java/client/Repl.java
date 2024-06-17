package client;

import chess.ChessBoard;
import exception.ResponseException;
import websocket.NotificationHandler;
import websocket.messages.LoadGame;
import websocket.messages.Notification;

import java.util.Scanner;

public class Repl implements NotificationHandler {
    private final Client client;

    public Repl(int port) {
        client = new Client(port, this);
    }

    public void run() {
        System.out.println("♕ 240 Chess Client: Here are some commands to get started. ♕");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.exit(0);
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }

    public void notify(Notification notification) {
        System.out.println(notification.getMessage());
        printPrompt();
    }

    public void load(LoadGame loadGame) throws ResponseException {
        ChessBoard board = loadGame.getChessGame().getBoard();
        client.setChessBoard(board);
        client.redraw(board);
    }
}