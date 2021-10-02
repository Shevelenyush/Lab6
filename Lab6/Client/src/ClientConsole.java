import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class ClientConsole {
    //обрабатываем полученные команды, отправляем их на сервер, а после получаем ответ от сервера
    public static void process(ObjectInputStream in, ObjectOutputStream out, Scanner sc) throws IOException, ClassNotFoundException {
        boolean isExit = false;

        while (!isExit) {
            String[] data = null;
            if(sc.hasNext()){
                data = sc.nextLine().replaceAll("^\\s+", "").split(" ", 2);
            }
            else {
                System.exit(0);
            }

            String cmd = data[0];
            String arg;

            try{
                arg = data[1];
            } catch (ArrayIndexOutOfBoundsException e) {
                arg = null;
            }
            Request request = processRequest(cmd, arg, sc, in, out);


            out.writeObject(request);

            if (request.isExit) {
                isExit = true;
            }



            String ans = (String) in.readObject();
            System.out.println(ans);
        }
    }

    //Часть команд, которые удобнее обрабатывать в программе клиента
    public static Request processRequest(String cmd, String arg, Scanner sc, ObjectInputStream in, ObjectOutputStream out) {
        Request req = new Request();
        req.cmd = cmd;
        req.arg = arg;
        switch (cmd) {
            case ("execute_script"):
                Commands.execute_script(arg, out, in);
            case ("add"):
            case ("update_id"):
                req.band = Commands.createBand(sc);
                break;
            case ("exit"):
                req.isExit = true;
                break;
            default:
                break;
        }
        return req;
    }
}
