package me.alb_i986.hbs.domain;


/**
 * Identifica tutte quelle classe che partecipano a un decorator
 */
public interface Component {

    /**
     * Restituisce il ruolo più specifico dell'oggetto decorato
     * @return una stringa che qualifica il ruolo
     */
    public StatoUtente getRuolo();
    
    public String getUsername();

    /**
     *
     * @return il ComponentS successivo nella catena
     */
    public Component next();

    public String toString();
}
