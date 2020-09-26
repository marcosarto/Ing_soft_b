package Applicazione.UnitaImmobiliare;

import Categorie.CategoriaAttuatore;
import Categorie.ModalitaOperativa;
import Categorie.Parametro;
import Contenitori.Artefatto;
import Contenitori.Attuatore;
import Contenitori.Sensore;
import Contenitori.Stanza;
import View.Interazione;

import java.io.Serializable;

class GestoreSensori implements Serializable {

    UnitaImmobiliare im;
    ControllerUnitaImmobiliare controller;

    GestoreSensori(UnitaImmobiliare im, ControllerUnitaImmobiliare controller) {
        this.im = im;
        this.controller = controller;
    }

    void attivaDisattivaSensore(boolean attiva) {
        if(!almenoUnSensore(!attiva)){
            System.out.println("Non ci sono sensori da commutare");
            return;
        }
        String risp;
        do {
            elencaSensoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome sensore che vuoi mutare : ");
            Sensore val = cercaSensoreRestituisciSensore(risposta);
            if (val != null) {
                val.setAttivo(attiva);
            } else
                System.out.println("Nome sensore non corretto");
            risp = Interazione.domanda("Vuoi mutare un altro sensore? (y/any key)");
        } while (risp.equals("y"));
    }

    void attivaDisattivaAttuatore(boolean attiva) {
        if(!almenoUnAttuatore(!attiva)){
            System.out.println("Non ci sono attuatori da commutare");
            return;
        }
        String valA;
        do {
            elencaAttuatoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome attuatore che vuoi comandare : ");
            Attuatore val = cercaAttuatoreRestituisciAttuatore(risposta);

            if (val != null) {
                val.setAttivo(attiva);
            } else
                System.out.println("Il nome attuatore non e` corretto");
            valA = Interazione.domanda("Vuoi controllare un altro attuatore? (y/any key)");
        } while (valA.equals("y"));
    }

    void elencaSensoriUnitaImmobiliare() {
        for (Stanza s : im.getStanze().values()) {
            System.out.println("Sensori nella stanza " + s.getNome());
            System.out.println(s.getSensori());
            System.out.println("Sensori sugli artifatti della stanza " + s.getNome());
            for (Artefatto a : s.getArtefatti()) {
                System.out.println(a.getSensori());
            }
            System.out.println(Interazione.DELIMITATORE);
        }
    }

    void proceduraLetturaSensori() {
        do {
            elencaSensoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome sensore che vuoi ispezionare : ");
            String val = cercaSensoreRestituisciValore(risposta);
            if (!val.isEmpty()) {
                System.out.println("Il valore del sensore e` : " + val);
            }
            val = Interazione.domanda("Vuoi controllare un altro sensore? (y/any key)");
            if (!val.equals("y"))
                return;
        } while (true);
    }

    String cercaSensoreRestituisciValore(String risposta) {
        String val = "";
        for (Stanza s : im.getStanze().values()) {
            if (val.isEmpty())
                val = s.ritornaValoreSensore(risposta);
            for (Artefatto a : s.getArtefatti()) {
                if (val.isEmpty())
                    val = a.ritornaValoreSensore(risposta);
            }
        }
        return val;
    }

    Sensore cercaSensoreRestituisciSensore(String risposta) {
        Sensore val = null;
        for (Stanza s : im.getStanze().values()) {
            if (val == null)
                val = s.ritornaRiferimentoSensore(risposta);
            for (Artefatto a : s.getArtefatti()) {
                if (val == null)
                    val = a.ritornaRiferimentoSensore(risposta);
            }
        }
        return val;
    }

    void elencaAttuatoriUnitaImmobiliare() {
        for (Stanza s : im.getStanze().values()) {
            System.out.println("Attuatori nella stanza " + s.getNome());
            System.out.println(s.getAttuatori());
            System.out.println("Attuatori sugli artifatti della stanza " + s.getNome());
            for (Artefatto a : s.getArtefatti()) {
                System.out.println(a.getAttuatori());
            }
            System.out.println(Interazione.DELIMITATORE);
        }
    }

