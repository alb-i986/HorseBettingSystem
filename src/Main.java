package modello_di_progetto;

import java.sql.SQLException;

/**
 *
 * @author giovannibruno
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {

        /****STARTUP****/
        // inizializzo la connessione al db
        String url = "jdbc:derby://localhost:1527/spings";
        DBHandler.init(url, "app", "app");
        
        // inizializzo Controllers
        AgenziaIppica BBL = AgenziaIppica.getInstance(new Utente(DBHandler.getConto(11), "adm", "adm"), 0.3f);
        ControllerScommettitore controllerScom = new ControllerScommettitore();


        Window w = new Window(BBL, controllerScom);

        w.start();

    }
}

