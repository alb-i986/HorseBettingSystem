package modello_di_progetto;

import java.sql.SQLException;

public abstract class Quotazione {

    protected float quotazioneFissa;
    protected float quotazioneTotalizzatore;
    protected Cavallo cavallo;
    protected Corsa corsa;

    
    public float getQuotazioneFissa() {
        return this.quotazioneFissa;
    }

    public String toString() {
        String toRet = "";
        toRet += "quotazioneFissa = " + this.quotazioneFissa + "\n";
        toRet += "quotazioneTotalizzatore = " + this.quotazioneTotalizzatore + "\n";
        toRet += "cavalloid = " + this.cavallo.getIdCavallo() + "\n";
        toRet += "corsaid = " + this.corsa.getId() + "\n";

        return toRet;
    }
}
