package DipendenteDalTempo;

import Applicazione.UnitaImmobiliare.UnitaImmobiliare;
import Categorie.*;
import Contenitori.Sensore;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;


public class Regola implements Observer {
    private Antecedente condizione;
    private Sensore[] sensoreCondizione=new Sensore[2];
    private ArrayList<Conseguente> azione = new ArrayList<>();
    private String expr;
    private Orario orario;
    private int activeComponents;


    public Regola(UnitaImmobiliare immobile, String expr) throws IllegalArgumentException {
        this.expr = expr;
        String operatore = "";

        String[] anticedenti;

        controlliIndipendenti(expr);

        String antecedente = expr.substring(3, expr.indexOf("then") - 1);
        anticedenti = antecedente.split("(<=|>=|=|>|<)");

        operatore = sceltaOperatore(antecedente);

        anticedenti[0] = anticedenti[0].trim();
        anticedenti[1] = anticedenti[1].trim();

        if(anticedenti[0].equals("time")){
            primoCampoTime(operatore,anticedenti);
        }

        else if(anticedenti[1].equals("time")){
            secondoCampoTime(operatore,anticedenti);
        }

        else {
            antecedenteSenzaTime(anticedenti,operatore,immobile);
        }

        orario = new ParserConseguente().creazioneConseguente(immobile,expr,this,orario);

    }

    private void primoCampoTime(String operatore,String[] anticedenti){
        if (operatore.contains(">"))
            operatore = operatore.replace(">", "<");
        else if (operatore.contains("<"))
            operatore = operatore.replace("<", ">");
        secondoCampoTime(operatore, anticedenti);
    }

    private void secondoCampoTime(String operatore,String[] anticedenti){

        int ore = Integer.parseInt(anticedenti[1].split("[.]")[0]);
        int minuti = Integer.parseInt(anticedenti[1].split("[.]")[1]);
        condizione = new AntecedenteNumeroTime(ore,minuti,operatore);

    }

    private void antecedenteSenzaTime(String[] anticedenti,String operatore,UnitaImmobiliare immobile){
        boolean isPrimoNum = false, isSecondoNum = false;
        boolean isPrimaStr = false, isSecondaStr = false;
        int const1 = 0, const2 = 0;
        String str1 = null, str2 = null;
        Rilevazione r1 = null, r2 = null;

        if (!isNumeric(anticedenti[0])) {
            Sensore s = immobile.getController().cercaSensoreRestituisciSensore(anticedenti[0].split("[.]")[0]);
            if (s == null) {
                //Potrebbe essere un valore stringa
                if (!operatore.equals("="))
                    throw new IllegalArgumentException("Con sensori a dominio discreto serve l' '=' ");
                str1 = anticedenti[0];
                isPrimaStr = true;
            } else {
                sensoreCondizione[0] = s;
                s.addObserver(this);
                r1 = ((CategoriaSensore) s.getCategoria()).getRilevazione(anticedenti[0].split("[.]")[1]);
                if (r1 == null) {
                    throw new IllegalArgumentException("Rilevazione " + anticedenti[0].split("[.]")[1] + " non trovato");
                }
            }
        } else {
            isPrimoNum = true;
            const1 = Integer.parseInt(anticedenti[0]);
        }

        if (!isNumeric(anticedenti[1])) {
            Sensore s = immobile.getController().cercaSensoreRestituisciSensore(anticedenti[1].split("[.]")[0]);
            if (s == null) {
                if (!operatore.equals("="))
                    throw new IllegalArgumentException("Con sensori a dominio discreto serve l' '=' ");
                str2 = anticedenti[1];
                isSecondaStr = true;
            } else {
                sensoreCondizione[1] = s;
                s.addObserver(this);
                r2 = ((CategoriaSensore) s.getCategoria()).getRilevazione(anticedenti[1].split("[.]")[1]);
                if (r2 == null) {
                    throw new IllegalArgumentException("Rilevazione " + anticedenti[1].split("[.]")[1] + " non trovato");
                }
            }

        } else {
            isSecondoNum = true;
            const2 = Integer.parseInt(anticedenti[1]);
        }

        if (!operatore.equals("=")) {
            if (r1 != null && !r1.isMassimoMinimo()) {
                throw new IllegalArgumentException("Tutte le rilevazioni dovrebbero essere numeriche e non a stati, errore");
            }
            if (r2 != null && !r2.isMassimoMinimo()) {
                throw new IllegalArgumentException("Tutte le rilevazioni dovrebbero essere numeriche e non a stati, errore");
            }
        }

        if (isPrimoNum && !isSecondoNum)
            condizione = new AntecedenteNumeroSensore(const1, r2, operatore);
        else if (!isPrimoNum && isSecondoNum) {
            if (operatore.contains(">"))
                operatore = operatore.replace(">", "<");
            else if (operatore.contains("<"))
                operatore = operatore.replace("<", ">");
            condizione = new AntecedenteNumeroSensore(const2, r1, operatore);
        } else if (isPrimaStr && !isSecondaStr)
            condizione = new AntecedenteStringaSensore(str1, r2, operatore);
        else if (!isPrimaStr && isSecondaStr)
            condizione = new AntecedenteStringaSensore(str2, r1, operatore);
        else if (isPrimoNum && isSecondoNum)
            condizione = new AntecedenteNumeroNumero(const1, const2, operatore);
        else
            condizione = new AntecedenteSensoreSensore(r1, r2, operatore);
    }

    private String sceltaOperatore(String antecedente) throws IllegalArgumentException{
        String operatore = "";
        if (antecedente.contains("<="))
            operatore = "<=";
        else if (antecedente.contains(">="))
            operatore = ">=";
        else if (antecedente.contains("="))
            operatore = "=";
        else if (antecedente.contains("<"))
            operatore = "<";
        else if (antecedente.contains(">"))
            operatore = ">";
        else
            throw new IllegalArgumentException("Operatore non consentito");

        return operatore;
    }

    private void controlliIndipendenti(String expr) throws IllegalArgumentException{
        String inizio = expr.substring(0, 2);
        if (!inizio.equals("if") || !expr.contains("then")) {
            throw new IllegalArgumentException("La condizione non e` scritta correttamente");
        }
    }

    public void valuta() {
        if (antecedente())
            conseguente();
    }

    public void stampa(){
        System.out.println(expr);
    }

    private void conseguente() {
        if(activeComponents==0) {
            if (orario != null) {
                for (Conseguente conseguente : azione)
                    Orologio.registrati(conseguente, orario);
            } else {
                for (Conseguente conseguente : azione) {
                    conseguente.aziona();
                }
            }
        }
    }

    private boolean antecedente() {
        if(activeComponents==0)
            return condizione.valuta();
        else return false;
    }

    boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void addToConseguente(Conseguente c){
        azione.add(c);
    }

    public int getActiveComponents() {
        return activeComponents;
    }

    @Override
    public void update(Observable observable, Object o) {
        if((boolean)o) activeComponents++;
        else activeComponents --;
    }
}
