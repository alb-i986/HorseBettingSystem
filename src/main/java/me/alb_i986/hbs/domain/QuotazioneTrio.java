package me.alb_i986.hbs.domain;

import me.alb_i986.hbs.db.DBHandler;

public class QuotazioneTrio extends Quotazione {

    public QuotazioneTrio() {
    }

    public QuotazioneTrio(float qfissa, float qtot, Cavallo triplaCavalli, Corsa corsa) {

        this.quotazioneFissa = qfissa;
        this.quotazioneTotalizzatore = qtot;
        if(triplaCavalli instanceof TriplaCavalli){
            this.cavallo = triplaCavalli;
        }
        else{
            throw new IllegalArgumentException("hai inserito il tipo di cavallo sbagliato");
        }
        this.corsa = corsa;
    }
    public QuotazioneTrio(String[] dati) {
        //dati[0] è il tipo;
        this(Float.parseFloat(dati[1]),Float.parseFloat(dati[2]), DBHandler.getCavallo(Integer.parseInt(dati[3])),DBHandler.getCorsa(Integer.parseInt(dati[4])));
    }

    public void setTriplaCavalli(TriplaCavalli triplaCavalli) {
        this.cavallo = triplaCavalli;
    }

    public TriplaCavalli getTriplaCavalli() {
        return (TriplaCavalli) this.cavallo;
    }

    public void setCorsa(Corsa corsa) {
        this.corsa = corsa;
    }

    public Corsa getCorsa() {
        return this.corsa;
    }
}
