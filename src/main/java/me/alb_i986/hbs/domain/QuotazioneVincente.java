package me.alb_i986.hbs.domain;

import me.alb_i986.hbs.db.DBHandler;

public class QuotazioneVincente extends Quotazione {

//gli devo passare un cavallo o singoloCavallo ... vale anche per le altre quotazioni
    public QuotazioneVincente(float qfissa, float qtot, Cavallo cavallo, Corsa Corsa) {
        this.quotazioneFissa = qfissa;
        this.quotazioneTotalizzatore = qtot;
        if(cavallo instanceof SingoloCavallo){
            this.cavallo = cavallo;
        }
        else{
            throw new IllegalArgumentException("hai inserito il tipo di cavallo sbagliato");
        }
        this.corsa = Corsa;
    }
    public QuotazioneVincente(String[] dati){
        //dati[0] è il tipo;
        this(Float.parseFloat(dati[1]),Float.parseFloat(dati[2]), DBHandler.getCavallo(Integer.parseInt(dati[3])),DBHandler.getCorsa(Integer.parseInt(dati[4])));
    }




    public void setCavallo(SingoloCavallo cavallo) {
        this.cavallo = cavallo;
    }

    public SingoloCavallo getCavallo() {
        return (SingoloCavallo) this.cavallo;
    }

    public void setCorsa(Corsa Corsa) {
        this.corsa = Corsa;
    }

    public Corsa getCorsa() {
        return this.corsa;
    }
}
