package Applicazione.UnitaImmobiliare;

import View.Interazione;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

class LetturaDaFile implements Serializable {
    UnitaImmobiliare im;
    GestoreRegole regole;

    LetturaDaFile(UnitaImmobiliare im, GestoreRegole regole) {
        this.im = im;
        this.regole = regole;
    }

    void importaRegoleDaFile() {
        String filename = Interazione.domanda("Nome del file contenente le regole? : ");
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filename));
            String line = reader.readLine();
            while (line != null) {
                try{
                    regole.inserisciRegolaCode(line);
                }catch(IllegalArgumentException e){
                    System.out.println("La riga "+line+" ha generato il seguente errore");
                    System.out.println(e.getMessage());
                }
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Il file non esiste, controlla il nome inserito");
        }
    }
}
