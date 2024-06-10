package client;

import java.util.Scanner;

public class Repl {
    private final Client client;

    public Repl(int port) {
        client = new Client(port);
    }

    public void run() {
        System.out.println("♕ 240 Chess Client: Type Help to get started. ♕");
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
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + ">>> ");
    }
}