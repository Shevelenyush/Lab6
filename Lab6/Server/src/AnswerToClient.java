import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;

public class AnswerToClient {
    public AnswerToClient() {

    }

    interface GoCom {
        void help();
    }

    // Здесь мы видим какие команды естьи  что они будут делать/обрабатывать
    public static void SendAnswer(Request request, Date cr_date, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) {
        try {
            switch (request.cmd) {
                case ("help"):
                    GoCom com = () -> {
                        try {
                            out.writeObject(Commands.help(request, history));
                            Commands.save(list, history, out);
                            System.out.println("Server sent the \"help\" command to the client");

                        } catch (IOException e) {
                            System.out.println("Input or output error!");
                        }
                        };
                    com.help();
                    break;
                case ("info"):
                    out.writeObject(Commands.info(request, cr_date, list, history));
                    Commands.save(list, history, out);
                    System.out.println("Server sent the \"info\" command to the client");
                    break;
                case ("show"):
                    if(Commands.show(request, list, history) == null) {
                        out.writeObject("The list of bands is empty");
                        break;
                    }
                    out.writeObject(Commands.show(request, list, history));
                    System.out.println("The client has watched the list of the bands");
                    Commands.updateHistory(history, request.cmd);
                    break;
                case ("add"):
                    Commands.add(request, list, history);
                    out.writeObject("The band was added");
                    System.out.println("The client added a new band to the list");
                    Commands.save(list, history, out);
                    break;
                case ("update_id"):
                    System.out.println("The client wants to update the band info");
                    Commands.update_id(request, list, history, out);
                    System.out.println("The client updated the band info");
                    Commands.save(list, history, out);
                    break;
                case ("remove_by_id"):
                    Commands.remove_by_id(request, list, history, out);
                    System.out.println("The client removed the band from the list");
                    Commands.save(list, history, out);
                    break;
                case ("clear"):
                    Commands.clear(request, list, history, out);
                    System.out.println("The client cleared all the list");
                    Commands.save(list, history, out);
                    break;
                case ("execute_script"):
                    Commands.updateHistory(history, request.cmd);
                    break;
                case ("exit"):
                    System.out.println("Client went out");
                    Commands.save(list, history, out);
                    out.writeObject("Goodbye!");
                    Commands.save(list, history, out);
                    break;
                case ("remove_last"):
                    Commands.remove_last(request, list, history, out);
                    System.out.println("The client removed the last band from the list");
                    Commands.save(list, history, out);
                    break;
                case ("remove_greater"):
                    Commands.remove_greater(request, list, history, out);
                    System.out.println("The client removed some of the bands");
                    Commands.save(list, history, out);
                    break;
                case ("history"):
                    Commands.history(request, history, out);
                    System.out.println("The client wants to know the history of the requests");
                    break;
                case ("remove_any_by_genre"):
                    Commands.remove_any_by_genre(request, list, history, out);
                    System.out.println("The client removed th band by genre");
                    Commands.save(list, history, out);
                    break;
                case ("count_by_studio"):
                    Commands.count_by_studio(request, list, history, out);
                    break;
                case ("print_field_descending_studio"):
                    Commands.print_field_descending_studio(request, list, history, out);
                    break;
                default:
                    out.writeObject("There's no commands like this. Try \"help\" to see the list of the commands. ");
                    Commands.updateHistory(history, request.cmd);
            }
        } catch (IOException e) {
            System.out.println("Input or output error!");
        }

    }
}
