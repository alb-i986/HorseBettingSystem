package me.alb_i986.hbs.domain;



public class Bookmaker extends RuoloScommettitore {

    private float percentuale;
    //ComponentS scommettitoreDelegante;


    public Bookmaker(ComponentS u, int idBook, float percentuale) {
        super(u,idBook);
        // controllo che le regole di composizione del Decorator siano rispettate
        if (!(u instanceof Scommettitore)) {
            throw new DecoratorException("Composizione non valida: a Bookmaker puo' essere aggiunto solo Scommettitore.");
        }
        this.percentuale = percentuale;
    }

    public void incrementaAmmontareConto(float importo) {
        super.incrementaAmmontareConto(importo);
    }


    /**
     * Incrementa l'ammontare del conto del bookmaker e di quello dello scommettitore delegante
     * @param importo l'importo della vincita della scommessa, da ripartire tra bookmaker e scommettitore delegante
     */
 /*   public void incrementaAmmontareConto(float importo) {
        float guadagnoBookmaker = percentuale*importo;
        float guadagnoScommettitoreDelegante = importo - guadagnoBookmaker;

        super.incrementaAmmontareConto(guadagnoBookmaker);
        scommettitoreDelegante.incrementaAmmontareConto(guadagnoScommettitoreDelegante);
    }
*/

    @Override
    public void decrementaAmmontareConto(float importo, StrategyDecrementaAmmontareConto strategy) {
        super.decrementaAmmontareConto(importo,strategy);
    }

    @Override
    public StatoUtente getRuolo() {
        return StatoUtente.valueOf("bookmaker");
    }

    @Override
    public String getUsername() {
        return super.getUsername();
    }

    @Override
    public Component next() {
        return super.next();
    }
}
