package modello_di_progetto;

import java.util.LinkedList;
import java.util.List;

public class SingoloCavallo extends Cavallo {
	private String nome;
	
        private List<TipoLink_Piazzamento>  piazzatoIn;


        public SingoloCavallo(int id,String nome){
            super(id);
            this.nome = nome;
            this.piazzatoIn = new LinkedList<TipoLink_Piazzamento>();
        }
       



        public String getNome(){
            return this.nome;
        }

        public void addPiazzamento(TipoLink_Piazzamento piazzamento){
            piazzatoIn.add(piazzamento);
        }
    public int getNumSingoliCavalli(){
        return 1;
    }



}