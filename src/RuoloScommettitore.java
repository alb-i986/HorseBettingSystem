package modello_di_progetto;

public abstract class RuoloScommettitore implements ComponentS {

    private ComponentS utente;
    private int id;

    public RuoloScommettitore(ComponentS u, int id) {
        utente = u;
        this.id = id;
    }

    public void incrementaAmmontareConto(float importo) {
        utente.incrementaAmmontareConto(importo);
    }

    public void decrementaAmmontareConto(float importo, StrategyDecrementaAmmontareConto strategy)  {
        utente.decrementaAmmontareConto(importo,strategy);
    }

    public String toString() {
        return utente.toString();
    }

    public StatoUtente getRuolo() {
        return utente.getRuolo();
    }

    public String getUsername() {
        return utente.getUsername();
    }

    public Component next() {
        return utente;
    }

    public int getId(){
        return id;
    }
}
