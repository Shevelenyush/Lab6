import java.io.*;
import java.nio.channels.ServerSocketChannel;
import java.util.*;
// Класс, где мы обрабатываем данны после определенной команды, данной клиентом
public class Commands {

    public Commands() {

    }

    public static String help(Request request, ArrayDeque<String> history) {
        String cmd = request.cmd;
        updateHistory(history, cmd);
        return "help : show the information about the commands\n" +
                "info : show the collection information(type, date of initialization, number of elements etc.)\n" +
                "show : show all the elements of collection\n" +
                "add {element} : add the new element to collection\n" +
                "update_id {element} : update the id of the collection element by its id\n" +
                "remove_by_id id : delete the element by its id\n" +
                "clear : clear the collection\n" +
                "execute_script file_name : read and execute script from th file.\n" +
                "exit : exit the program without saving\n" +
                "remove_last : delete the last element of the collection\n" +
                "remove_greater {element} : delete all the element higher than given one\n" +
                "history : show the last 11 commands\n" +
                "remove_any_by_genre genre : delete 1 collection element which genre field is equal to given from you\n" +
                "count_by_studio {Studio name} : show the number of th collection elements, which studio field is equal to given from you\n" +
                "print_field_descending_studio : show the values of the studio field of the all elements in descending order ";
    }

    public static String info(Request request, Date cr_date, ArrayList<MusicBand> list, ArrayDeque<String> history) {
        String cmd = request.cmd;
        updateHistory(history, cmd);
        return "Collection type: ArrayList\n" +
                "Date of initialization: " + cr_date +
                "\nNumber of the collection elements: " + list.size();
    }

    public static Object show(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history) {
        String showInfo = null;
        if(!list.isEmpty()) {
            showInfo = "";
            for(MusicBand musicBand : list) {
                showInfo = showInfo + "\n" + musicBand.showBandInfo();
            }

        }
        return showInfo;
    }

    public static ArrayList<MusicBand> add(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history) throws IOException {
        MusicBand band = request.band;
        String cmd = request.cmd;
        list.add(band);
        updateHistory(history, cmd);
        return list;
    }

    public static void update_id(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        String arg = request.arg;
        boolean flag = false;
        try{
            Long id = Long.parseLong(arg);
            for(Iterator<MusicBand> it = list.iterator(); it.hasNext(); ) {
                MusicBand musicBand = it.next();
                if (musicBand.getId() == id) {
                    it.remove();
                    flag = true;
                }
            }
            if(!flag) {
                out.writeObject("There's no id like this in the list");
            } else {
                MusicBand bandy = request.band;
                bandy.setId(id);
                list.add(bandy);
                out.writeObject("The band was updated");
            }
            updateHistory(history, cmd);
        } catch(NumberFormatException e) {
            out.writeObject("Wrong format. Enter the ID. Try again. ");
        }
    }

    public static void remove_by_id(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        String arg = request.arg;
        try{
            Long id = Long.parseLong(arg);
            for(Iterator<MusicBand> it = list.iterator(); it.hasNext(); ) {
                MusicBand musicBand = it.next();
                if (musicBand.getId() == id) {
                    it.remove();
                    out.writeObject("The band was deleted");
                    break;
                }
            }
        } catch(NumberFormatException | IOException e) {
            out.writeObject("Wrong format. Try again. ");
        }
        updateHistory(history, cmd);
    }

    public static void clear(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        list.clear();
        out.writeObject("Everything was deleted. ");
        updateHistory(history, cmd);
    }

