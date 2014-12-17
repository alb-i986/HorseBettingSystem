package me.alb_i986.hbs.domain;

import java.util.LinkedList;
import java.util.List;



public class CoppiaCavalli extends Cavallo {
	private List<SingoloCavallo> listaCavalli = new LinkedList<SingoloCavallo>();


        public CoppiaCavalli(int id, SingoloCavallo primo,SingoloCavallo secondo){
            //quotazione = q;
            super(id);
            listaCavalli.add(primo);
            listaCavalli.add(secondo);
        }
        public String getNome(){
            String toRet="";
            for(SingoloCavallo c : listaCavalli){
                toRet += c.getNome();
            }
            return toRet;
        }
    public int getNumSingoliCavalli(){
        return 2;
    }


}