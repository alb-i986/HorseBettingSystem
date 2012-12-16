package modello_di_progetto;

import java.sql.SQLException;
import java.util.Date;

public class Scommessa {

    private int id;
    private float puntata;
    private TipoScommessa tipo;
    private float quotazioneFissa;
    private float importoVincita;
    private boolean vincente;
    private boolean pagata;
    private TipoGiocata tipoGiocata;
    private Date data;
    private ComponentS scommettitore; //in caso di scommessa tramite bookmaker non è valorizzato
    private Corsa corsa;
    private Cavallo cavallo;
    private Delega delega; ////in caso di scommessa semplice non è valorizzato

    //creata di default con vincete e pagata = false
    //creaScommessa() effettivamente è il costruttore
    public Scommessa() {
        this.vincente = false;
        this.pagata = false;
    }

    /**
     * Costruttore che deve essere usato solo per creare una scommessa quando la si estrae dal DB (???)
     */
    public Scommessa(int id, float puntata, TipoScommessa tipo, float quotazioneFissa, float importoVincita, boolean vincente, boolean pagata, TipoGiocata tipoGiocata, Date data, ComponentS owner, Corsa corsa, Cavallo cavallo, Delega delega) {
        this.id = id;
        this.puntata = puntata;
        this.tipo = tipo;
        this.quotazioneFissa = quotazioneFissa;
        this.importoVincita = importoVincita;
        this.vincente = vincente;
        this.pagata = pagata;
        this.tipoGiocata = tipoGiocata;
        this.data = data;
        this.scommettitore = owner; //opzionale
        this.corsa = corsa;
        this.cavallo = cavallo;
        this.delega = delega; //opzionale
    }

    public Scommessa(String[] elem) {
        //elem[9] è lo scommettitore
        //elem[12-13] sono gli id della delega,
        //solo uno dei due sarà valorizzato quindi andiamo a controllarli;

        this.id = Integer.parseInt(elem[0]);
        this.puntata = Float.parseFloat(elem[1]);
        this.tipo = TipoScommessa.valueOf(elem[2]);
        this.quotazioneFissa = Float.parseFloat(elem[3]);
        this.importoVincita = Float.parseFloat(elem[4]);
        this.vincente = Boolean.parseBoolean(elem[5]);
        this.pagata = Boolean.parseBoolean(elem[6]);
        this.tipoGiocata = TipoGiocata.valueOf(elem[7]);
        this.data = java.sql.Date.valueOf(elem[8]);
        //this.listaBonus = listaBonus;
        if (elem[9].equals("null")) {
            this.scommettitore = null;
        } else {
           
            this.scommettitore = DBHandler.getScommettitore(Integer.parseInt(elem[9])); //opzionale
        }

        this.corsa = DBHandler.getCorsa(Integer.parseInt(elem[10]));
        this.cavallo = DBHandler.getCavallo(Integer.parseInt(elem[11]));
        if (elem[12].equals("null") || elem[13].equals("null")) {
            this.delega = null;
        } else {
            this.delega = DBHandler.getDelega(Integer.parseInt(elem[12]), Integer.parseInt(elem[13]));
        }
    }

    public boolean isVincente() {

        return this.vincente;
    }

