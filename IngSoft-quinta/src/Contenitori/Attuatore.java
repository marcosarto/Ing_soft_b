package Contenitori;

import Categorie.CategoriaAttuatore;
import Categorie.ModalitaOperativa;
import Categorie.Parametro;

import java.io.Serializable;
import java.util.Map;

public class Attuatore extends Dispositivo implements Serializable {

    private Map<String, ModalitaOperativa> datiAttuali;

    public Attuatore(CategoriaAttuatore categoria, String nome) {
        super(categoria, nome);
        riempiDatiAttuali();
    }

    private void riempiDatiAttuali(){
        datiAttuali = ((CategoriaAttuatore)categoria).clone().getModalita();
    }


    public String getStatoCorrente(String mod) {
        return datiAttuali.get(mod).getStatoCorrente();
    }

    public void setStatoCorrente(String mod,String statoCorrente) {
        datiAttuali.get(mod).setStatoCorrente(statoCorrente);
    }

    public Map<String, ModalitaOperativa> getDatiAttuali() {
        return datiAttuali;
    }
}
