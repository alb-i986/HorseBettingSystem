package me.alb_i986.hbs.domain;


import java.util.List;


public interface ComponentA extends Component {
    public void incrementaAmmontareConto(float importo);
    public void decrementaAmmontareConto(float importo);
    public void assegnaVincite(List<Integer> scommesse);
    public String[] visualizzaScommesseDaPagare(int idCorsa);
}