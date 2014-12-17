
package me.alb_i986.hbs.domain;

import java.util.LinkedList;
import java.util.List;



public class TriplaCavalli extends Cavallo {
	private List<SingoloCavallo> listaCavalli = new LinkedList<SingoloCavallo>();


        public TriplaCavalli(int id, SingoloCavallo primo,SingoloCavallo secondo,SingoloCavallo terzo){
          
             super(id);
            listaCavalli.add(primo);
            listaCavalli.add(secondo);
            listaCavalli.add(terzo);
        }
        public String getNome(){
            String toRet="";
            for(SingoloCavallo c : listaCavalli){
                toRet += c.getNome();
            }
            return toRet;
        }
     public int getNumSingoliCavalli(){
        return 3;
    }



}