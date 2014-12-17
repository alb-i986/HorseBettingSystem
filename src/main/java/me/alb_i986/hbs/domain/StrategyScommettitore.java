package me.alb_i986.hbs.domain;

public class StrategyScommettitore implements StrategyDecrementaAmmontareConto {

public StrategyScommettitore() {
    }

    public void decrementaAmmontareConto(float importo, Conto c) {
        c.decAmmontare(importo);
        c.decAmmontareTot(importo);
    }
}