    public float getImportoVincita() {
        return this.importoVincita;
    }

  
    public void inserisciDati(ComponentS u, TipoScommessa tipoScommessa, TipoGiocata tipoGiocata, Cavallo cavalli, float puntata) throws IllegalArgumentException {
        if (puntata < 2) {
            throw new IllegalArgumentException("Puntata inferiore alla puntata minima (2 euro).");
        }
        if (tipoScommessa.compareTo(TipoScommessa.quotaFissa) == 0 && importoVincita > 10000) {
            throw new IllegalArgumentException("Scommessa a quota fissa: la vincita non puo' essere superiore a 10000 euro.");
        }

        this.tipo = tipoScommessa;
        this.tipoGiocata = tipoGiocata;
        this.cavallo = cavalli;
        this.puntata = puntata;

        //controlliamo che per il tipoGiocata selezionata abbiamo scelto il numero di cavalli giusto
        if(TipoGiocata.piazzato.compareTo(tipoGiocata)==0||TipoGiocata.vincente.compareTo(tipoGiocata)==0){
            if(this.cavallo.getNumSingoliCavalli()!=1){
                throw new IllegalArgumentException("Il tipo giocata non corrisponde al numero di singoli cavalli scelti");
            }
        }
        else if(TipoGiocata.accoppiata.compareTo(tipoGiocata)==0){
            if(this.cavallo.getNumSingoliCavalli()!=2){
                throw new IllegalArgumentException("Il tipo giocata non corrisponde al numero di singoli cavalli scelti");
            }
        }
        else if(TipoGiocata.trio.compareTo(tipoGiocata)==0){
            if(this.cavallo.getNumSingoliCavalli()!=3){
                throw new IllegalArgumentException("Il tipo giocata non corrisponde al numero di singoli cavalli scelti");
            }
        }

        StatoUtente ruolo = u.getRuolo();


        // scommessa su delega
        if (ruolo.compareTo(StatoUtente.bookmaker) == 0) {
            StrategyDecrementaAmmontareConto strategy = new StrategyBookmaker();
            //delega vieni inserita all'atto della creazione della scommessa nuovaScommessa()
            delega.decAmmontareResiduo(puntata);
            ComponentS scom = delega.getScommettitore();
            scom.decrementaAmmontareConto(puntata, strategy);

            // scommessa non su delega nè con bonus

        } else if (ruolo.compareTo(StatoUtente.scommettitore) == 0) {

            StrategyDecrementaAmmontareConto strategy = new StrategyScommettitore();
           
            //scommettitore viene inserita all'atto della creazione della scommessa nuovaScommessa()
            scommettitore.decrementaAmmontareConto(puntata, strategy);

        }
        try{
        AgenziaIppica.getInstance().incrementaAmmontareConto(puntata);
        }
        catch(SingletonException e){
            e.printStackTrace();
        }



    }
    

    public void setQuotazioneFissa(float quotazione) {
        this.quotazioneFissa = quotazione;
    }

    public void setImportoVincita(float puntata) {
        if (this.quotazioneFissa == 0) {
            throw new UnsupportedOperationException("effettuare prima setQuotazioneFissa()");
        }
        if (tipo.compareTo(TipoScommessa.quotaFissa) == 0) {
            this.importoVincita = puntata * this.quotazioneFissa;
        }
    }

    public void setPagata(boolean pagata) {
        this.pagata = true;
    }

    public boolean isPagata() {

        return this.pagata;
    }

    public void setCorsa(int idCorsa) {
        this.corsa = DBHandler.getCorsa(idCorsa);
            }

    public void incrementaAmmontareConto() {
        scommettitore.incrementaAmmontareConto(this.importoVincita);
    }

    public void setScommettitore(ComponentS u) {
        this.scommettitore = u;
    }

    public ComponentS getScommettitore() {
        return scommettitore;
    }

    public void setData(Date dataCorrente) {
        this.data = dataCorrente;
    }

    public void addDelega(ComponentS book, ComponentS scom) {

        this.delega = DBHandler.getDelega(book.getId(), scom.getId());

    }

    public void aggiornaBonus(int idBonus) {
        Bonus toMod = DBHandler.getBonus(idBonus);
        toMod.decImportoResiduo(puntata);
       

    }

   ì
    @Override
    public String toString() {
        String toRet = "";
        toRet += "Id = " + this.id + "\n";
        toRet += "Puntata = " + this.puntata + "\n";
        toRet += "Tipo = " + this.tipo + "\n";
        toRet += "Quotazione Fissa = " + this.quotazioneFissa + "\n";
        toRet += "Importo Vincita = " + this.importoVincita + "\n";
        toRet += "Vincente = " + this.vincente + "\n";
        toRet += "Pagata = " + this.pagata + "\n";
        toRet += "Tipo Giocata = " + this.tipoGiocata + "\n";
        toRet += "Data = " + this.data.toString() + "\n";
        if(this.scommettitore!=null){
             toRet += "Scommettitore = " + this.scommettitore.getId() + "\n";
        }
       
        toRet += "Cavallo = " + this.cavallo.getIdCavallo() + "\n";
        toRet += "Corsa = " + this.corsa.getId() + "\n";
        if(this.delega!=null){
            toRet += "Delega = " + this.delega.getInfo() + "\n";
        }
        
        return toRet;

    }

    public String getInfo() {
        String toRet = "";
        toRet += "Id = " + this.id + "";
        toRet += "Importovincita = " + this.importoVincita + "\n";
        
        return toRet;
    }

    public Corsa getCorsa() {
        return corsa;
    }
}
