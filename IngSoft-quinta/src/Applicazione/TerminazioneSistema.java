package Applicazione;

import Applicazione.UnitaImmobiliare.UnitaImmobiliare;

import java.util.List;

public class TerminazioneSistema {
    public static void terminaThreads(List<UnitaImmobiliare> l) {
        for (UnitaImmobiliare u : l) {
            u.getController().terminaThread();
        }
    }
}
