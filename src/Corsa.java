package modello_di_progetto;

import java.util.LinkedList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class Corsa {

    private int id;
    private StatoCorsa stato;
    private Date dataOra;
    private int numCavalli;
    private String nomeIppodromo;
    private List<TipoLink_Piazzamento> piazzamenti;
    private String titolo;
    private TipoCorsa tipo;
    private float montepremi;

    public Corsa(String[] elem) {

        this.id = Integer.parseInt(elem[0].trim());
        this.stato = StatoCorsa.valueOf(elem[1]);
        this.dataOra = java.sql.Date.valueOf(elem[2]);
        this.numCavalli = Integer.parseInt(elem[3]);
        this.nomeIppodromo = elem[4];
        this.piazzamenti = new LinkedList<TipoLink_Piazzamento>(); //quando creo la corsa non c'è ancora la classifica che vado a inserire al termine della corsa
        this.titolo = elem[5];
        this.tipo = TipoCorsa.valueOf(elem[6]);
        this.montepremi = Float.parseFloat(elem[7]);
    }

    public List<Scommessa> getScommesseDaPagare() {
        List<Scommessa> toRet = new LinkedList();
        Scommessa elem = null;

        List<Scommessa> scom = DBHandler.getScommesse(id);

        Iterator it = scom.iterator();
        //LOOP iteriamo su tutte le scommesse associate alla corsa

        while (it.hasNext()) {

            elem = (Scommessa) it.next();
            //restituiamo tt qll che sono vincenti e non ancora pagate
            if (elem.isVincente() && !elem.isPagata()) {
                toRet.add(elem);
            }
        }
        return toRet;
    }

    public void addScommessa(Scommessa s) {

    }

    public String toString() {
        String toRet = "";
        toRet += "ID = " + this.id + "\n";
        toRet += "Stato = " + this.stato + "\n";
        toRet += "DataOra = " + this.dataOra.toString() + "\n";
        toRet += "Nome Ippodromo = " + this.nomeIppodromo + "\n";
        if(!this.piazzamenti.isEmpty()){
            toRet +="Classifica";
            for(TipoLink_Piazzamento p : this.piazzamenti){
                toRet+=p.getInfo()+"\t";

            }
        }
        return toRet;

    }

    /**
     * Restituisce un array di stringhe, dove ogni stringa contiene il nome
     * di un cavallo partecipante alla corsa
     */
    public String[] getInfo() {

        //per ogni partecipante alla corsa NomeCavallo - quotazione vincente/accoppiata
        List<SingoloCavallo> partecipanti = DBHandler.getPartecipanti(this.id);
        int dim = partecipanti.size();
        String[] toRet = new String[dim];

        for (int i = 0; i < dim; i++) {
            SingoloCavallo cav = partecipanti.get(i);
            Quotazione q = DBHandler.getQuotazione(id, cav);
            toRet[i] = "Cavallo="+cav.getNome()+" Quotazione fissa ="+q.getQuotazioneFissa()+"\t";


        }

        return toRet;
    }

    public int getId() {
        return this.id;
    }

    public String getTitolo() {
        return titolo;
    }

    public void addPiazzamento(TipoLink_Piazzamento piazzamento) {
        piazzamenti.add(piazzamento);
    }
}
