package modello_di_progetto;

public interface ComponentS extends Component {

    public void incrementaAmmontareConto(float importo);
    public void decrementaAmmontareConto(float importo, StrategyDecrementaAmmontareConto strategy);
    public int getId();
}