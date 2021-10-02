import com.sun.deploy.security.ruleset.RuleSetParser;
import com.sun.xml.internal.ws.client.ClientTransportException;
import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.text.ParseException;
import java.util.*;

// подключаем сервер, сокет, и запускаем серверное приложение.
//Создаем необходимую коллекцию
public class Server extends Object implements Serializable {

    public static void main(String[] args) {
        SocketChannel channel = null;
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(8502));
            server.configureBlocking(false);
            ArrayDeque<String> history = new ArrayDeque<>();
            ArrayList<MusicBand> list = new ArrayList<>();
            System.out.println("The server is working right now");
            ParserJSON.parser(list);
            while(true) {
                ServerConsole.go(channel, server, list, history);
            }

            } catch (IOException ioException) {
            System.out.print("Connection lost!");
        }
    }
}
