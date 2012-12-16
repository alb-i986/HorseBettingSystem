/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modello_di_progetto;

import java.sql.SQLException;


public class TipoLink_Piazzamento {
    private boolean partecipa;
    private int posizione;
    private SingoloCavallo ilCavallo;
    private Corsa laCorsa;

    public TipoLink_Piazzamento(String dati){

         String[] elem = dati.split(",");
            this.partecipa = Boolean.parseBoolean(elem[0]);
             this.posizione = Integer.parseInt(elem[1].trim());
             this.ilCavallo = DBHandler.getCavalloSingolo(elem[2].trim());
             this.laCorsa = DBHandler.getCorsa(Integer.parseInt(elem[3].trim()));
    }

    


    public String getInfo() {
		String toRet = "";
                toRet += "Cavallo  = " + this.ilCavallo.getNome();
                toRet +="Partecipa = " + this.partecipa+"\n";
                toRet +="Posizione = " + this.posizione+"\n";
              

                return toRet;
	}
    public void setAssociazione(Corsa co, SingoloCavallo ca) {
        this.laCorsa=co;
        this.ilCavallo=ca;
    }
}
