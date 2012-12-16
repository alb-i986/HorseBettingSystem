/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package modello_di_progetto;
import java.util.LinkedList;


/**
 *
 * @author giovannibruno
 */
public class Utility {

    public static LinkedList StringToList(String input){
    LinkedList list = new LinkedList();
    String elem[] = input.split("-");
    for(int i=0; i<elem.length; i++){
        list.add(elem[i]);
    }

    return list;
    }


}
