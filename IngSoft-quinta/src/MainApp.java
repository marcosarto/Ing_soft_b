import Applicazione.ControllerSistemaDomotico;
import Applicazione.Salvataggio;

public class MainApp {
    public static void main(String[] args) {
        ControllerSistemaDomotico controller = ControllerSistemaDomotico.getInstance();
        controller.iniziaFlusso();
        controller.salva();
    }
}
