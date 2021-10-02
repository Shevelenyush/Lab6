import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.channels.Channels;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

//Запуск сервера
public class ServerConsole implements Serializable {
    public static void go(SocketChannel socket, ServerSocketChannel server, ArrayList<MusicBand> list, ArrayDeque<String> history) {
        goServer(socket, server, list, history);
    }

    public static void goServer(SocketChannel channel, ServerSocketChannel server, ArrayList<MusicBand> list, ArrayDeque<String> history) {

        try {
            while (channel == null) {
                channel = server.accept();
            }
            if(channel != null) {
                System.out.println("The client has connected to the server");
                ObjectInputStream in = new ObjectInputStream(Channels.newInputStream(channel));
                ObjectOutputStream out = new ObjectOutputStream(Channels.newOutputStream(channel));
                while (server.isOpen()) {
                    ServerConsole.process(in, out, list, history, server);
                }
                Commands.save(list, history, out);
                in.close();
                out.close();
                server.close();
            }
        } catch (IOException i) {
            go(null, server, list, history);
        }
    }
    // Общаемся с клиентом, поулчаем данные, обрабатываем и отправляем результат обработки.
    public static void process(ObjectInputStream in, ObjectOutputStream out, ArrayList<MusicBand> list, ArrayDeque<String> history, ServerSocketChannel server)  {
        try {
            Request request = (Request) in.readObject();
            Date cr_date = new Date();
            Comparator<MusicBand> comp = Comparator.comparing(MusicBand::getName);
            list.sort(comp);
            AnswerToClient.SendAnswer(request, cr_date, list, history, out);
        } catch (ClassNotFoundException e) {
            go(null, server, list, history);
        } catch (IOException e) {
            System.out.print("The client is not on the server\n");
            go(null, server, list, history);
        }
    }
}
