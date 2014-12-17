package me.alb_i986.hbs;

import me.alb_i986.hbs.db.DBHandler;
import me.alb_i986.hbs.domain.AgenziaIppica;
import me.alb_i986.hbs.domain.ControllerScommettitore;
import me.alb_i986.hbs.ui.Window;

import java.sql.SQLException;

public class App {

    public static void main(String[] args) throws SQLException {

        /****STARTUP****/
        // inizializzo la connessione al db
        String url = "jdbc:derby://localhost:1527/hbs";
        DBHandler.init(url);
        
        // inizializzo Controllers
        AgenziaIppica BBL = AgenziaIppica.getInstance(0.3f);
        ControllerScommettitore controllerScom = new ControllerScommettitore();

        new Window(BBL, controllerScom).start();
    }
}

