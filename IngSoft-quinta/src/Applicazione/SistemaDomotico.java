package Applicazione;

import Applicazione.UnitaImmobiliare.ControllerUnitaImmobiliare;
import Applicazione.UnitaImmobiliare.UnitaImmobiliare;
import Categorie.*;
import View.Interazione;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SistemaDomotico implements Serializable {
    private List<UnitaImmobiliare> unitaImmobiliari = new ArrayList();
    private List<CategoriaDispositivo> categorieSensori = new ArrayList();
    private List<CategoriaDispositivo> categorieAttuatori = new ArrayList();

    void aggiungiUnitaImmobiliare() {
        unitaImmobiliari.add(new ControllerUnitaImmobiliare(this).creaUnitaImmobiliare());
    }

    void aggiungiUnitaImmobiliare(UnitaImmobiliare um){unitaImmobiliari.add(um);}

    void aggiungiCategoriaSensore(CategoriaSensore cs){categorieSensori.add(cs);}

    void aggiungiCategoriaAttuatore(CategoriaAttuatore ca){categorieAttuatori.add(ca);}

    int visualizzaElencoUnitaImmobiliari() {
        //riempie entrateUnitaImmobiliare con i nomi delle unita immobiliari
        String[] entrateUnitaImmobiliare = new String[unitaImmobiliari.size()];
        if (entrateUnitaImmobiliare.length == 0) {
            System.out.println("Nessuna unita immobiliare ancora aggiunta");
            return -1;
        }
        for (int i = 0; i < entrateUnitaImmobiliare.length; i++) {
            entrateUnitaImmobiliare[i] = unitaImmobiliari.get(i).getController().getNomeUnitaImmobiliare();
        }

        int risposta = Interazione.interrogazione("Seleziona l'unita' immobiliare che vuoi ispezionare :",
                entrateUnitaImmobiliare, true);
        return risposta;
    }

    boolean controlloNomiCategoria(String nome, List<CategoriaDispositivo> lista) {
        for (CategoriaDispositivo c :
                lista) {
            if (c.getNome().equals(nome))
                return true;
        }
        return false;
    }

    public List<CategoriaDispositivo> getCategorieSensori() {
        return categorieSensori;
    }

    public List<CategoriaDispositivo> getCategorieAttuatori() {
        return categorieAttuatori;
    }

    boolean isCategoriaSensorePresente(String cat){
        for(CategoriaDispositivo c : categorieSensori){
            if(c.getNome().equals(cat))
                return true;
        }
        return false;
    }

    boolean isCategoriaAttuatorePresente(String cat){
        for(CategoriaDispositivo c : categorieAttuatori){
            if(c.getNome().equals(cat))
                return true;
        }
        return false;
    }

    List<UnitaImmobiliare> getUnitaImmobiliari() {
        return unitaImmobiliari;
    }
}
