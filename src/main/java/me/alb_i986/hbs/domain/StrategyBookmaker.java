package me.alb_i986.hbs.domain;

public class StrategyBookmaker implements StrategyDecrementaAmmontareConto {

    public StrategyBookmaker() {
    }

    public void decrementaAmmontareConto(float importo, Conto c)  {
        c.decAmmontareTot(importo);
    }
}