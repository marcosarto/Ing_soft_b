package Categorie;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class CaratteristicheAttuatore implements Serializable,Cloneable {
    private Map<String, ModalitaOperativa> modalita = new HashMap<>();

    public void addModalita(ModalitaOperativa modalita) {
        this.modalita.put(modalita.getNome(),modalita);
    }
    public Map<String, ModalitaOperativa> getModalita(){return modalita;}

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
