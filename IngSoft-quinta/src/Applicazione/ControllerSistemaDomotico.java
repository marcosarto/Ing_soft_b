package Applicazione;

import Applicazione.UnitaImmobiliare.UnitaImmobiliare;
import Contenitori.Stanza;

import java.io.Serializable;
import java.util.Map;

public class ControllerSistemaDomotico implements Serializable {
    private static ControllerSistemaDomotico instance = Salvataggio.ripristina();
    private static Salvataggio salvataggio = new Salvataggio();

    SistemaDomotico sm;
    LetturaDaFile lf;
    GestoreFlusso gf;
    CategorieCreator creator;

    private ControllerSistemaDomotico(){}

    public static ControllerSistemaDomotico getInstance(){
        if(instance == null) {
            instance = new ControllerSistemaDomotico();
        }
        return instance;
    }

    public void terminaThreads(){
        TerminazioneSistema.terminaThreads(sm.getUnitaImmobiliari());
    }

    public void salva(){
        terminaThreads();
        salvataggio.salva(this);
    }

    public void iniziaFlusso(){
        if(sm == null) sm = new SistemaDomotico();
        if(creator == null) creator = new CategorieCreator(sm);
        if(lf == null) lf = new LetturaDaFile(sm,creator);
        if(gf == null) gf = new GestoreFlusso(creator,sm,lf);
        gf.run();
    }

    public UnitaImmobiliare importaUnitaImmobiliariRiga(String riga){
        return lf.importaUnitaImmobiliariRiga(riga);
    }

}
