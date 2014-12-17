package me.alb_i986.hbs.domain;

import me.alb_i986.hbs.db.DBHandler;

public class Delega {

    private float ammontareMax;
    private float ammontareResiduo;
    private StatoDelega stato;
    private int idBookmaker;
    private int idScommettitore;

    public Delega(String[] elem) {
        this(Float.parseFloat(elem[0]), Float.parseFloat(elem[1]), StatoDelega.valueOf(elem[2]), Integer.parseInt(elem[3]), Integer.parseInt(elem[4]));

    }

    public Delega(float ammontareMax, float ammontareResiduo, StatoDelega stato, int idBookmaker, int idScommettitore) {
        this.ammontareMax = ammontareMax;
        this.ammontareResiduo = ammontareResiduo;
        this.stato = stato;
        this.idBookmaker = idBookmaker;
        this.idScommettitore = idScommettitore;
    }

  

    public ComponentS getScommettitore() {
        return DBHandler.getScommettitore(idScommettitore);
    }

    public void decAmmontareResiduo(float puntata) {

        if (this.ammontareResiduo - puntata < 0) {
            throw new IllegalArgumentException("ammontare residuo delega non sufficiente: inserire una puntata inferiore.");
        }
        this.ammontareResiduo -= puntata;
        
    }

    public String getInfo() {
        String toRet = "";

        toRet = "[ ";
        toRet += "idBookmaker= " + this.idBookmaker + ", ";
        toRet += "idScommettitore=" + this.idScommettitore;
        toRet += " ]";

        return toRet;
    }
    
}
