package modello_di_progetto;

public class StrategyScommettitore implements StrategyDecrementaAmmontareConto {

public StrategyScommettitore() {
    }

    public void decrementaAmmontareConto(float importo, Conto c) {
        c.decAmmontare(importo);
        c.decAmmontareTot(importo);
    }
}
