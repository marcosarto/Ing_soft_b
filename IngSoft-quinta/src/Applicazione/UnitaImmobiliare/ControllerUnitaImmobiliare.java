package Applicazione.UnitaImmobiliare;

import Applicazione.SistemaDomotico;
import Contenitori.Attuatore;
import Contenitori.Sensore;

import java.io.Serializable;


public class ControllerUnitaImmobiliare implements Serializable {
    UnitaImmobiliare im;
    GestoreFlusso gf;
    GestoreRegole regole;
    GestoreSensori sensori;
    LetturaDaFile lf;
    SensoriCreator creator;
    SistemaDomotico sm;

    public ControllerUnitaImmobiliare(SistemaDomotico sm){
        this.sm = sm;
    }

    private void fill(){
        regole = new GestoreRegole(im);
        sensori = new GestoreSensori(im,this);
        lf = new LetturaDaFile(im,regole);
        creator = new SensoriCreator(sm,sensori,im);
        gf = new GestoreFlusso(im,regole,creator,lf,sensori);
    }

    public UnitaImmobiliare creaUnitaImmobiliare(){
        im = new UnitaImmobiliare(this);
        fill();
        return im;
    }

    public UnitaImmobiliare creaUnitaImmobiliare(String nome, String tipo){
        im = new UnitaImmobiliare(nome,tipo,this);
        fill();
        return im;
    }

    public void flussoManutentore(){
        gf.flussoManutentore();
    }

    public void flussoFruitore() {gf.flussoFruitore();}

    public boolean aggiungiStanzaCode(String s){
        return im.aggiungiStanzaCode(s);
    }

    public boolean aggiungiArtefattoCode(String nomeStanza, String nomeArtefatto) {
        return im.aggiungiArtefattoCode(nomeStanza,nomeArtefatto);
    }

    public boolean aggiungiCategoriaSensoreAArtefatto(String stanza, String artefatto, String catSensore){
        return sensori.aggiungiCategoriaSensoreAArtefatto(stanza,artefatto,catSensore);
    }

    public boolean aggiungiCategoriaSensoreAStanza(String stanza, String catSensore){
        return sensori.aggiungiCategoriaSensoreAStanza(stanza,catSensore);
    }

    public boolean aggiungiCategoriaAttuatoreAStanza(String stanza, String catAttuatore){
        return sensori.aggiungiCategoriaAttuatoreAStanza(stanza,catAttuatore);
    }

    public void aggiungiCategoriaAttuatoreAArtefatto(String stanza, String artefatto, String catAttuatore){
        sensori.aggiungiCategoriaAttuatoreAArtefatto(stanza,artefatto,catAttuatore);
    }

    public Attuatore cercaAttuatoreRestituisciAttuatore(String risposta){
        return sensori.cercaAttuatoreRestituisciAttuatore(risposta);
    }

    public Sensore cercaSensoreRestituisciSensore(String risposta) {
        return sensori.cercaSensoreRestituisciSensore(risposta);
    }

    public void terminaThread(){
        regole.terminaThread();
    }

    public String getNomeUnitaImmobiliare() {
        return im.getNomeUnitaImmobiliare();
    }

    public String getTipoUnitaImmobiliare(){return im.getTipoUnitaImmobiliare();}
}
