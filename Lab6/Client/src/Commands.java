import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Класс команд клиента
public class Commands {


    public static void execute_script(String arg, ObjectOutputStream out, ObjectInputStream in) {
        try {
            File script = new File(arg);
            Scanner c = new Scanner(script);
            ClientConsole.process(in, out, c);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("No way to file");
        }
    }

    public static MusicBand createBand(Scanner scanner) {
        String name = "";
        Integer x = null;
        Double y = null;
        long numberOfParticipants = 0;
        long singlesCount = 0;
        Integer albumsCount = null;
        String genre = null;
        String st_name = null;
        String address = null;


        System.out.print("Enter the band name: ");
        try {
            if(scanner.hasNext()) {
                name = scanner.nextLine();
            }
            else {
                String input = scanner.nextLine();
                System.out.println(String.format("input = %s", input));
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }
        System.out.print("Enter the x coordinate as Integer number: ");
        try {
            while (x==null)
            {
                if(scanner.hasNext()) {
                    try {
                        x = Integer.parseInt(scanner.nextLine());
                        if(x <= -994) {
                            x=null;
                            System.out.print("The number mustn't be lower than -994. Try again: ");
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Wrong format. Try again. ");
                    }
                }
                else {
                    String input = scanner.nextLine();
                    System.out.println(String.format("input = %s", input));
                }
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        System.out.print("Enter the y coordinate as Real number: ");
        try {
            while (y == null)
            {
                if(scanner.hasNext()) {
                    try {
                        y = Double.parseDouble(scanner.nextLine());
                    } catch (NumberFormatException e) {
                        System.out.print("Wrong format. Try again. ");
                    }
                }
                else {
                    String input = scanner.nextLine();
                    System.out.println(String.format("input = %s", input));
                }
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        System.out.print("Enter the number of the participants: ");
        try {
            while(numberOfParticipants == 0) {
                if(scanner.hasNext()) {
                    try {
                        numberOfParticipants = Long.parseLong(scanner.nextLine());
                        if(numberOfParticipants < 0) {
                            System.out.print("The number mustn't be lower than 0. Try again: ");
                            numberOfParticipants = 0;
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Wrong format. Try again. ");
                    }
                }
                else {
                    String input = scanner.nextLine();
                    System.out.println(String.format("input = %s", input));
                }
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        System.out.print("Enter the number of the band songs: ");
        try {
            while (singlesCount == 0) {
                if(scanner.hasNext()) {
                    try {
                        singlesCount = Long.parseLong(scanner.nextLine());
                        if(singlesCount < 0) {
                            System.out.print("The number mustn't be lower than 0. Try again: ");
                            singlesCount = 0;
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Wrong format. Try again. ");
                    }
                }
                else {
                    String input = scanner.nextLine();
                    System.out.println(String.format("input = %s", input));
                }
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        System.out.print("Enter the number of the albums: ");
        try {
            while (true) {
                if(scanner.hasNext()) {
                    try {
                        albumsCount = Integer.parseInt(scanner.nextLine());
                        if (albumsCount < 0) {
                            System.out.print("The number mustn't be lower than 0. Try again: ");
                        }
                        else break;
                    } catch (NumberFormatException e) {
                        System.out.print("Wrong format. Try again. ");
                    }
                }
                else {
                    String input = scanner.nextLine();
                    System.out.println(String.format("input = %s", input));
                }
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        System.out.print("Enter the genre(PROGRESSIVE_ROCK, PSYCHEDELIC_ROCK, POST_ROCK, POST_PUNK, BRIT_POP): ");
        try {
            while (genre==null) {
                if(scanner.hasNext()) {
                    try {
                        genre = scanner.nextLine();
                        if(!MusicBand.checkGenre(genre)) {
                            System.out.print("Wrong format. Try again: ");
                            genre = null;
                        }
                    } catch (NumberFormatException e) {
                        System.out.print("Wrong format. Try again. ");
                    }
                }
                else {
                    String input = scanner.nextLine();
                    System.out.println(String.format("input = %s", input));
                }
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        System.out.print("Enter the studio name: ");
        try {
            if(scanner.hasNext()) {
                try {
                    st_name = scanner.nextLine();

                } catch (NumberFormatException e) {
                    System.out.print("Wrong format. Try again. ");
                }
            }
            else {
                String input = scanner.nextLine();
                System.out.println(String.format("input = %s", input));
            }

        } catch (NoSuchElementException e) {
            System.exit(0);
        }

        System.out.print("Enter the studio address: ");
        try {
            if(scanner.hasNext()) {
                try {
                    address = scanner.nextLine();

                } catch (NumberFormatException e) {
                    System.out.print("Wrong format. Try again. ");
                }
            }
            else {
                String input = scanner.nextLine();
                System.out.println(String.format("input = %s", input));
            }
        } catch (NoSuchElementException e) {
            System.exit(0);
        }


        Coordinates coordinates = new Coordinates(x, y);
        Studio studio = new Studio(st_name, address);
        java.util.Date date = null;
        Long id = null;
        MusicBand m = new MusicBand(id, name, coordinates, date, numberOfParticipants, singlesCount, albumsCount, genre, studio);
        return m;
    }
}
