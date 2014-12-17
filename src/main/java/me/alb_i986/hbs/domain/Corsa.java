package me.alb_i986.hbs.domain;

import me.alb_i986.hbs.db.DBHandler;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Corsa {

    private int id;
    private StatoCorsa stato;
    private Date dataOra;
    private int numCavalli;
    private String nomeIppodromo;
    //List<modello_di_progetto.Scommessa> listaScommesse = new LinkedList<Scommessa>();
    private List<TipoLink_Piazzamento> piazzamenti;
    private String titolo;
    private float montepremi;
    private TipoCorsa tipo;

    public Corsa(String[] elem) {

        this.id = Integer.parseInt(elem[0].trim());
        this.stato = StatoCorsa.valueOf(elem[1]); //String to enum
        this.dataOra = java.sql.Date.valueOf(elem[2]); //è solo data NB
        this.numCavalli = Integer.parseInt(elem[3]);
        this.nomeIppodromo = elem[4];
        this.piazzamenti = new LinkedList<TipoLink_Piazzamento>(); //quando creo la corsa non c'è ancora la classifica? che vado a inserire al termine della corsa
    }

    public List<Scommessa> getScommesseDaPagare() {
        List<Scommessa> toRet = new LinkedList();
        Scommessa elem = null;

        List<Scommessa> scom = DBHandler.getScommesse(id);

        Iterator it = scom.iterator();
        //LOOP iteriamo su tutte le scommesse associate alla corsa

        while (it.hasNext()) {

            elem = (Scommessa) it.next();
            //restituiamo tt qll che sono vincenti e non ancora pagate
            if (elem.isVincente() && !elem.isPagata()) {
                toRet.add(elem);
            }
        }
        return toRet;
    }

    public void addScommessa(Scommessa s) {
        //listaScommesse.add(s);
        //l'abbiamo levata di mezzo, farla nel DB diretto???
    }

    public String toString() {
        String toRet = "";
        toRet += "ID = " + this.id + "\n";
        toRet += "Stato = " + this.stato + "\n";
        toRet += "DataOra = " + this.dataOra.toString() + "\n";
        toRet += "Nome Ippodromo = " + this.nomeIppodromo + "\n";
        return toRet;

    }

    /**
     * Restituisce un array di stringhe, dove ogni stringa contiene il nome
     * di un cavallo partecipante alla corsa
     */
    public String[] getInfo() {

        //per ogni partecipante alla corsa NomeCavallo - quotazione vincente/accoppiata
        List<SingoloCavallo> partecipanti = DBHandler.getPartecipanti(this.id);
        int dim = partecipanti.size();
        String[] toRet = new String[dim];

        for (int i = 0; i < dim; i++) {

            toRet[i] = partecipanti.get(i).getNome();

        }

        return toRet;
    }

    public int getId() {
        return this.id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void addPiazzamento(TipoLink_Piazzamento piazzamento) {
        piazzamenti.add(piazzamento);
    }
}
