package Categorie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModalitaOperativa implements Serializable {
    private String nome;
    private ArrayList<String> stati = new ArrayList<>();
    private Map<String, Parametro> parametri = new HashMap<>();
    private String statoCorrente;

    private boolean aStati;

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void aggiungiStato(String stato){
        stati.add(stato);
        aStati = true;
    }

    public void setStatoCorrente(String statoCorrente) {
        this.statoCorrente = statoCorrente;
    }

    public String getStatoCorrente() { return statoCorrente;}

    public boolean isAStati(){
        return aStati;
    }

    public ArrayList<String> getStati(){
        return stati;
    }

    public Map<String, Parametro> getParametri() {
        return parametri;
    }

    public void addParametro(String nome , Parametro p){
        parametri.put(nome,p);
        aStati=false;
    }

    public void setValoriParametri(String nomeParametro, int valParametro){
        if(parametri.containsKey(nomeParametro))
            parametri.get(nomeParametro).setValoreDesiderato(valParametro);
    }

    public Integer ritornaValoreParametri(String par){
        if(parametri.containsKey(par))
            return parametri.get(par).getValoreDesiderato();
        else
            return -1;
    }


}
