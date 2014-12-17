package me.alb_i986.hbs.domain;

public interface ComponentS extends Component {

    public void incrementaAmmontareConto(float importo);
    public void decrementaAmmontareConto(float importo, StrategyDecrementaAmmontareConto strategy);
    public int getId();
}