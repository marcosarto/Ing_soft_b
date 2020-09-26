package Applicazione.UnitaImmobiliare;

import Applicazione.SistemaDomotico;
import Categorie.CategoriaAttuatore;
import Categorie.CategoriaSensore;
import Contenitori.Attuatore;
import Contenitori.Sensore;
import View.Interazione;

import java.io.Serializable;

class SensoriCreator implements Serializable {
    SistemaDomotico sistemaDomotico;
    GestoreSensori sensori;
    UnitaImmobiliare im;

    SensoriCreator(SistemaDomotico sistemaDomotico, GestoreSensori sensori, UnitaImmobiliare im) {
        this.sistemaDomotico = sistemaDomotico;
        this.sensori = sensori;
        this.im = im;
    }

    Sensore creaSensore() {
        String nome = Interazione.domanda("Nome fantasia sensore");
        String[] nomiCat = new String[sistemaDomotico.getCategorieSensori().size()];
        for (int i = 0; i < nomiCat.length; i++) {
            nomiCat[i] = sistemaDomotico.getCategorieSensori().get(i).getNome();
        }
        if (nomiCat.length == 0) {
            System.out.println("Al momento non hai categorie sensori tra cui scegliere, ritorno al menu");
            return null;
        }
        int cat = Interazione.interrogazione("Scegli la categoria del sensore", nomiCat, false);
        if(sensori.cercaSensoreRestituisciSensore(nome+"_"+nomiCat[cat])!=null) {
            System.out.println("Esiste gia un sensore di questa categoria con questo nome");
            return null;
        }
        return new Sensore((CategoriaSensore) sistemaDomotico.getCategorieSensori().get(cat), nome);
    }

    Attuatore creaAttuatore() {
        String nome = Interazione.domanda("Nome fantasia attuatore");
        String[] nomiCat = new String[sistemaDomotico.getCategorieAttuatori().size()];
        for (int i = 0; i < sistemaDomotico.getCategorieAttuatori().size(); i++) {
            nomiCat[i] = sistemaDomotico.getCategorieAttuatori().get(i).getNome();
        }
        if (nomiCat.length == 0) {
            System.out.println("Al momento non hai categorie attuatori tra cui scegliere, ritorno al menu");
            return null;
        }
        int cat = Interazione.interrogazione("Scegli la categoria dell'attuatore", nomiCat, false);
        if(sensori.cercaAttuatoreRestituisciAttuatore(nome+"_"+nomiCat[cat])!=null) {
            System.out.println("Esiste gia un attuatore di questa categoria con questo nome");
            return null;
        }
        return new Attuatore((CategoriaAttuatore) sistemaDomotico.getCategorieAttuatori().get(cat), nome);
    }

    void aggiungiSensore() {
        Sensore sensoreCorrente = creaSensore();
        if (sensoreCorrente == null)
            return;
        int risposta = Interazione.interrogazione("Dove vuoi posizionare il sensore?",
                new String[]{"Artefatto", "Stanza"}, false);
        if (risposta == 0) {
            if (im.getArtefatti().keySet().size() == 0) {
                System.out.println("Non hai artefatti presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovoArtefatto = false;
            do {
                StringBuilder st = new StringBuilder();
                st.append("Scegli il l'artefatto (inserisci il nome) : " + "\n");
                for (String s : im.getArtefatti().keySet()) {
                    st.append(s + "\n");
                }
                String artefattoScelto = Interazione.domanda(st.toString());
                if (!im.getArtefatti().containsKey(artefattoScelto))
                    System.out.println("Nome non corretto o non esistenete");
                else {
                    if (!im.getArtefatti().get(artefattoScelto).aggiungiSensoreSuArtefatto(artefattoScelto, sensoreCorrente))
                        System.out.println("C'e` gia un sensore di questa categoria!");
                    else
                        im.setAlmenoUnSensore(true);
                }
                if (Interazione.domanda("Vuoi collegarlo ad un altro artefatto? (y/any key)").equals("y"))
                    nuovoArtefatto = true;
                else
                    nuovoArtefatto = false;

            } while (nuovoArtefatto);

        } else {
            if (im.getStanze().keySet().size() == 0) {
                System.out.println("Non hai stanze presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovaStanza = false;
            do {
                String[] elencoStanze = im.getStanze().keySet().toArray(new String[0]);
                risposta = Interazione.interrogazione("In quale stanza? ", elencoStanze, false);
                if (elencoStanze[risposta].equals("Esterno"))
                    System.out.println("Non si puo` aggiungere un sensore all'esterno senza un artefatto");
                else if (!im.getStanze().get(elencoStanze[risposta]).aggiungiSensore(sensoreCorrente))
                    System.out.println("C'e` gia un sensore di questa categoria!");
                else
                    im.setAlmenoUnSensore(true);

                if (Interazione.domanda("Vuoi collegarlo ad un altra stanza? (y/any key)").equals("y"))
                    nuovaStanza = true;
                else
                    nuovaStanza = false;
            } while (nuovaStanza);
        }

    }

    void aggingiAttuatore() {
        Attuatore attuatoreCorrente = creaAttuatore();
        if (attuatoreCorrente == null)
            return;
        int risposta = Interazione.interrogazione("Dove vuoi posizionare l'attuatore?",
                new String[]{"Artefatto", "Stanza"}, false);
        if (risposta == 0) {
            if (im.getArtefatti().keySet().size() == 0) {
                System.out.println("Non hai artefatti presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovoArtefatto = false;
            do {
                StringBuilder st = new StringBuilder();
                st.append("Scegli il l'artefatto (inserisci il nome) : " + "\n");
                for (String s : im.getArtefatti().keySet()) {
                    st.append(s + "\n");
                }
                String artefattoScelto = Interazione.domanda(st.toString());
                if (!im.getArtefatti().containsKey(artefattoScelto))
                    System.out.println("Nome non corretto o non esistenete");
                else {
                    if (!im.getArtefatti().get(artefattoScelto).aggiungiAttuatoreSuArtefatto(artefattoScelto, attuatoreCorrente))
                        System.out.println("C'e` gia un'attuatore di questa categoria!");
                }
                if (Interazione.domanda("Vuoi collegarlo ad un altro artefatto? (y/any key)").equals("y"))
                    nuovoArtefatto = true;
                else
                    nuovoArtefatto = false;

            } while (nuovoArtefatto);

        } else {
            if (im.getStanze().keySet().size() == 0) {
                System.out.println("Non hai stanze presenti al momento, ritorno al menu");
                return;
            }
            boolean nuovaStanza = false;
            do {
                String[] elencoStanze = im.getStanze().keySet().toArray(new String[0]);
                risposta = Interazione.interrogazione("In quale stanza? ", elencoStanze, false);
                if (elencoStanze[risposta].equals("Esterno"))
                    System.out.println("Non si puo` aggiungere un attuatore all'esterno senza un artefatto");
                else if (!im.getStanze().get(elencoStanze[risposta]).aggiungiAttuatore(attuatoreCorrente))
                    System.out.println("C'e` gia un attuatore di questa categoria!");

                if (Interazione.domanda("Vuoi collegarlo ad un altra stanza? (y/any key)").equals("y"))
                    nuovaStanza = true;
                else
                    nuovaStanza = false;
            } while (nuovaStanza);
        }
    }
}
