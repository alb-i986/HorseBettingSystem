package modello_di_progetto;



public class QuotazioneAccoppiata extends Quotazione {

    public QuotazioneAccoppiata(float qfissa, float qtot, Cavallo coppiaCavalli, Corsa Corsa) {
        this.quotazioneFissa = qfissa;
        this.quotazioneTotalizzatore = qtot;
        if(coppiaCavalli instanceof CoppiaCavalli){
            this.cavallo = coppiaCavalli;
        }
        else{
            throw new IllegalArgumentException("hai inserito il tipo di cavallo sbagliato");
        }
        
        this.corsa = Corsa;

    }

    public QuotazioneAccoppiata(String[] dati){
        //dati[0] è il tipo;
        this(Float.parseFloat(dati[1]),Float.parseFloat(dati[2]),DBHandler.getCavallo(Integer.parseInt(dati[3])),DBHandler.getCorsa(Integer.parseInt(dati[4])));
    }

    public void setCoppiaCavalli(CoppiaCavalli coppiaCavalli) {
        this.cavallo = coppiaCavalli;
    }

    public CoppiaCavalli getCoppiaCavalli() {
        return (CoppiaCavalli) this.cavallo;
    }

    public void setCorsa(Corsa Corsa) {
        this.corsa = Corsa;
    }

    public Corsa getCorsa() {
        return this.corsa;
    }

}
