package modello_di_progetto;

import java.util.List;

public class AgenziaIppica extends RuoloAgenzia {

    private float percentuale;
    private static AgenziaIppica instance = null;


    public static AgenziaIppica getInstance() throws SingletonException {
        if (instance == null) {
            throw new SingletonException("Oggetto singleton non esistente. Invoca prima getInstance(ComponentA,float).");
        }
        return instance;
    }

    public static AgenziaIppica getInstance(ComponentA u, float percentuale) {
        if (instance == null)
            instance = new AgenziaIppica(u, percentuale);
        else if(instance.next()==null)
				instance = new AgenziaIppica(u, percentuale);

        return instance;
    }

    public static AgenziaIppica getInstance(float percentuale) {

        if (instance == null) {
            instance = new AgenziaIppica(u, percentuale);
        }
        else
        		if(instance.next()==null)

        return instance;
    }

    private AgenziaIppica(ComponentA u, float percentuale) {
        super(u);
        // controllo che le regole di composizione del Decorator siano rispettate
        if (!(u instanceof Utente)) {
            throw new DecoratorException("Composizione non valida: ad AgenziaIppica puo' essere aggiunto solo Utente.");
        }
        this.percentuale = percentuale;
    }

    private AgenziaIppica(ComponentA u, float percentuale) {
        super(u);
        // controllo che le regole di composizione del Decorator siano rispettate
        if (!(u instanceof Utente)) {
            throw new DecoratorException("Composizione non valida: ad AgenziaIppica puo' essere aggiunto solo Utente.");
        }
        this.percentuale = percentuale;
    }

    public void assegnaVincite(List<Integer> scommesse) {
        //iteriamo su tutte le scommesse passate in input
        for (Integer id : scommesse) {
            Scommessa scom = DBHandler.getScommessa(id);

            /* assegnamento vincite relative a scommesse su bookmaker non implementato perche' relativo a flusso alt UC Assegnare vincite */
            if(scom.getScommettitore()!=null) {
                //per ogni scommessa (vincente) andiamo a fare le op necessarie a pagare la vincita
                //effettuaiamo un ulteriore controllo se è daPagare??
                if (!scom.isVincente()) {
                    throw new IllegalArgumentException("scommessa non vincente! ");
                }
                if (scom.isPagata()) {
                    throw new IllegalArgumentException("scommessa  gia' pagata");
                }
                scom.incrementaAmmontareConto();
                scom.setPagata(true);
                float importoVincita = scom.getImportoVincita();
                decrementaAmmontareConto(importoVincita);
            }
        }
    }


    public String[] visualizzaScommesseDaPagare(int idCorsa) {

        Corsa c = DBHandler.getCorsa(idCorsa);
        /*
        if(c.getStato()!=StatoCorsa.terminata)
            throw new IllegalArgumentException("corsa non terminata");
        */
            List<Scommessa> scommmesseDaPagare = c.getScommesseDaPagare();

            String[] toDisplay = new String[scommmesseDaPagare.size()];
            int index = 0;

            for (Scommessa s : scommmesseDaPagare) {
                toDisplay[index] = s.getInfo();
                index++;

            }
            //System.out.println(toDisplay);

            return toDisplay;
    }

    public void incrementaAmmontareConto(float importo) {
        super.incrementaAmmontareConto(importo);
    }

    public void decrementaAmmontareConto(float importo){
        super.decrementaAmmontareConto(importo);
    }

    public StatoUtente getRuolo() {
        return StatoUtente.valueOf("agenzia_ippica");
    }

    public String getUsername() {
        return super.getUsername();
    }


    public Component next() {
        return super.next();
    }

    public String toString() {
        return super.toString()+"\nPercentuale agenzia: "+percentuale;
    }
}
