package Applicazione.UnitaImmobiliare;

import Applicazione.SistemaDomotico;
import Categorie.CategoriaAttuatore;
import Categorie.CategoriaSensore;
import Categorie.ModalitaOperativa;
import Categorie.Parametro;
import Contenitori.Artefatto;
import Contenitori.Attuatore;
import Contenitori.Sensore;
import Contenitori.Stanza;
import DipendenteDalTempo.CycleRoutine;
import View.Interazione;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UnitaImmobiliare implements java.io.Serializable {
    private String nomeUnitaImmobiliare, tipoUnitaImmobiliare;
    private Map<String, Stanza> stanze = new HashMap<>();
    private Map<String, Stanza> artefatti = new HashMap<>();//chiave nome artefatto valore stanza associata
    private SistemaDomotico sistemaDomotico;
    private boolean almenoUnSensore = false;
    private ControllerUnitaImmobiliare controller;

    UnitaImmobiliare(ControllerUnitaImmobiliare controller) {
        this.controller = controller;
        String nome = Interazione.domanda("Nome dell'unita immobiliare?");
        this.nomeUnitaImmobiliare = nome;
        String tipo = Interazione.domanda("Tipologia unita immobiliare?");
        this.tipoUnitaImmobiliare = tipo;
        stanze.put("Esterno", new Stanza("Esterno"));
        int risposta = Interazione.interrogazione
                ("Bisogna aggiungere almeno una stanza o un artefatto," +
                                " (nel seguito se ne potranno aggiungere altri)",
                        new String[]{"Artefatto", "Stanza"}, false);

        switch (risposta) {
            case 0:
                aggiungiArtefatto();
                break;
            case 1:
                aggiungiStanza();
                break;
        }
    }

    UnitaImmobiliare(String nome, String tipo, ControllerUnitaImmobiliare controller) {
        this.controller = controller;
        nomeUnitaImmobiliare = nome;
        tipoUnitaImmobiliare = tipo;
    }

    boolean aggiungiStanzaCode(String stanza) {
        if (stanze.containsKey(stanza))
            return false;
        else {
            stanze.put(stanza, new Stanza(stanza));
            return true;
        }
    }

    void aggiungiStanza() {
        String risposta = null;
        do {
            String stanza = Interazione.domanda("Inserisci nome stanza : ");

            //Se c'e` gia almeno una stanza controlla che il nome non sia duplicato
            if (!aggiungiStanzaCode(stanza)) {
                System.out.println("Nome stanza gia esistente, non aggiunta");
            }

            risposta = Interazione.domanda("Vuoi uscire? (y/any key) : ");

        } while (!risposta.equals("y"));
    }

    boolean aggiungiArtefattoCode(String nomeStanza, String nomeArtefatto) {
        if (!stanze.get(nomeStanza).aggiungiArtefatto(nomeArtefatto))
            return false;
        else {
            artefatti.put(nomeArtefatto, stanze.get(nomeStanza));
            return true;
        }
    }

    void aggiungiArtefatto() {
        String risposta = null;
        do {

            String artefatto = Interazione.domanda("Inserisci nome artefatto : ");

            String[] elencoStanze = stanze.keySet().toArray(new String[0]);

            int stanzaDaAbbinare = Interazione.interrogazione
                    ("In quale stanza vuoi aggiungere l'artefatto ", elencoStanze, false);
            String nomeStanzaDaAbbinare = elencoStanze[stanzaDaAbbinare];

            if (!aggiungiArtefattoCode(nomeStanzaDaAbbinare, artefatto))
                System.out.println("Artefatto gia esistente in questa stanza");

            risposta = Interazione.domanda("Vuoi uscire? (y/any key) : ");

        } while (!risposta.equals("y"));

    }

    void stampaAlberoUnitaImmobiliare() {

        StringBuilder tree = new StringBuilder();
        tree.append("Nome unita immobiliare :" + nomeUnitaImmobiliare + "\n\n");
        for (Stanza s : stanze.values()) {
            tree.append("\tCategorie sensori nella stanza " + s.getNome() + "\n");
            for (String sensore : s.getCategoriaSensoriPresenti()) {
                tree.append("\t\t" + sensore + "\n");
            }
            tree.append("\n");
            tree.append("\tCategorie attuatori nella stanza " + s.getNome() + "\n");
            for (String attuatore : s.getCategoriaAttuatoriPresenti()) {
                tree.append("\t\t" + attuatore + "\n");
            }
            tree.append("\n");
            for (Artefatto a : s.getArtefatti()) {
                tree.append("\t\tCategorie sensori dell'artefatto " + a.getNome() + " nella stanza\n");
                for (String sensore : a.getCategoriaSensoriPresenti()) {
                    tree.append("\t\t\t" + sensore + "\n");
                }
                tree.append("\n");
                tree.append("\t\tCategorie attuatori dell'artefatto " + a.getNome() + " nella stanza\n");
                for (String attuatore : a.getCategoriaAttuatoriPresenti()) {
                    tree.append("\t\t\t" + attuatore + "\n");
                }
                tree.append("\n");
            }
        }
        System.out.println(tree.toString());
    }

    String getNomeUnitaImmobiliare() {
        return nomeUnitaImmobiliare;
    }

    Map<String, Stanza> getStanze() {
        return stanze;
    }

    Map<String, Stanza> getArtefatti() {
        return artefatti;
    }

    void setAlmenoUnSensore(boolean almenoUnSensore) {
        this.almenoUnSensore = almenoUnSensore;
    }

    public ControllerUnitaImmobiliare getController() {
        return controller;
    }

    public String getTipoUnitaImmobiliare() {
        return tipoUnitaImmobiliare;
    }
}

