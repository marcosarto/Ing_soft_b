package Applicazione;

import Categorie.*;
import View.Interazione;

import java.io.Serializable;

public class CategorieCreator implements Serializable {
    SistemaDomotico sm;

    public CategorieCreator(SistemaDomotico sm) {
        this.sm = sm;
    }

    void aggiungiCategorieSensori() {
        String risposta = Interazione.domanda("Nome della categoria : ");
        CategoriaSensore categoriaSensore = creaCatSensore(risposta);
        if (categoriaSensore == null) {
            System.out.print("Gia` presente, ritorno alla schermata precedente");
            return;
        }

        risposta = Interazione.domanda("Testo libero (massimo 180 caratteri) " +
                "(unita` di misura dei sensori numerici divisi da ,): ");
        if (!aggiungiDescrizioneCatSensore(categoriaSensore, risposta)) {
            System.out.println("Il testo supera la lunghezza massima");
            return;
        }

        int counterRilevazioniPerUnitaMisura = 0;
        String isUscita = null;
        do {
            String nome;
            Rilevazione rilevazione = null;
            nome = Interazione.domanda("Nome della rilevazione del sensore : ");
            int scelta = Interazione.interrogazione("Misura un valore numerico o un dominio discreto? ",
                    new String[]{"Valore numerico", "Dominio discreto"}, false);
            if (scelta == 0) {
                try {
                    int min, max;
                    risposta = Interazione.domanda("Minimo rilevabile dal sensore : ");
                    min = Integer.valueOf(risposta);
                    risposta = Interazione.domanda("Massimo rilevabile dal sensore : ");
                    max = Integer.valueOf(risposta);
                    String unitaDiMisura = categoriaSensore.getUnitaMisura(counterRilevazioniPerUnitaMisura);
                    rilevazione = creaRilevazioneNumerica(nome, min, max, unitaDiMisura);
                } catch (Exception e) {
                    System.out.println("Devi inserire un numero!");
                    continue;
                }
            } else if (scelta == 1) {
                risposta = Interazione.domanda("Inserisci primo elemento del dominio");
                rilevazione = creaRilevazioneStati(nome, risposta);
                do {
                    risposta = Interazione.domanda("Inserisci un elemento del dominio (0 per uscire)");
                    if (!risposta.equals("0")) aggiungiRilevazioneStato(rilevazione, risposta);
                } while (!risposta.equals("0"));
            }
            categoriaSensore.setInformazioni(rilevazione);
            counterRilevazioniPerUnitaMisura++;
            isUscita = Interazione.domanda("Vuoi inserire un'altra rilevazione? (y/any key)");
        } while (isUscita.equals("y"));
    }

    void aggiungiCategoriaAttuatori() {
        String risposta = Interazione.domanda("Nome della categoria : ");
        CategoriaAttuatore categoriaAttuatore = creaCategoriaAttuatore(risposta);
        if (categoriaAttuatore == null) {
            System.out.print("Gia` presente, ritorno alla schermata precedente");
            return;
        }
        risposta = Interazione.domanda("Testo libero (massimo 180 caratteri) : ");
        if (!aggiungiDescrizioneCatAttuatore(categoriaAttuatore, risposta)) {
            System.out.println("Il testo supera la lunghezza massima, ritorno schemata precedente");
            return;
        }

        String isEsci;
        do {
            risposta = Interazione.domanda("Nome della modalita` opertiva");
            ModalitaOperativa modalitaOperativa = creaModOp(risposta);
            int scelta = Interazione.interrogazione("Modalita` a stati o parametrica?",
                    new String[]{"A stati", "Parametrica"}, false);
            if (scelta == 0) {
                risposta = Interazione.domanda("Inserisci stati possibili");
                while (!risposta.equals("0")) {
                    aggiungiStato(modalitaOperativa, risposta);
                    risposta = Interazione.domanda("Inserisci stati possibili (0 per uscire)");
                }
            } else {
                risposta = Interazione.domanda("Inserisci nome parametro settabile : ");
                while (!risposta.equals("0")) {
                    aggiungiParametro(modalitaOperativa, risposta);
                    risposta = Interazione.domanda("Inserisci nome parametro settabile (0 per uscire) : ");
                }
            }
            categoriaAttuatore.addModalita(modalitaOperativa);
            isEsci = Interazione.domanda("Vuoi aggiungere una nuova modalita` operativa all'attuatore? (y/any key)");
        } while (isEsci.equals("y"));
    }

    Rilevazione creaRilevazioneStati(String nome, String stato) {
        Rilevazione rilevazione = new Rilevazione();
        rilevazione.setNome(nome);
        rilevazione.aggiungiStato(stato);
        return rilevazione;
    }

    void aggiungiRilevazioneStato(Rilevazione rilevazione, String stato) {
        rilevazione.aggiungiStato(stato);
    }

    CategoriaAttuatore creaCategoriaAttuatore(String nome) {
        if (!sm.controlloNomiCategoria(nome, sm.getCategorieAttuatori()))
            return new CategoriaAttuatore(nome);
        else
            return null;
    }

    boolean aggiungiDescrizioneCatAttuatore(CategoriaAttuatore categoriaAttuatore, String descr) {
        if (descr.length() > 180)
            return false;
        else {
            sm.aggiungiCategoriaAttuatore(categoriaAttuatore);
            categoriaAttuatore.setDescrizione(descr);
            return true;
        }
    }

    ModalitaOperativa creaModOp(String nome) {
        ModalitaOperativa op = new ModalitaOperativa();
        op.setNome(nome);
        return op;
    }

    void aggiungiStato(ModalitaOperativa op, String stato) {
        op.aggiungiStato(stato);
    }

    void aggiungiParametro(ModalitaOperativa op, String nome) {
        op.addParametro(nome, creaParametro(nome));
    }

    Parametro creaParametro(String nome) {
        Parametro p = new Parametro(nome);
        return p;
    }

    CategoriaSensore creaCatSensore(String nome) {
        if (sm.controlloNomiCategoria(nome, sm.getCategorieSensori())) {
            return null;
        } else
            return new CategoriaSensore(nome);
    }

    boolean aggiungiDescrizioneCatSensore(CategoriaSensore categoriaSensore, String descr) {
        if (descr.length() > 180) {
            return false;
        }
        categoriaSensore.setDescrizione(descr);
        sm.aggiungiCategoriaSensore(categoriaSensore);
        return true;
    }

    Rilevazione creaRilevazioneNumerica(String nome, int min, int max, String unitaDiMisura) {
        Rilevazione rilevazione = new Rilevazione();
        rilevazione.setNome(nome);
        rilevazione.setMinimo(min);
        rilevazione.setMassimo(max);
        rilevazione.setUnitaDiMisura(unitaDiMisura);
        return rilevazione;
    }
}
