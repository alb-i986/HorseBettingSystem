package me.alb_i986.hbs.domain;

public class ScommettitoreVIP extends RuoloScommettitore {

    public ScommettitoreVIP(ComponentS u, int idScom) {
        super(u, idScom);
        // controllo che le regole di composizione del Decorator siano rispettate
        if (!(u instanceof Scommettitore)) {
            throw new DecoratorException("Composizione non valida: a ScommettitoreVIP puo' essere aggiunto solo Scommettitore.");
        }
    }

    public void incrementaAmmontareConto(float importo)  {
        super.incrementaAmmontareConto(importo);
    }

    public void decrementaAmmontareConto(float importo, StrategyDecrementaAmmontareConto strategy)  {
        super.decrementaAmmontareConto(importo, strategy);
    }

    public StatoUtente getRuolo() {
        return StatoUtente.valueOf("scommettitore_vip");
    }
    public String getUsername() {
        return super.getUsername();
    }

    public Component next() {
        return super.next();
    }

    public String toString() {
        return "";
    }
}
