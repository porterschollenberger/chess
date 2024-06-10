import chess.*;
import client.Repl;
import server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        new Repl(port).run();
    }
}