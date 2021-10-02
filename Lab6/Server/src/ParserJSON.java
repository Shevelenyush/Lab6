import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ParserJSON {
    //парсер для файла json
    public static void parser(ArrayList<MusicBand> list) throws FileNotFoundException {
        BufferedReader file = null;
        try {
            file = new BufferedReader(new FileReader(System.getenv("IN")));
        } catch (NullPointerException | FileNotFoundException e) {
            System.out.println("The file does not exist");
            file = new BufferedReader(new FileReader("file.json"));
        }
        try {
            JSONParser parser = new JSONParser();
            if (file.readLine() == null) {
                System.out.println("No errors, and file is empty");
            }
            file = new BufferedReader(new FileReader("file.json"));
            JSONArray arr = (JSONArray) parser.parse(file);
            for (Object o : arr) {
                JSONObject obj = (JSONObject) o;

                Long id = (Long) obj.get("id");
                String name = (String) obj.get("name");
                Integer x_int = ((Long) obj.get("x")).intValue();
                Double y_doub = (Double) obj.get("y");
                Coordinates coordinates = new Coordinates(x_int, y_doub);
                String creationDate_str = (String) obj.get("creationDate");
                DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date creationDate = format.parse(creationDate_str);


                long numberOfParticipants = (long) obj.get("numberOfParticipants");
                long singlesCount = (long) obj.get("singlesCount");
                Integer albumsCount = ((Long) obj.get("albumsCount")).intValue();
                String genre = (String) obj.get("genre");
                String name_str = (String) obj.get("studio_name");
                String address_str = (String) obj.get("address");
                Studio studio = new Studio(name_str, address_str);

                MusicBand musBand = new MusicBand(id, name, coordinates, creationDate, numberOfParticipants, singlesCount, albumsCount, genre, studio);
                list.add(musBand);
            }
        } catch (IOException | ParseException | java.text.ParseException e) {
            System.out.println("Something went wrong...");
        }

    }
}
