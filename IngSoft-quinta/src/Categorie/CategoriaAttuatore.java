package Categorie;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CategoriaAttuatore extends CategoriaDispositivo implements Serializable {
    private CaratteristicheAttuatore caratteristicheOperative = new CaratteristicheAttuatore();

    public CategoriaAttuatore(String nome) {
        super(nome);
    }

    public void addModalita(ModalitaOperativa modalita) {
        caratteristicheOperative.addModalita(modalita);
    }

    public Map<String, ModalitaOperativa> getModalitaOperativa(){
        return caratteristicheOperative.getModalita();
    }

    public CaratteristicheAttuatore clone(){
        try {
            return (CaratteristicheAttuatore)caratteristicheOperative.clone();
        }catch(Exception e){}
        return null;
    }
}
