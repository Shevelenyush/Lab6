import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;
// Запуск программы клиента, запускаем сокет, проверяем подключение и запускаем процесс ввода команд
public class Client implements Serializable {
    public static void main(String[] args)  {
        try {
            InetAddress address = InetAddress.getLocalHost();
            System.out.println("Address of the host: " + address);

            Socket socket = new Socket(address, 8502);
            System.out.println("Socket: " + socket);
            if(socket.isConnected()) {
                System.out.println("You've connected to the server");
            }

            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            Scanner sc = new Scanner(System.in);
            ClientConsole.process(in, out, sc);

            in.close();
            out.close();
            System.exit(0);
        } catch (ConnectException e) {
            System.out.println("No connection to server");
        } catch (SocketException | ClassNotFoundException e) {
            System.out.println("Connection failed");
        } catch (NoSuchElementException e) {
            System.out.println("Invalid input, try to load program again");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
