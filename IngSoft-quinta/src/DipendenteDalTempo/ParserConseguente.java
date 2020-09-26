package DipendenteDalTempo;

import Applicazione.UnitaImmobiliare.UnitaImmobiliare;
import Categorie.CategoriaAttuatore;
import Categorie.ModalitaOperativa;
import Categorie.Parametro;
import Contenitori.Attuatore;

public class ParserConseguente {

    Orario creazioneConseguente(UnitaImmobiliare immobile,String expr,Regola r, Orario orario) {
        ModalitaOperativa modOp = null;
        Parametro parametro = null;
        int const3;
        String str3 = null;
        String[] conseguenti;

        String conseguenteMultiplo = expr.substring(expr.indexOf("then") + 5);
        for(String conseguente : conseguenteMultiplo.split(",")) {
            conseguenti = conseguente.split(":=");
            conseguenti[0] = conseguenti[0].trim();
            conseguenti[1] = conseguenti[1].trim();

            if (conseguenti[0].equals("start")) {
                int ora = Integer.parseInt(conseguenti[1].split("[.]")[0]);
                int minuto = Integer.parseInt(conseguenti[1].split("[.]")[1]);
                orario = new Orario(ora, minuto);
            } else {

                Attuatore a = immobile.getController().cercaAttuatoreRestituisciAttuatore(conseguenti[0].split("[.]")[0]);
                if (a == null) {
                    throw new IllegalArgumentException("Attuatore con nome " + conseguenti[0].split("[.]")[0] + " non trovato");
                }
                a.addObserver(r);
                if (((CategoriaAttuatore) a.getCategoria()).getModalitaOperativa().containsKey(conseguenti[0].split("[.]")[1]))
                    modOp = ((CategoriaAttuatore) a.getCategoria()).getModalitaOperativa().get(conseguenti[0].split("[.]")[1]);
                else
                    throw new IllegalArgumentException("Modalita operativa con nome " + conseguenti[0].split("[.]")[1] + " non trovata");

                if (!modOp.isAStati()) {
                    if (modOp.getParametri().containsKey(conseguenti[0].split("[.]")[2]))
                        parametro = modOp.getParametri().get(conseguenti[0].split("[.]")[2]);
                    else
                        throw new IllegalArgumentException("Parametro " + conseguenti[0].split("[.]")[2] + " non esistente");
                }

                ModalitaOperativa datiAttuatore = a.getDatiAttuali().get(conseguenti[0].split("[.]")[1]);

                if (modOp.isAStati()) {
                    str3 = conseguenti[1];
                    if (!modOp.getStati().contains(str3))
                        throw new IllegalArgumentException("La modalita` operativa non contiene lo stato " + str3);
                    r.addToConseguente(new Conseguente(datiAttuatore, str3, a, modOp.getNome()));
                } else {
                    if (r.isNumeric(conseguenti[1]))
                        const3 = Integer.parseInt(conseguenti[1]);
                    else
                        throw new IllegalArgumentException("La modalita` operativa accetta solo numeri");
                    r.addToConseguente(new Conseguente(datiAttuatore, parametro.getNome(), const3, a, modOp.getNome()));
                }
            }
        }
        return orario;
    }
}
