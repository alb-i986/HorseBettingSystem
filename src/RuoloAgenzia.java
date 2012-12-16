package modello_di_progetto;

import java.util.List;

public abstract class RuoloAgenzia implements ComponentA {

    private ComponentA utente;

    public RuoloAgenzia(ComponentA u) {
        utente = u;
    }

    
    public void assegnaVincite(List<Integer> scommesse)  {
        utente.assegnaVincite(scommesse);
    }

    public String[] visualizzaScommesseDaPagare(int idCorsa)  {
        return utente.visualizzaScommesseDaPagare(idCorsa);
    }

    public void incrementaAmmontareConto(float importo) {
        utente.incrementaAmmontareConto(importo);
    }

    public void decrementaAmmontareConto(float importo){
        utente.decrementaAmmontareConto(importo);
    }

    public StatoUtente getRuolo() {
        return utente.getRuolo();
    }

    public String getUsername() {
        return utente.getUsername();
    }

    public String toString() {
        return utente.toString();
    }

    public Component next() {
        return utente;
    }
}
