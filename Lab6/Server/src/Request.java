import java.io.Serializable;
// Класс, с помощью которого мы отправляем или получаем данные о музыкальных группах
public class Request implements Serializable {
    String cmd;
    String arg;
    boolean isExit;
    MusicBand band;
    public Request() {

    }
}