    public static void save(ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String output_name = "file.json";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(output_name))){
            writer.write("[ ");
            for(MusicBand musicBand : list) {
                String id = String.valueOf(musicBand.getId());
                String name = musicBand.getName();
                String coordinate_x = String.valueOf(musicBand.getCoordinates().getX());
                String coordinate_y = String.valueOf(musicBand.getCoordinates().getY());
                String date = String.valueOf(musicBand.getCreationDate());
                String particNumber = String.valueOf(musicBand.getNumberOfParticipants());
                String singlesCount = String.valueOf(musicBand.getSinglesCount());
                String albumsCount = String.valueOf(musicBand.getAlbumsCount());
                String genre = String.valueOf(musicBand.getGenre());
                String address = musicBand.getStudio().getAddress();
                String st_name = musicBand.getStudio().getName();

                writer.write("{\n" +
                        "    \"id\": "+ id +" ,\n" +
                        "    \"name\": \""+ name +"\",\n" +
                        "    \"x\": "+ coordinate_x +",\n" +
                        "    \"y\": "+ coordinate_y +",\n" +
                        "    \"creationDate\": \"" + date +"\",\n" +
                        "    \"numberOfParticipants\": " + particNumber +",\n" +
                        "    \"singlesCount\": "+ singlesCount +",\n" +
                        "    \"albumsCount\": "+ albumsCount +",\n" +
                        "    \"genre\": \""+ genre +"\",\n" +
                        "    \"name\": \""+ st_name +"\",\n" +
                        "    \"address\": \"" + address +"\"\n" +
                        "    }");
                if(musicBand != list.get(list.size() - 1)) {
                    writer.write(",");
                }
            }
            writer.write("]");
            System.out.println("The list was saved to file");
        } catch (IOException e) {
            System.out.println("Output file error");
        }
    }

    public static void remove_last(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        try {
            list.remove(list.size() - 1);
            out.writeObject("The last band in list was deleted. ");
            updateHistory(history, cmd);
        } catch (ArrayIndexOutOfBoundsException | IOException e) {
            out.writeObject("There's no more band to delete");
        }
    }

    public static void remove_greater(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        String arg = request.arg;
        try {
            Long id = new Long(arg);
            boolean isId = false;
            for(Iterator<MusicBand> it = list.iterator(); it.hasNext(); ) {
                MusicBand musicBand = it.next();
                if(isId) {
                    it.remove();
                }
                if (musicBand.getId() == id) {
                    isId = true;
                }
            }
            out.writeObject("All the bands higher than given ID were deleted ");
        } catch (NumberFormatException | IOException e) {
            out.writeObject("Wrong format. Try again. ");
        }
        updateHistory(history, cmd);
    }

    public static void history(Request request, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        String showInfo = null;
        if(!history.isEmpty()) {
            showInfo = "";
            for(String s : history) {
                showInfo = showInfo + "\n" + s;
            }

        }
        out.writeObject("Here's the last" + history.size() + " commands: " + showInfo);
        updateHistory(history, cmd);
    }

    public static void remove_any_by_genre(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        String arg = request.arg;
        try{
            for(Iterator<MusicBand> it = list.iterator(); it.hasNext(); ) {
                MusicBand musicBand = it.next();
                if(musicBand.getGenre().equals(arg)) {
                    it.remove();
                    out.writeObject("The band was deleted by " + arg + " genre");
                    break;
                }
            }
        } catch (NumberFormatException | IOException e) {
            out.writeObject("Wrong format. Try again.");
        }
        updateHistory(history, cmd);
    }

    public static void count_by_studio(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        String arg = request.arg;
        int counter = 0;
        try{
            for (MusicBand musicBand : list) {
                if (musicBand.getStudio().getName().equals(arg)) {
                    counter++;
                }
            }
            out.writeObject("Number of values is " + counter);
        } catch (NumberFormatException | NullPointerException | IOException e) {
            out.writeObject("Wrong format. Try again.");
        }
        updateHistory(history, cmd);
    }

    public static void print_field_descending_studio(Request request, ArrayList<MusicBand> list, ArrayDeque<String> history, ObjectOutputStream out) throws IOException {
        String cmd = request.cmd;
        HashMap<String, String> st = new HashMap<>();
        try{
            for (MusicBand musicBand : list) {
                st.put(musicBand.getStudio().getName(),musicBand.getStudio().getAddress());
            }
            out.writeObject("Output field values by th descending order: ");
            st.entrySet().stream().sorted(Map.Entry.<String, String>comparingByKey().reversed()).forEach(System.out::println);
        } catch (NumberFormatException | IOException e) {
            out.writeObject("Wrong format. Try again.");
        }
        updateHistory(history, cmd);
    }

    public static void newId(ArrayList<MusicBand> list) {
        LinkedHashSet<Long> newId = new LinkedHashSet<>();
        for(MusicBand band : list) {
            if(!newId.add(band.getId())) {
                Long nextId = new Random().nextLong();
                band.setId(nextId);
            }
        }
    }

    public static void updateHistory(ArrayDeque<String> hist, String cmd) {
        hist.addFirst(cmd);
        if(hist.size() > 11) {
            hist.removeLast();
        }
    }

}
