package Applicazione;

import Applicazione.UnitaImmobiliare.UnitaImmobiliare;
import View.Interazione;

import java.io.Serializable;

public class GestoreFlusso implements Serializable {
    CategorieCreator creator;
    SistemaDomotico sm;
    LetturaDaFile lf;

    public GestoreFlusso(CategorieCreator creator, SistemaDomotico sm, LetturaDaFile lf) {
        this.creator = creator;
        this.sm = sm;
        this.lf = lf;
    }

    public void run() {
        String uscita = null;
        do {
            int risposta = Interazione.interrogazione("Seleziona la modalita` operativa",
                    new String[]{"Utente", "Manutentore"}, true);

            switch (risposta) {
                case 0:
                    flussoFruitore();
                    break;
                case 1:
                    flussoManutentore();
                    break;
            }
            uscita = Interazione.domanda("Vuoi uscire (y/any key) : ");
        } while (!uscita.equals("y"));
    }

    private void flussoManutentore() {
        boolean esci = false;
        do {
            int risposta = Interazione.interrogazione("Cosa vuoi fare(qualsiasi altro tasto per uscire) :",
                    new String[]{"Aggiungi categorie sensori",
                            "Aggiungi categoria attuatori",
                            "Creare e descrivere l'unita` immobiliare",
                            "Selezionare un'unita` immobiliare per lavorarci",
                            "Importa categorie sensori",
                            "Importa categoria attuatori",
                            "Importa unita immobiliari"
                    }, true);
            switch (risposta) {
                case 0:
                    creator.aggiungiCategorieSensori();
                    break;
                case 1:
                    creator.aggiungiCategoriaAttuatori();
                    break;
                case 2:
                    sm.aggiungiUnitaImmobiliare();
                    break;
                case 3:
                    int numeroUnitaImm = sm.visualizzaElencoUnitaImmobiliari();
                    sm.getUnitaImmobiliari().get(numeroUnitaImm).getController().flussoManutentore();
                    break;
                case 4:
                    lf.importaCategorieDaFile("importaCategorieSensori.txt", true);
                    break;
                case 5:
                    lf.importaCategorieDaFile("importaCategorieAttuatori.txt", false);
                    break;
                case 6:
                    lf.importaUnitaImmobiliari();
                    break;
                default:
                    esci = true;
            }
        } while (!esci);
    }

    private void flussoFruitore() {
        if (sm.getUnitaImmobiliari().size() == 0) {
            System.out.println("Non e` ancora stata creata nessuna unita` immobiliare, contatta il manutentore");
        } else {
            int numeroUnitaImmobiliare = sm.visualizzaElencoUnitaImmobiliari();
            if (numeroUnitaImmobiliare == -1)
                return;
            sm.getUnitaImmobiliari().get(numeroUnitaImmobiliare).getController().flussoFruitore();
        }
    }
}
