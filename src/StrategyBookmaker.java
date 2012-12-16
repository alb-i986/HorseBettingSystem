package modello_di_progetto;

public class StrategyBookmaker implements StrategyDecrementaAmmontareConto {

    public StrategyBookmaker() {
    }

    public void decrementaAmmontareConto(float importo, Conto c)  {
        c.decAmmontareTot(importo);
    }
}