    void proceduraAttuatori() {
        do {
            elencaAttuatoriUnitaImmobiliare();
            String risposta = Interazione.domanda("Inserisci nome attuatore che vuoi comandare : ");
            Attuatore val = cercaAttuatoreRestituisciAttuatore(risposta);

            if (val != null) {
                CategoriaAttuatore cat = (CategoriaAttuatore) val.getCategoria();
                System.out.println("Attuatore di categoria " + cat.getNome());
                int risp = Interazione.interrogazione("Quale modalita operativa vuoi comandare : ",
                        cat.getModalitaOperativa().keySet().toArray(new String[0]), false);
                String modS = cat.getModalitaOperativa().keySet().toArray(new String[0])[risp];
                ModalitaOperativa mod = cat.getModalitaOperativa().get(modS);
                if (mod.isAStati()) {
                    if (val.getStatoCorrente(modS) == null)
                        System.out.println("Al momento questo attuatore non ha uno stato settato");
                    else
                        System.out.println("Stato corrente " + val.getStatoCorrente(modS));
                    risp = Interazione.interrogazione("Quale stato vuoi applicare? ",
                            mod.getStati().toArray(new String[0]), false);
                    String nuovoStato = mod.getStati().toArray(new String[0])[risp];
                    val.setStatoCorrente(modS, nuovoStato);
                } else {
                    for (Parametro p : mod.getParametri().values()) {
                        System.out.println("Il parametro " + p.getNome() + " ha un valore desiderato di " + p.getValoreDesiderato());
                    }
                    String parametroDaModificare = Interazione.domanda("Quale parametro vuoi cambiare? : ");
                    if (!mod.getParametri().containsKey(parametroDaModificare))
                        System.out.println("Non esiste un parametro con quel nome");
                    boolean fineTransizione = false;
                    do {
                        try {
                            String valoreDesiderato = Interazione.domanda("Che valore vuoi impostare? : ");
                            mod.getParametri().get(parametroDaModificare).setValoreDesiderato(Integer.parseInt(valoreDesiderato));
                            fineTransizione = true;
                        } catch (Exception e) {
                            System.out.println("Devi inserire un valore numerico");
                        }
                    } while (!fineTransizione);
                }
            } else {
                System.out.println("Attuatore non trovato ");
            }
            String valA = Interazione.domanda("Vuoi controllare un altro attuatore? (y/any key)");
            if (!valA.equals("y"))
                return;

        } while (true);
    }

    Attuatore cercaAttuatoreRestituisciAttuatore(String risposta) {
        Attuatore val = null;
        for (Stanza s : im.getStanze().values()) {
            if (s.ritornaRiferimentoAttuatore(risposta) != null) {
                val = s.ritornaRiferimentoAttuatore(risposta);
            }
            for (Artefatto a : s.getArtefatti()) {
                if (a.ritornaRiferimentoAttuatore(risposta) != null) {
                    val = a.ritornaRiferimentoAttuatore(risposta);
                }
            }
        }
        return val;
    }

    boolean almenoUnAttuatore(boolean attivo){
        for(Stanza s : im.getStanze().values()){
            if(s.almenoUnAttuatore(attivo))
                return true;
            for(Artefatto a : s.getArtefatti()){
                if(a.almenoUnAttuatore(attivo))
                    return true;
            }
        }
        return false;
    }

    boolean almenoUnSensore(boolean attivo){
        for(Stanza s : im.getStanze().values()){
            if(s.almenoUnSensore(attivo))
                return true;
            for(Artefatto a : s.getArtefatti()){
                if(a.almenoUnSensore(attivo))
                    return true;
            }
        }
        return false;
    }

    boolean aggiungiCategoriaSensoreAStanza(String stanza, String catSensore) {
        return im.getStanze().get(stanza).aggiungiCategoriaSensoriPresenti(catSensore);
    }

    boolean aggiungiCategoriaAttuatoreAStanza(String stanza, String catAttuatore) {
        return im.getStanze().get(stanza).aggiungiCategoriaAttuatoriPresenti(catAttuatore);
    }

    boolean aggiungiCategoriaSensoreAArtefatto(String stanza, String artefatto, String catSensore) {
        if (stanza.contains(stanza))
            return im.getStanze().get(stanza).aggiungiCategoriaSensoriPresentiSuArtifatto(artefatto, catSensore);
        return false;
    }

    void aggiungiCategoriaAttuatoreAArtefatto(String stanza, String artefatto, String catAttuatore) {
        if (stanza.contains(stanza))
            im.getStanze().get(stanza).aggiungiCategoriaAttuatoriPresentiSuArtifatto(artefatto, catAttuatore);
    }
}
