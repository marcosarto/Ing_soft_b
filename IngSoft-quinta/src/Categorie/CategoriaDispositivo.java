package Categorie;

import java.io.Serializable;

public class CategoriaDispositivo implements Serializable {
    private String nome,descrizione;
    private boolean stato;

    public CategoriaDispositivo(String nome) {
        this.nome = nome;
        stato = true;
    }

    public String getUnitaMisura(int pos){
        if(pos>=descrizione.split(",").length)
            return "NotDefined";
        return descrizione.split(",")[pos];
    }

    public String getNome() {
        return nome;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
