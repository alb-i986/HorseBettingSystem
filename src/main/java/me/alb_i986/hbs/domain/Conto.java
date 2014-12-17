package me.alb_i986.hbs.domain;

public class Conto {

    private int id;
    private StatoConto stato;
    private float ammontare;
    private float ammontareTot;

    public Conto(int id, StatoConto stato, float ammontare, float ammontareTot) {
        this.id = id;
        this.stato = stato;
        this.ammontare = ammontare;
        this.ammontareTot = ammontareTot;
    }

    public float getAmmontare() {
        return ammontare;
    }
   

    public Conto(String[] elem) {
        this(Integer.parseInt(elem[0]), StatoConto.valueOf(elem[1]), Float.parseFloat(elem[2]), Float.parseFloat(elem[3]));
    }

    public void incAmmontare(float importo) {
        if (importo < 0) {
            throw new IllegalArgumentException("L'importo con cui incrementare il conto deve essere non negativo.");
        }
        this.ammontare += importo;
    }

    public void incAmmontareTot(float importo) {
        if (importo < 0) {
            throw new IllegalArgumentException("L'importo con cui incrementare il conto deve essere non negativo.");
        }
        this.ammontareTot += importo;
    }

    public void decAmmontare(float importo) {
        if (importo < 0) {
            throw new IllegalArgumentException("L'importo con cui decrementare il conto deve essere non negativo.");
        }
        this.ammontare -= importo;
    }

    public void decAmmontareTot(float importo) {
        if (importo < 0) {
            throw new IllegalArgumentException("L'importo con cui decrementare il conto deve essere non negativo.");
        }
        this.ammontareTot -= importo;
    }

   
    public String toString() {
        String toRet = "";
		toRet += "Stato="+stato;
		toRet += "Ammontare="+ammontare;
		toRet += "AmmontareTot="+ammontareTot;
        return toRet;
    }
}
