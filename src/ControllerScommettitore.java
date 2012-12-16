package modello_di_progetto;

import java.util.Date;
import java.util.List;

public class ControllerScommettitore {

    public ControllerScommettitore() {
    }

    public void inserisciDatiScommessa(ComponentS u, Scommessa scommessaAppenaCreata, TipoScommessa tipoScommessa, TipoGiocata tipoGiocata, String[] cavalli, float puntata) throws IllegalArgumentException {
        if (scommessaAppenaCreata == null) {
            throw new IllegalArgumentException("Devi prima creare una nuova scommessa");
        }
        //if(puntata > u.getId())
        Cavallo cavalliDaInserire = DBHandler.getCavallo(cavalli);
        scommessaAppenaCreata.inserisciDati(u, tipoScommessa, tipoGiocata, cavalliDaInserire, puntata);


        if (tipoScommessa.compareTo(TipoScommessa.valueOf("quotaFissa")) == 0) {
            Quotazione q = DBHandler.getQuotazione(scommessaAppenaCreata.getCorsa().getId(), cavalliDaInserire); //idCorsa è passato in nuovaScomm()
            Float quotazione = q.getQuotazioneFissa();
            scommessaAppenaCreata.setQuotazioneFissa(quotazione);
            scommessaAppenaCreata.setImportoVincita(puntata);
        }
        try {
                AgenziaIppica.getInstance().incrementaAmmontareConto(puntata);
            } catch (SingletonException e) {
                System.out.println(e);
            }
    }

    public void inserisciDatiScommessaConBonus(ComponentS u, Scommessa scommessaAppenaCreata, TipoScommessa tipoScommessa, TipoGiocata tipoGiocata, String[] cavalli, int idBonus, float importoGiocato) throws IllegalArgumentException {
        if (scommessaAppenaCreata == null) {
            throw new IllegalArgumentException("Devi prima creare una nuova scommessa!");
        }
        scommessaAppenaCreata.inserisciDati(u, tipoScommessa, tipoGiocata, DBHandler.getCavallo(cavalli), importoGiocato);
        scommessaAppenaCreata.aggiornaBonus(idBonus);
        if (tipoScommessa.compareTo(TipoScommessa.quotaFissa) == 0) {
            Quotazione q = DBHandler.getQuotazione(scommessaAppenaCreata.getCorsa().getId(), DBHandler.getCavallo(cavalli)); //idCorsa è passato in nuovaScomm()
            Float q_fissa = q.getQuotazioneFissa();
            scommessaAppenaCreata.setQuotazioneFissa(q_fissa);
            scommessaAppenaCreata.setImportoVincita(importoGiocato);
        }
    }

    //fatta nel caso di bookmaker
    public Scommessa nuovaScommessa(ComponentS u, int idCorsa, ComponentS scommettitore) {
        Scommessa s = new Scommessa(); //nuovaScommessa;

        s.addDelega(u, scommettitore);
        s.setData(new Date()); //dataCorrente
        s.setCorsa(idCorsa);

        // il riferimento a scommessa ci serve per chiamare inserisciDatiScommessa()
        return s;
    }

    /**
     * Dati un utente e un idCorsa, crea una nuova corsa e la restituisce
     * Legato a UC Effuare scommesse, scenario principale e scenario alternativo "con bonus"
     *
     */
    public Scommessa nuovaScommessa(ComponentS u, int idCorsa) {
        Scommessa s = new Scommessa(); //nuovaScommessa;

        s.setScommettitore(u);
        s.setData(new Date());
        s.setCorsa(idCorsa);

        //ci serve perchè poi quando inseriamo i dati dobbiamo conoscere la scommessa che abbiamo creato
        return s;
    }

    public String[] visualizzaCorse(String nomeIppodromo) {
        List<Corsa> listaCorse = DBHandler.getCorse(nomeIppodromo);
        if (listaCorse == null) {
            System.out.println("Non sono attualmente disponibili corse per quell'ippodromo");
            return null;
        } else {

            String[] toDisplay = new String[listaCorse.size()];

            int index = 0;

            for (Corsa c : listaCorse) {
                toDisplay[index] = "Nome = " + c.getTitolo() + " id= " + c.getId();
                index++;
            }
            return toDisplay;
        }

    }

    public String[] visualizzaCorseProgrammate(String nomeIppodromo) {
        List<Corsa> listaCorse = DBHandler.getCorseProgrammate(nomeIppodromo);
        if (listaCorse == null) {
            System.out.println("Non sono attualmente disponibili corse per quell'ippodromo");
            return null;
        } else {

            String[] toDisplay = new String[listaCorse.size()];

            int index = 0;

            for (Corsa c : listaCorse) {
                toDisplay[index] = "Nome = " + c.getTitolo() + " id= " + c.getId();
                index++;
            }
            return toDisplay;
        }

    }

    public String[] visualizzaDettagli(int idCorsa) {
        Corsa c = DBHandler.getCorsa(idCorsa);

        return c.getInfo();
    }

    public String[] visualizzaStorico(String cavallo, Date dataInizio, Date dataFine) {
        List<TipoLink_Piazzamento> p = DBHandler.getStorico(cavallo, dataInizio, dataFine);
        String[] toRet = new String[p.size()];
        int index = 0;
        for (TipoLink_Piazzamento piazz : p) {
            toRet[index] = piazz.getInfo();
            index++;
        }
        return toRet;
    }

    /**
     * Restituisce un array di stringhe, dove ognuna di esse descrive un bonus relativo
     * all'utente u, non scaduto, giocabile sulla corsa specificata da idCorsa, con tipo scommessa tipo
     *
     * @param u
     * @param idCorsa
     * @param tipo
     * @return
     */
    public String[] visualizzaBonus(ComponentS u, int idCorsa, TipoScommessa tipo) {
        List<Bonus> listaBonusUtilizzabili = DBHandler.getBonusScommettitore(u, idCorsa, tipo);
        String[] toDisplay = new String[listaBonusUtilizzabili.size()];
        int index = 0;

        for (Bonus b : listaBonusUtilizzabili) {
            toDisplay[index] = b.toString();
            index++;
        }
        return toDisplay;
    }

    public String[] visualizzaDeleghe(ComponentS u) {
        if (u instanceof Bookmaker) {
            List<Delega> listaDeleghe = DBHandler.getDeleghe(u.getId());
            String[] toDisplay = new String[listaDeleghe.size()];
            int index = 0;

            for (Delega d : listaDeleghe) {
                toDisplay[index] = d.getInfo();
                index++;
            }
            return toDisplay;
        } else {
            throw new IllegalArgumentException("devi pasare in input un bookmaker");
        }
    }
}
