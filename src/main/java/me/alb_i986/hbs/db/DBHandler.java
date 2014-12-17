package me.alb_i986.hbs.db;

import me.alb_i986.hbs.domain.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class DBHandler {

    static private String url = "";
    static private String user = "";
    static private String pwd = "";

    public static void init(String url_in, String user_in, String pwd_in) throws SQLException {
        url = url_in;
        user = user_in;
        pwd = pwd_in;
        DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
    }

    public static void init(String url_in) throws SQLException {
        url = url_in;
        DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());
    }

    //torna un array di Stringhe [0] con la risposta nel formato nome,cognome,... [1] con il numero di entry
    private static String[] esegui_query(String query) {
        try {
            Connection c = getConnection();
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData md = rs.getMetaData();
            int i, num_row = 0, num_col = md.getColumnCount();

            String tmp = "";
            while (rs.next()) {
                for (i = 1; i <= num_col; i++) {
                    tmp += rs.getString(i) + ((i == num_col) ? "" : ",");
                }
                num_row++;
                tmp += "\n";
            }

            rs.close();
            st.close();
            c.close();

            String[] risp = {tmp.trim(), num_row + ""};
            return risp;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static Connection getConnection() throws SQLException {
        if(user==null || pwd==null || user.isEmpty() || pwd.isEmpty())
            return DriverManager.getConnection(url);
        else
            return DriverManager.getConnection(url, user, pwd);
    }

    private static void update_element(String query) {
        try {
            Connection c = getConnection();
            Statement st = c.createStatement();
            st.executeUpdate(query);
            st.close();
            c.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static Corsa getCorsa(int idCorsa) {
        String risposta[] = null;
        String risposta2[] = null;
        risposta = esegui_query("SELECT * FROM CORSA WHERE ID =" + idCorsa);
        Corsa toRet = new Corsa(risposta[0].split(","));
        if (risposta[0].equals("")) {
            System.out.println("Nessun corsa con tali parametri");
            return null;
        }
        return toRet;
    }

    public static Quotazione getQuotazione(int idCorsa, Cavallo cavallo) {
        String[] risposta = null;

        risposta = esegui_query("SELECT Q.TIPO, Q.QUOTAZIONEFISSA, Q.QUOTAZIONETOTALIZZATORE, Q.CAVALLO, Q.CORSA FROM CORSA AS C, QUOTAZIONE AS Q WHERE C.ID = Q.CORSA AND Q.CAVALLO =" + cavallo.getIdCavallo());
        if (risposta[0].equals("")) {
            System.out.println("Nessun quotazione con tali parametri");
            return null;
        }
        Quotazione toRet = null;
        String[] dati = risposta[0].split(",");
        //tipoQutazione
        if (dati[0].equals("vincente")) {
            toRet = new QuotazioneVincente(dati);
        } else if (dati[0].equals("piazzato")) {
            toRet = new QuotazionePiazzato(dati);
        } else if (dati[0].equals("accoppiata")) {
            toRet = new QuotazioneAccoppiata(dati);
        } else if (dati[0].equals("trio")) {
            toRet = new QuotazioneTrio(dati);
        }

        return toRet;

    }

    public static List<Scommessa> getScommesse(int idCorsa) throws IllegalArgumentException {
        String risposta[] = null;

        risposta = esegui_query("SELECT * FROM SCOMMESSA WHERE CORSA =" + idCorsa);

        List<Scommessa> toRet = new LinkedList<Scommessa>();
        if (risposta[0].equals("")) {
            System.out.println("nessuna Scommessa con tali parametri");
            return null;
        }


        String num_string = risposta[1];

        int num = Integer.parseInt(num_string.trim());

        java.util.Scanner tmp = new java.util.Scanner(risposta[0]);
        String line = "";

        for (int j = 0; j < num; j++) {

            line = tmp.nextLine();
            toRet.add(new Scommessa(line.split(",")));
        }

        return toRet;

    }

    public static List<Corsa> getCorse(String nomeIppodromo) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM CORSA WHERE IPPODROMO = '" + nomeIppodromo + "'");
        List<Corsa> toRet = new LinkedList<Corsa>();
        if (risposta[0].equals("")) {
            System.out.println("nessuna Corsa");
            return null;
        }

        String num_string = risposta[1];
        int num = Integer.parseInt(num_string.trim());

        java.util.Scanner tmp = new java.util.Scanner(risposta[0]);
        String line = "";

        for (int j = 0; j < num; j++) {
            line = tmp.nextLine();
            toRet.add(new Corsa(line.split(",")));
        }

        return toRet;
    }


    public static List<Corsa> getCorseProgrammate(String nomeIppodromo) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM CORSA WHERE IPPODROMO = '" + nomeIppodromo + "' AND STATO='programmata'");
        List<Corsa> toRet = new LinkedList<Corsa>();
        if (risposta[0].equals("")) {
            System.out.println("nessuna Corsa");
            return null;
        }

        String num_string = risposta[1];
        int num = Integer.parseInt(num_string.trim());

        java.util.Scanner tmp = new java.util.Scanner(risposta[0]);
        String line = "";

        for (int j = 0; j < num; j++) {
            line = tmp.nextLine();
            toRet.add(new Corsa(line.split(",")));
        }

        return toRet;
    }

    
    public static List<TipoLink_Piazzamento> getStorico(String cavallo, Date dataInizio, Date dataFine) {
        String risposta[] = null;

        
        String str_in = "2005-11-11";
        String str_fin = "2019-11-11";
        risposta = esegui_query("SELECT P.PARTECIPA, P.POSIZIONE , P.CAVALLO, P.CORSA FROM CORSA AS C, PIAZZAMENTO AS P WHERE C.ID = P.CORSA AND C.DATAORA >= '" + /*dataInizio*/ str_in + "' AND C.DATAORA <= '" + /*dataFine*/ str_fin + "' AND P.CAVALLO = '" + cavallo + "'");

        String ris = risposta[0];
        String line = "";
        List<TipoLink_Piazzamento> toRet = new LinkedList<TipoLink_Piazzamento>();
        if (risposta[0].equals("")) {
            System.out.println("nessun piazzamento presente ");
            return null;
        }
        //else
        int num_entry = Integer.parseInt(risposta[1].trim());
        java.util.Scanner supp = new java.util.Scanner(risposta[0]);
        for (int i = 0; i < num_entry; i++) {
            line = supp.nextLine();
            TipoLink_Piazzamento piazzamento = new TipoLink_Piazzamento(line.trim());
            toRet.add(piazzamento);
        }


        return toRet;
    }

    public static Delega getDelega(int idBookmaker, int idScommettitore) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM DELEGA WHERE BOOKMAKER =" + idBookmaker + " AND SCOMMETTITORE=" + idScommettitore);
        if (risposta[0].equals("")) {
            System.out.println("nessuna delega rispetta tali parametri ");
            return null;
        }
        //else
        Delega toRet = new Delega(risposta[0].split(","));
        return toRet;

    }

    public static List<Delega> getDeleghe(int idBook) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM DELEGA WHERE BOOKMAKER =" + idBook);
        List<Delega> toRet = new LinkedList<Delega>();
        if (risposta[0].equals("")) {
            System.out.println("nessuna delega presente ");
            return null;
        }
        //else
        String line = "";
        int num_entry = Integer.parseInt(risposta[1].trim());
        java.util.Scanner supp = new java.util.Scanner(risposta[0]);

        Delega del = null;
        for (int i = 0; i < num_entry; i++) {
            line = supp.nextLine();
            del = new Delega(line.split(","));
            toRet.add(del);
        }
        return toRet;

    }

    public static Bonus getBonus(int idBonus) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM BONUS WHERE ID =" + idBonus);
        if (risposta[0].equals("")) {
            System.out.println("Nessun bonus con tale id");
            return null;
        }
        Bonus toRet = new Bonus(risposta[0].split(","));
        return toRet;
    }

    public static List<Bonus> getBonusScommettitore(ComponentS scom, int idCorsa, TipoScommessa tipoScom) {
        String risposta[] = null;
        risposta = esegui_query("SELECT S.ID, S.STATO,S.IMPORTO, S.IMPORTORESIDUO, S.DATASCADENZA, S.TIPOSCOMMESSA, S.OWNER FROM BONUS AS S, LISTACORSEGIOCABILI AS L WHERE S.OWNER="+scom.getId()+" AND L.IDBONUS = S.ID AND S.TIPOSCOMMESSA='" + tipoScom + "' AND L.IDCORSA=" + idCorsa + " AND (S.STATO='nonUtilizzato'  OR  S.STATO='semiUtilizzato')");
        List<Bonus> toRet = new LinkedList<Bonus>();
        if (risposta[0].equals("")) {
            System.out.println("Nessun bonus per questo scommettitore che rispecchia i parametri selezionati");
            return toRet;
        }

        String num_string = risposta[1];
        int num = Integer.parseInt(num_string.trim());

        java.util.Scanner tmp = new java.util.Scanner(risposta[0]);
        String line = "";

        for (int j = 0; j < num; j++) {
            line = tmp.nextLine();
            toRet.add(new Bonus(line.split(",")));
        }

        return toRet;
    }

    public static List<Bonus> getBonusScommettitore(ComponentS scom) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM BONUS WHERE OWNER="+scom.getId()+" AND (S.STATO='nonUtilizzato'  OR  S.STATO='semiUtilizzato'");
        List<Bonus> toRet = new LinkedList<Bonus>();
        if (risposta[0].equals("")) {
            System.out.println("Nessun bonus utilizzabile per questo scommettitore");
            return null;
        }

        String num_string = risposta[1];
        int num = Integer.parseInt(num_string.trim());

        java.util.Scanner tmp = new java.util.Scanner(risposta[0]);
        String line = "";

        for (int j = 0; j < num; j++) {
            line = tmp.nextLine();
            toRet.add(new Bonus(line.split(",")));
        }

        return toRet;
    }

    public static Scommessa getScommessa(int idScommessa) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM SCOMMESSA WHERE ID =" + idScommessa);
        
        if (risposta[0].equals("")) {
            System.out.println("Nessuna scommessa con tale id");
            return null;
        }

        Scommessa toRet = new Scommessa(risposta[0].split(","));
        return toRet;
    }

    public static Conto getConto(int idUtente) {
        String risposta[] = null;
        risposta = esegui_query("SELECT C.ID, C.STATO, C.AMMONTARE, C.AMMONTARETOT FROM UTENTE AS U, CONTO AS C WHERE C.ID = U.CONTO AND U.ID =" + idUtente);
        Conto toRet = new Conto(risposta[0].split(","));

        if (risposta[0].equals("")) {
            System.out.println("Nessun conto ha tale id");
            return null;
        }
        return toRet;
	}

    public static Conto getContoById(int idConto) {
        String risposta[] = null;
        risposta = esegui_query("SELECT * FROM CONTO  WHERE ID =" + idConto);
        Conto toRet = new Conto(risposta[0].split(","));
        if (risposta[0].equals("")) {
            System.out.println("Nessun conto ha tale id");
            return null;
        }
        return toRet;
    }

    
    public static SingoloCavallo getCavalloSingolo(String nome) {
        String[] cavallo = new String[1];
        cavallo[0] = nome;
        return (SingoloCavallo) DBHandler.getCavallo(cavallo);
    }

    public static Cavallo getCavallo(int idCavallo) {
        String risposta[] = null;
        risposta = esegui_query("SELECT S.NOME FROM LISTACAVALLI AS C, SINGOLOCAVALLO AS S WHERE S.NOME = C.NOMESINGOLOCAVALLO AND C.IDCAVALLO=" + idCavallo);

        String ris = risposta[0];
        String nome = "";
        List<SingoloCavallo> lista_cav = new LinkedList<SingoloCavallo>();
        if (risposta[0].equals("")) {
            System.out.println("nessun cavallo presente ");
            return null;
        }
        //else
        int num_entry = Integer.parseInt(risposta[1].trim());
        java.util.Scanner supp = new java.util.Scanner(risposta[0]);
        SingoloCavallo s_cav = null;
        for (int i = 0; i < num_entry; i++) {
            nome = supp.nextLine();
            s_cav = new SingoloCavallo(idCavallo, nome);
            lista_cav.add(s_cav);
        }
        int num_cav = lista_cav.size();
        if (num_cav == 1) {
            return s_cav;
        } else if (num_cav == 2) {
            return new CoppiaCavalli(idCavallo, lista_cav.get(0), lista_cav.get(1));
        } else if (num_cav == 3) {
            return new TriplaCavalli(idCavallo, lista_cav.get(0), lista_cav.get(1), lista_cav.get(2));
        }
        //else
        return null;
    }

    /**
     * Data una serie di nomi di cavalli, crea e restituisce un oggetto Cavallo, che li raggruppa
     */

    public static Cavallo getCavallo(String[] cavallo) {
        String risposta[] = null;
        String elem[] = null;
        String line = "";
        risposta = esegui_query("SELECT COUNT(*) AS NUMCAVALLI , IDCAVALLO  FROM LISTACAVALLI GROUP BY(IDCAVALLO)");
        //restituisce una tabella
        //NUMEROCAVALLI - IDCAVALLO

        if (risposta[0].equals("")) {
            System.out.println("nessun cavallo presente ");
            return null;
        }
        int num_entry = Integer.parseInt(risposta[1].trim());
        java.util.Scanner supp = new java.util.Scanner(risposta[0]);
        List<Integer> idCavalli = new LinkedList<Integer>();
        int num_cav = cavallo.length;

        for (int i = 0; i < num_entry; i++) {
            line = supp.nextLine();
            elem = line.split(",");
            //elem[0] = numerocavalli
            //elem[1] = idcavallo con quel numer di cavalli


            if (Integer.parseInt(elem[0]) == num_cav) {
                //se il numero di cavalli è quello che cerchiao l'id cavallo è tra i papabili
                idCavalli.add(Integer.parseInt(elem[1]));
            }

        }
        //creiamo la query adatta alla ricerca di 1-2-3 singolicavalli
        String query = "";
        if (num_cav == 1) {
            query = "SELECT * FROM LISTACAVALLI AS L1 WHERE L1.NOMESINGOLOCAVALLO='" + cavallo[0] + "' AND L1.IDCAVALLO=";
        } else if (num_cav == 2) {
            query = "SELECT L1.IDCAVALLO, L1.NOMESINGOLOCAVALLO, L2.NOMESINGOLOCAVALLO FROM LISTACAVALLI AS L1,LISTACAVALLI AS L2  WHERE L1.NOMESINGOLOCAVALLO='" + cavallo[0] + "' AND L2.NOMESINGOLOCAVALLO='" + cavallo[1] + "' AND L1.IDCAVALLO=L2.IDCAVALLO AND L2.IDCAVALLO=";
        } else if (num_cav == 3) {
            query = "SELECT L1.IDCAVALLO, L1.NOMESINGOLOCAVALLO, L2.NOMESINGOLOCAVALLO, L3.NOMESINGOLOCAVALLO FROM LISTACAVALLI AS L1,LISTACAVALLI AS L2,LISTACAVALLI AS L3  WHERE L1.NOMESINGOLOCAVALLO='" + cavallo[0] + "' AND L2.NOMESINGOLOCAVALLO='" + cavallo[1] + "' AND L3.NOMESINGOLOCAVALLO='" + cavallo[1] + "' AND L1.IDCAVALLO=L2.IDCAVALLO AND L2.IDCAVALLO=L3.IDCAVALLO AND L3.IDCAVALLO=";
        } else {
            System.out.println("puoi cercare da 1 a 3 singolicavalli ");
            return null;
        }
        for (Integer id : idCavalli) {
            //la risposta sarà unica xkè nn ci sono cavalli duplicati
            risposta = esegui_query(query + "" + id);
            if (!risposta[0].equals("")) {
                //ho trovato il cavallo che cercavo
                break;
            }
        }
        String dati[] = risposta[0].split(",");
        
        if (num_cav == 1) {
            return new SingoloCavallo(Integer.parseInt(dati[0]), dati[1]);
        } else if (num_cav == 2) {
            SingoloCavallo uno = new SingoloCavallo(Integer.parseInt(dati[0]), dati[1]);
            SingoloCavallo due = new SingoloCavallo(Integer.parseInt(dati[0]), dati[2]);
            return new CoppiaCavalli(Integer.parseInt(dati[0]), uno, due);
        } else if (num_cav == 3) {
            SingoloCavallo uno = new SingoloCavallo(Integer.parseInt(dati[0]), dati[1]);
            SingoloCavallo due = new SingoloCavallo(Integer.parseInt(dati[0]), dati[2]);
            SingoloCavallo tre = new SingoloCavallo(Integer.parseInt(dati[0]), dati[3]);
            return new TriplaCavalli(Integer.parseInt(dati[0]), uno, due, tre);
        }
        //else
        return null;


    }

    
    
    public static ComponentS getScommettitore(int idScommettitore) throws IllegalArgumentException {
        String risposta[] = null;
        risposta = esegui_query("SELECT U.CONTO, U.LOGIN, U.PASSWORD, S.NOME, S.COGNOME, S.CF, S.IBAN , U.ID FROM SCOMMETTITORE AS S, UTENTE AS U WHERE S.ID = U.ID AND S.ID =" + idScommettitore);
        String dati[] = risposta[0].split(",");
        
        if (risposta[0].equals("")) {
            System.out.println("Nessun scommettitore ha tale id");
            return null;
        }

        ComponentS toRet = new Scommettitore(new Utente(DBHandler.getContoById(Integer.parseInt(dati[0])), dati[1], dati[2]), dati[3], dati[4], dati[5], dati[6], Integer.parseInt(dati[7]));
        return toRet;
    }
	
    public static List<SingoloCavallo> getPartecipanti(int idCorsa) {
        String risposta[] = null;
        risposta = esegui_query("SELECT CAVALLO FROM PARTECIPANTI WHERE CORSA = " + idCorsa);
        String ris = risposta[0];
        String line = "";
        List<SingoloCavallo> lista_cav = new LinkedList<SingoloCavallo>();
        if (risposta[0].equals("")) {
            System.out.println("nessun cavallo presente ");
            return null;
        }
        //else
        int num_entry = Integer.parseInt(risposta[1].trim());
        java.util.Scanner supp = new java.util.Scanner(risposta[0]);
        for (int i = 0; i < num_entry; i++) {

            line = supp.nextLine();

            lista_cav.add(DBHandler.getCavalloSingolo(line));
        }


        return lista_cav;
    }
}
