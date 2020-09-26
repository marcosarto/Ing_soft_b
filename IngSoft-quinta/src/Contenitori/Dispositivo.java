package Contenitori;

import Categorie.CategoriaDispositivo;
import javafx.beans.InvalidationListener;

import java.io.Serializable;
import java.util.Observable;

public abstract class Dispositivo extends Observable implements Serializable {
    protected CategoriaDispositivo categoria;
    protected String nome;
    protected boolean attivo;

    public Dispositivo(CategoriaDispositivo categoria, String nome) {
        this.categoria = categoria;
        this.nome = nome;
        attivo = true;
    }

    public boolean isAttivo() {
        return attivo;
    }

    public void setAttivo(boolean attivo) {
        if(attivo!=this.attivo) setChanged();
        notifyObservers(attivo);
        this.attivo = attivo;
    }


    public String getNome() {
        return nome;
    }

    public CategoriaDispositivo getCategoria() {
        return categoria;
    }
}
