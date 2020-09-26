package Applicazione.UnitaImmobiliare;

import DipendenteDalTempo.CycleRoutine;
import View.Interazione;

import java.io.Serializable;

class GestoreRegole implements Serializable {

    private transient CycleRoutine cycle;
    UnitaImmobiliare im;

    GestoreRegole(UnitaImmobiliare im) {
        cycle = new CycleRoutine(im);
        this.im = im;
    }

    void disattivaRegola() {
        if (cycle!=null)
            cycle.disattivaRegola();
        else
            System.out.println("Ancora nessuna regola inserita");
    }

    void attivaRegola() {
        if (cycle != null)
            cycle.attivaRegola();
        else
            System.out.println("Ancora nessuna regola inserita");
    }

    void stampaRegole() {
        if (cycle != null)
            cycle.stampaRegole();
        else
            System.out.println("Al momento non son state inserite regole");
    }

    void inserisciRegola() {
        String regola = Interazione.domanda("Inserisci regola 'if antecedente then conseguente' : ");
        try {
            inserisciRegolaCode(regola);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    void inserisciRegolaCode(String regola) throws IllegalArgumentException {
        if (cycle == null)
            cycle = new CycleRoutine(im);
        cycle.aggiungiRegola(regola);
    }

    void terminaThread() {
        if (cycle != null)
            cycle.terminaThread();
    }

}
