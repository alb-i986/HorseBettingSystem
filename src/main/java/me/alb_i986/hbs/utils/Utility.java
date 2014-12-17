package me.alb_i986.hbs.utils;

import java.util.*;

public class Utility {

    public static List<String> stringToList(String input){
        List<String> list = new ArrayList<String>();
        for(String element : input.split("-")) {
            list.add(element);
        }
        return list;
    }

}
