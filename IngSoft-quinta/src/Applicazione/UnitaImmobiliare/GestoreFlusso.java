package Applicazione.UnitaImmobiliare;

import View.Interazione;

import java.io.Serializable;

class GestoreFlusso implements Serializable {
    UnitaImmobiliare im;
    GestoreRegole regole;
    SensoriCreator creator;
    LetturaDaFile lf;
    GestoreSensori sensori;

    GestoreFlusso(UnitaImmobiliare im, GestoreRegole regole, SensoriCreator creator, LetturaDaFile lf, GestoreSensori sensori) {
        this.im = im;
        this.regole = regole;
        this.creator = creator;
        this.lf = lf;
        this.sensori = sensori;
    }

    void flussoFruitore() {
        int risposta;
        do {
            risposta = Interazione.interrogazione("Cosa vuoi fare?",
                    new String[]{"Visualizza piantina unita' immobiliare",
                            "Visualizza valori di sensori specifici",
                            "Agisci sugli attuatori",
                            "Inserisci regola",
                            "Stampa regole gia` inserite",
                            "Disattiva regola",
                            "Attiva regola",
                            "Disattiva sensore",
                            "Disattiva attuatore",
                            "Attiva sensore",
                            "Attiva attuatore"
                    }, true);
            switch (risposta) {
                case 0:
                    im.stampaAlberoUnitaImmobiliare();
                    break;
                case 1:
                    sensori.proceduraLetturaSensori();
                    break;
                case 2:
                    sensori.proceduraAttuatori();
                    break;
                case 3:
                    regole.inserisciRegola();
                    break;
                case 4:
                    regole.stampaRegole();
                    break;
                case 5:
                    regole.disattivaRegola();
                    break;
                case 6:
                    regole.attivaRegola();
                    break;
                case 7:
                    sensori.attivaDisattivaSensore(false);
                    break;
                case 8:
                    sensori.attivaDisattivaAttuatore(false);
                    break;
                case 9:
                    sensori.attivaDisattivaSensore(true);
                    break;
                case 10:
                    sensori.attivaDisattivaAttuatore(true);
                    break;
            }
        } while (risposta != -1);
    }

    void flussoManutentore() {
        boolean esci = false;
        do {
            int risposta = Interazione.interrogazione("Cosa vuoi fare (qualsiasi altro tasto per uscire) :",
                    new String[]{"Aggiungi stanze",
                            "Aggiungi artefatti",
                            "Aggiungi sensore",
                            "Aggiungi attuatore",
                            "Visualizza albero associazioni",
                            "Importa regole da file"
                    }, true);
            switch (risposta) {
                case 0:
                    im.aggiungiStanza();
                    break;
                case 1:
                    im.aggiungiArtefatto();
                    break;
                case 2:
                    creator.aggiungiSensore();
                    break;
                case 3:
                    creator.aggingiAttuatore();
                    break;
                case 4:
                    im.stampaAlberoUnitaImmobiliare();
                    break;
                case 5:
                    lf.importaRegoleDaFile();
                    break;
                default:
                    esci = true;
                    break;
            }
        } while (!esci);
    }
}
