package Applicazione;

import Applicazione.UnitaImmobiliare.ControllerUnitaImmobiliare;
import Applicazione.UnitaImmobiliare.UnitaImmobiliare;
import Categorie.CategoriaAttuatore;
import Categorie.CategoriaSensore;
import Categorie.ModalitaOperativa;
import Categorie.Rilevazione;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

public class LetturaDaFile implements Serializable {
    CategorieCreator creator;
    SistemaDomotico sm;

    public LetturaDaFile(SistemaDomotico sm,CategorieCreator creator) {
        this.sm = sm;
        this.creator = creator;
    }

    void aggiungiCatAttuatoreDaFile(String riga) {
        String[] campi = riga.split("-");

        CategoriaAttuatore categoriaAttuatore = creator.creaCategoriaAttuatore(campi[0]);
        if (categoriaAttuatore == null) return;

        if (!creator.aggiungiDescrizioneCatAttuatore(categoriaAttuatore, campi[1])) return;

        int pos = 3;
        boolean esci = false;
        do {
            ModalitaOperativa modalitaOperativa = creator.creaModOp(campi[pos - 1]);
            if (campi[pos].equals("1")) {
                do {
                    pos++;
                    creator.aggiungiStato(modalitaOperativa, campi[pos]);
                } while (!campi[pos + 1].equals("0"));
                pos = pos + 2;
            } else {
                do {
                    pos++;
                    creator.aggiungiParametro(modalitaOperativa, campi[pos]);
                } while (!campi[pos + 1].equals("0"));
                pos = pos + 2;
            }
            categoriaAttuatore.addModalita(modalitaOperativa);
            if (!(pos < campi.length)) esci = true;
        } while (!esci);
    }

    void importaCategorieDaFile(String file, boolean sensori) {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(file));
            String daButtare = reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                if (sensori)
                    aggiungiCatSensoreDaFile(line);
                else
                    aggiungiCatAttuatoreDaFile(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void importaUnitaImmobiliari() {
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("importaUnitaImmobiliari.txt"));
            String daButtare = reader.readLine();
            String line = reader.readLine();
            while (line != null) {
                importaUnitaImmobiliariRiga(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    UnitaImmobiliare importaUnitaImmobiliariRiga(String riga) {
        String[] campi = riga.split("-");
        UnitaImmobiliare unitaImmobiliare = new ControllerUnitaImmobiliare(sm).creaUnitaImmobiliare(campi[0], campi[1]);
        int pos = 2;
        if (campi[pos].equals("")) {
            System.out.println("Devi inserire almeno una stanza riga " + riga + " NON INSERITA");
            return null;
        }
        do {
            String stanzaCorrente = campi[pos];
            unitaImmobiliare.getController().aggiungiStanzaCode(stanzaCorrente);
            pos++;
            if (!campi[pos].equals("")) {
                do {
                    //Aggiungo solo le categorie che sono contenute nel sistema domotico ma non lancio errori in caso contrario
                    //nel caso basterebbe mettere un messaggio nell'else
                    if (sm.isCategoriaSensorePresente(campi[pos]))
                        unitaImmobiliare.getController().aggiungiCategoriaSensoreAStanza(stanzaCorrente, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
            if (!campi[pos].equals("")) {
                do {
                    if (sm.isCategoriaAttuatorePresente(campi[pos]))
                        unitaImmobiliare.getController().aggiungiCategoriaAttuatoreAStanza(stanzaCorrente, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
            String artefatto="";
            if (!campi[pos].equals("")) {
                artefatto = campi[pos];
                unitaImmobiliare.getController().aggiungiArtefattoCode(stanzaCorrente, artefatto);
            }
            pos++;
            if (!campi[pos].equals("")) {
                if(artefatto.equals(""))
                    System.out.println("Non puoi aggiungere categorie di sensori senza un artefatto riga "+riga+" ERRORE");
                do {
                    if (sm.isCategoriaSensorePresente(campi[pos]))
                        unitaImmobiliare.getController().aggiungiCategoriaSensoreAArtefatto(stanzaCorrente, artefatto, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
            if (!campi[pos].equals("")) {
                if(artefatto.equals(""))
                    System.out.println("Non puoi aggiungere categorie di attuatori senza un artefatto riga "+riga+" ERRORE");
                do {
                    if (sm.isCategoriaAttuatorePresente(campi[pos]))
                        unitaImmobiliare.getController().aggiungiCategoriaAttuatoreAArtefatto(stanzaCorrente,artefatto, campi[pos]);
                    pos++;
                } while (!campi[pos].equals("0"));
            }
            pos++;
        }while(pos<campi.length);
        sm.aggiungiUnitaImmobiliare(unitaImmobiliare);
        return unitaImmobiliare;
    }

    void aggiungiCatSensoreDaFile(String riga) {
        String[] campi = riga.split("-");
        //0 nome 1 descrizione 2 nome rilevazione 3 (1 se numerica 2 se a stati)
        //caso 3 sia 1 allora 4 min 5 max, da li riinizio come se leggessi il 3
        //caso 3 sia 2 allora da 4 in poi sono stati del sensore fino a 0, da li riinizio come se leggessi il 3
        CategoriaSensore categoriaSensore = creator.creaCatSensore(campi[0]);
        if (categoriaSensore == null) return;

        if (!creator.aggiungiDescrizioneCatSensore(categoriaSensore, campi[1])) return;

        int counterRilevazioniPerUnitaMisura = 0;
        int pos = 3;
        boolean esci = false;
        do {
            Rilevazione rilevazione = null;
            if (campi[pos].equals("1")) {
                try {
                    String unitaDiMisura = categoriaSensore.getUnitaMisura(counterRilevazioniPerUnitaMisura);
                    int min = Integer.parseInt(campi[pos + 1]);
                    int max = Integer.parseInt(campi[pos + 2]);
                    rilevazione = creator.creaRilevazioneNumerica(campi[pos - 1], min, max, unitaDiMisura);
                    pos = pos + 4;
                } catch (Exception e) {
                    System.out.println("Errore nella conversione del numero");
                }
            } else {
                String elemento = campi[pos + 1];
                rilevazione = creator.creaRilevazioneStati(campi[pos - 1], elemento);
                do {
                    pos++;
                    elemento = campi[pos + 1];
                    if (!elemento.equals("0")) creator.aggiungiRilevazioneStato(rilevazione, elemento);
                } while (!elemento.equals("0"));
                pos = pos + 3;
            }
            categoriaSensore.setInformazioni(rilevazione);
            counterRilevazioniPerUnitaMisura++;
            if (pos > campi.length) esci = true;
        } while (!esci);

    }
}
