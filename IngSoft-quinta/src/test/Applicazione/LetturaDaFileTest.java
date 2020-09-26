package Applicazione;

import Applicazione.UnitaImmobiliare.ControllerUnitaImmobiliare;
import Applicazione.UnitaImmobiliare.UnitaImmobiliare;
import Categorie.CategoriaAttuatore;
import Categorie.CategoriaDispositivo;
import Categorie.CategoriaSensore;
import Contenitori.Contenitore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class LetturaDaFileTest {

    //Le classi derivate coinvolte nel test sono SistemaDomotico,CategorieCreator.
    @Test
    void importaUnitaImmobiliariRiga() {
        SistemaDomotico sm = new SistemaDomotico();
        sm.aggiungiCategoriaSensore(new CategoriaSensore("luce"));
        sm.aggiungiCategoriaSensore(new CategoriaSensore("clima"));
        sm.aggiungiCategoriaAttuatore(new CategoriaAttuatore("tapparella"));
        UnitaImmobiliare im = new LetturaDaFile(sm,new CategorieCreator(sm)).
                importaUnitaImmobiliariRiga("" +
                "smeraldo-condominio-cucina-luce-0-tapparella-0-piedistallo-clima-0- -0");
        ControllerUnitaImmobiliare controller = im.getController();

        //Controllo che abbia creato la categoria luce,tapparella,clima nella stanza cucina
        //e nell'artefatto piedistallo in cucina
        assertFalse(controller.aggiungiCategoriaSensoreAStanza
                ("cucina","luce"));
        assertFalse(controller.aggiungiCategoriaAttuatoreAStanza
                ("cucina","tapparella"));
        assertTrue(controller.aggiungiCategoriaSensoreAArtefatto
                ("cucina","piedistallo","clima"));


        //Controllo stanze e artefatti
        assertFalse(controller.aggiungiStanzaCode("cucina"));
        assertFalse(controller.aggiungiArtefattoCode("cucina","piedistallo"));

        //Controllo nome e tipo immobile
        assertTrue(controller.getNomeUnitaImmobiliare().equals("smeraldo"));
        assertTrue(controller.getTipoUnitaImmobiliare().equals("condominio"));
    }
}