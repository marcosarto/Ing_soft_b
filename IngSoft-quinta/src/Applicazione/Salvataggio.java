package Applicazione;

import java.io.*;

public class Salvataggio implements Serializable{
    static String filename = "file.ser";

    public static ControllerSistemaDomotico ripristina(){
        ControllerSistemaDomotico controller;
        try
        {
            // Reading the object from a file
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);

            // Method for deserialization of object
            controller = (ControllerSistemaDomotico) in.readObject();

            in.close();
            file.close();
        }

        catch(IOException ex)
        {
            controller = null;
        }

        catch(ClassNotFoundException ex)
        {
            controller = null;
        }

        return controller;
    }

    public void salva(ControllerSistemaDomotico controller){
        try
        {
            //Salvo in un file
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            // Metodo per la serializzazione
            out.writeObject(controller);

            out.close();
            file.close();
        }

        catch(IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
