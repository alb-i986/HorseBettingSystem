package modello_di_progetto;

import java.util.List;

public class Utente implements ComponentA, ComponentS {

    private Conto conto;
    private StatoUtente stato;
    private String username;
    private String password;

    public Utente(Conto c, String us, String pwd) {
        conto = c;
        username = us;
        password = pwd;
    }

    public void assegnaVincite(List<Integer> scommesse) {
        throw new UnsupportedOperationException();
    }

    public String[] visualizzaScommesseDaPagare(int idCorsa)  {
        throw new UnsupportedOperationException();
    }

    public void incrementaAmmontareConto(float importo) {
        if (importo < 0) {
            throw new IllegalArgumentException("L'importo con cui incrementare il conto deve essere non negativo.");
        }
        conto.incAmmontare(importo);
        conto.incAmmontareTot(importo);
    }

    public void decrementaAmmontareConto(float importo, StrategyDecrementaAmmontareConto strategy) {
        if (importo < 0) {
            throw new IllegalArgumentException("L'importo con cui decrementare il conto deve essere non negativo.");
        }
        if (importo > conto.getAmmontare()) {
            throw new IllegalArgumentException("Il saldo del conto non è sufficiente");
        }
        /* STRATEGY */
        strategy.decrementaAmmontareConto(importo, conto);
    }

    public void decrementaAmmontareConto(float importo) { //serve per il conto dell'agenzia Ippica
        if (importo < 0) {
            throw new IllegalArgumentException("L'importo con cui decrementare il conto deve essere non negativo.");
        }
        if (importo > conto.getAmmontare()) {
            throw new IllegalArgumentException("Il saldo del conto non è sufficiente");
        }

        conto.decAmmontare(importo);
        conto.decAmmontareTot(importo);
    }

    public String toString() {
        return "";
    }

    public StatoUtente getRuolo() {
        return null;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return -1;
    }

    public Component next() {
        return null;
    }
}

