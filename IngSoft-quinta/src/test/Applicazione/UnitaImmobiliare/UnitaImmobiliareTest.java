package Applicazione.UnitaImmobiliare;

import Applicazione.SistemaDomotico;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitaImmobiliareTest {

    @Test
    void aggiungiStanzaCode() {
        UnitaImmobiliare im = new UnitaImmobiliare("casa", "appartamento",
                new ControllerUnitaImmobiliare(new SistemaDomotico()));
        //Controllo sia che funzioni inserendo un nome molto lungo sia
        //che la stanza prima non esista e dopo venga riconosciuta
        assertTrue(im.aggiungiStanzaCode("ProvaProvaProvaProvaProvaProva" +
                "ProvaProvaProvaProvaProvaProvaProva"));
        //Ora ci dovrebbe gia' essere
        assertFalse(im.aggiungiStanzaCode("ProvaProvaProvaProvaProva" +
                "ProvaProvaProvaProvaProvaProvaProvaProva"));
        //Testo ora gli artefatti
        assertTrue(im.aggiungiArtefattoCode("ProvaProvaProvaProva" +
                        "ProvaProvaProvaProvaProvaProvaProvaProvaProva",
                "♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫"));
        //Ora ci dovrebbe gia' essere
        assertFalse(im.aggiungiArtefattoCode("ProvaProvaProvaProva" +
                        "ProvaProvaProvaProvaProvaProvaProvaProvaProva",
                "♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫♫"));
    }
}