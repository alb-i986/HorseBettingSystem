package me.alb_i986.hbs.domain;

import me.alb_i986.hbs.db.DBHandler;

import java.util.List;

public class AgenziaIppica extends RuoloAgenzia {

    private float percentuale;
    private static AgenziaIppica instance = null;

    /**
     * Pseudo-costruttore usato per ottenere l'unica istanza sapendo che è già stata istanziata,
     * ma NON la crea
     * Precondizione: è già stato invocato almeno una volta getInstance(ComponentA,float)
     *                o getInstance(float)
     *                ovvero è stata già creata l'unica istanza
     *
     * @throws SingletonException se la precondizione è violata
     */
    public static AgenziaIppica getInstance() throws SingletonException {
        if (instance == null) {
            throw new SingletonException("Oggetto singleton non esistente. Invoca prima getInstance(ComponentA,float) o getInstance(float).");
        }
        return instance;
    }

    /**
     * Pseudo-costruttore usato per creare l'istanza di AgenziaIppica in quanto Controller,
     * non in quanto utente loggato (NO Decorator)
     * => il riferimento al ComponentA successivo viene inizializzato a null
     *
     * NB: Da usare solo nel main, quando si creano i Controller, e ogni volta che
     * un operatore dell'agenzia ippica fa logout
     * NON DEVE essere usato mentre un operatore dell'agenzia ippica è loggato nel sistema
     */
    public static AgenziaIppica getInstance(float percentuale) {
        // crea una nuova istanza se la classe non è ancora mai stata istanziata,
        // oppure la ricrea se è stata già istanziata in qualità di utente loggato nel sistema
        // (i.e. con il campo utente valorizzato)
        if (instance == null || instance.next() != null)
            instance = new AgenziaIppica(percentuale);

        return instance;
    }

    /**
     * Pseudo-costruttore usato per creare e/o ottenere l'istanza di AgenziaIppica
     * non solo in quanto Controller, ma anche in quanto utente loggato
     *
     * NB: Da usare SOLO nella procedura di login, quando un operatore dell'agenzia
     * ippica si logga nel sistema, e solo dopo che è stato creato l'oggetto Utente
     *
     * Precondizione: u deve essere non nullo
     */
    public static AgenziaIppica getInstance(ComponentA u, float percentuale) {
        if (u == null) {
            throw new IllegalArgumentException("Costruttore per creare un'AgenziaIpppica che ha appena fatto login.");
        }
        // crea una nuova istanza se la classe non è ancora mai stata istanziata,
        // oppure la ricrea se è stata già istanziata, ma con il campo utente a null (AgenziaIppica Controller)
        if (instance == null || instance.next() == null) {
            instance = new AgenziaIppica(u, percentuale);
        }

        return instance;
    }

    private AgenziaIppica(float percentuale) {
        super(null);
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

            /* assgnamento vincite relative a scommesse su bookmaker non implementato perche' relativo a flusso alt UC Assegnare vincite */
            if (scom.getScommettitore() != null) {
                //per ogni scommessa (vincente) andiamo a fare le op necessarie a pagare la vincita
                //effettuaiamo un ulteriore controllo se è daPagare??
                if (!scom.isVincente()) {
                    throw new IllegalArgumentException("scommessa non vincente! ");
                }
                if (scom.isPagata()) {
                    throw new IllegalArgumentException("scommessa  gia' pagata");
                }
                scom.incrementaAmmontareConto();
                decrementaAmmontareConto(scom.getImportoVincita());
                scom.setPagata(true);
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

    public void decrementaAmmontareConto(float importo) {
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
        return super.toString() + "\nPercentuale agenzia: " + percentuale;
    }
}
