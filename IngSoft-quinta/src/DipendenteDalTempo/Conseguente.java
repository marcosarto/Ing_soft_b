package DipendenteDalTempo;

import Categorie.ModalitaOperativa;
import Contenitori.Attuatore;

public class Conseguente{
    private ModalitaOperativa dati;
    private String statoVoluto,nomeParametro,nomeAttuatore,nomeModOp;
    private Attuatore attuatore;
    private int valParametro;
    private boolean aStati = false;

    public Conseguente(ModalitaOperativa dati,String statoVoluto,Attuatore a,String nomeModOp){
        this.nomeAttuatore = a.getNome();
        this.attuatore = a;
        this.nomeModOp = nomeModOp;
        aStati = true;
        this.dati = dati;
        this.statoVoluto = statoVoluto;
    }

    public Conseguente(ModalitaOperativa dati,String nomeParametro,int valParametro,Attuatore a,String nomeModOp){
        this.nomeAttuatore = a.getNome();
        this.attuatore = a;
        this.nomeModOp = nomeModOp;
        this.nomeParametro = nomeParametro;
        this.valParametro = valParametro;
        this.dati = dati;
        aStati = false;
    }

    public void aziona() {
        if (attuatore.isAttivo()) {
            if (aStati) {
                dati.setStatoCorrente(statoVoluto);
                System.out.println("Attuatore " + nomeAttuatore + " modOp " + nomeModOp + " impostato a " + statoVoluto);
            } else {
                dati.setValoriParametri(nomeParametro, valParametro);
                System.out.println("Attuatore " + nomeAttuatore + "modOp " + nomeModOp + "parametro " + nomeParametro + " impostato a " + valParametro);
            }
        }
    }

}
