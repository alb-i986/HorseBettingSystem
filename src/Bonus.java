package modello_di_progetto;

import java.util.Date;
import java.util.List;

public class Bonus {

    private int id;
    private StatoBonus stato;
    private final float importo;
    private float importoResiduo;
    private Date dataScadenza;
    private TipoScommessa tipoScommessa;
    private int owner;
    private List<Integer> listaidcorse;

    public Bonus(String[] elem) {
        this(Integer.parseInt(elem[0].trim()), StatoBonus.valueOf(elem[1]), Float.parseFloat(elem[2]), Float.parseFloat(elem[3]), java.sql.Date.valueOf(elem[4]), TipoScommessa.valueOf(elem[5]), Integer.parseInt(elem[6].trim()));
    }


    public Bonus(int id, StatoBonus stato, float importo, float importoResiduo, Date dataScadenza, TipoScommessa tipoScommessa, int owner) {
        this.id = id;
        this.stato = stato;
        this.importo = importo;
        this.importoResiduo = importoResiduo;
        this.dataScadenza = dataScadenza;
        this.tipoScommessa = tipoScommessa;
        this.owner = owner;
    }

    public void decImportoResiduo(float importo) {
        if (importoResiduo - importo < 0) {
            throw new IllegalArgumentException("Importo bonus non sufficiente: inserire un importo inferiore.");
        }
        this.importoResiduo -= importo;
        aggiornaStato();
    }

    /**
     * In base ai valori correnti di importo e importoResiduo, viene modificato lo stato del bonus
     */
    public void aggiornaStato() {
        if (importoResiduo == 0) {
            stato = StatoBonus.utilizzato;
        } else if (importoResiduo < importo) {
            stato = StatoBonus.semiUtilizzato;
        }
    }

    public StatoBonus getStato() {
        return stato;
    }

    @Override
    public String toString() {
        String toRet = "";
        toRet += "ID = " + this.id + "\n";
        toRet += "Stato = " + this.stato + "\n";
        toRet += "Importo = " + this.importo + "\n";
        toRet += "Importo residuo = " + this.importoResiduo + "\n";
        toRet += "Data scadenza = " + this.dataScadenza.toString() + "\n";
        toRet += "Tipo scommessa per cui il bonus e' valido = " + this.tipoScommessa + "\n";
        return toRet;

    }
}
