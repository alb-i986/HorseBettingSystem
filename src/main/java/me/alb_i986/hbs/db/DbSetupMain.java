package me.alb_i986.hbs.db;

import java.io.*;
import java.sql.*;

public class DbSetupMain {

    public static void main(String[] args) throws SQLException, IOException {
        //serve a caricare i driver
        DriverManager.registerDriver(new org.apache.derby.jdbc.ClientDriver());

        String url = "jdbc:derby://localhost:1527/hbs;create=true";
        String user = "app";
        String pwd = "app";

        Connection c = DriverManager.getConnection(url);
        try {
            creaTabella(c);
            popolaDB(c);
        } finally {
            c.close();
        }
    }

    public static void creaTabella(Connection c) throws SQLException {
        Statement st = c.createStatement();

        // first of all, drop tables
        String[] tables = new String[]{"QUOTAZIONE", "LISTACORSEGIOCABILI", "BONUS", "PIAZZAMENTO", "PARTECIPANTI",
        "LISTACAVALLI", "SINGOLOCAVALLO", "DELEGA", "CAVALLO","CORSA","BOOKMAKER","SCOMMESSA","SCOMMETTITORE","UTENTE",
        "CONTO","IPPODROMO"};
        for(String table : tables) {
            try {
                st.execute("DROP TABLE " + table);
            } catch (SQLException e) {
                System.out.println("La tabella " + table + " non esisteva in DB ma e' tutto OK");
            }
        }
        // 2. create tables
        st.executeUpdate("CREATE TABLE CONTO"
                + "(ID INTEGER PRIMARY KEY, "
                + "STATO VARCHAR(15) CONSTRAINT STATOCONTO_CK CHECK (STATO IN ('aperto','chiuso','daAutorizzare')),"
                + "AMMONTARE  FLOAT NOT NULL,"
                + "AMMONTARETOT FLOAT NOT NULL"
                + ")");

        st.executeUpdate("CREATE TABLE UTENTE"
                + "(ID INTEGER PRIMARY KEY,"
                + "LOGIN  VARCHAR(50) NOT NULL,"
                + "PASSWORD  VARCHAR(50) NOT NULL,"
                + "CONTO INTEGER NOT NULL REFERENCES CONTO(ID)"
                + ")");

        st.executeUpdate("CREATE TABLE SCOMMETTITORE"
                + "(ID INTEGER REFERENCES UTENTE(ID),"
                + "NOME  VARCHAR(50) NOT NULL,"
                + "COGNOME  VARCHAR(50) NOT NULL,"
                + "CF  VARCHAR(16) NOT NULL,"
                + "IBAN VARCHAR(30) NOT NULL,"
                + "PRIMARY KEY (ID)"
                + ")");

        st.executeUpdate("CREATE TABLE BOOKMAKER"
                + "(ID INTEGER REFERENCES SCOMMETTITORE(ID),"
                + "PERCENTUALE FLOAT NOT NULL,"
                + "PRIMARY KEY (ID)"
                + ")");

        st.executeUpdate("CREATE TABLE IPPODROMO"
                + "(NOME  VARCHAR(40) PRIMARY KEY"
                + ")");


        st.executeUpdate("CREATE TABLE CORSA"
                + "(ID INTEGER PRIMARY KEY, "
                + "STATO VARCHAR(15) CONSTRAINT STATO_CK CHECK (STATO IN ('programmata','iniziata','terminata','cancellata')),"
                + "DATAORA DATE NOT NULL,"
                + "NUMCAVALLI INTEGER NOT NULL, "
                + "IPPODROMO VARCHAR(40) NOT NULL REFERENCES IPPODROMO(nome)"
                + ")");
        st.executeUpdate("CREATE TABLE SINGOLOCAVALLO"
                + "(NOME  VARCHAR(50) PRIMARY KEY"
                + ")");
        st.executeUpdate("CREATE TABLE CAVALLO"
                + "(ID INTEGER PRIMARY KEY"
                + ")");
        st.executeUpdate("CREATE TABLE QUOTAZIONE"
                + "(TIPO VARCHAR(15) CONSTRAINT tipoQuot_CK CHECK (TIPO IN ('vincente','piazzato','accoppiata','trio')),"
                + "QUOTAZIONEFISSA  FLOAT NOT NULL, "
                + "QUOTAZIONETOTALIZZATORE FLOAT NOT NULL,"
                + "CAVALLO INTEGER REFERENCES CAVALLO(ID),"
                + "CORSA INTEGER REFERENCES CORSA(ID),"
                + "PRIMARY KEY (CAVALLO,CORSA)"
                + ")");
        st.executeUpdate("CREATE TABLE DELEGA"
                + "(AMMONTAREMAX  FLOAT NOT NULL,"
                + "AMMONTARERESIDUO  FLOAT NOT NULL,"
                + "STATO VARCHAR(15) CONSTRAINT STATODELEGA_CK CHECK (STATO IN ('daAccettare','accettata')),"
                + "BOOKMAKER INTEGER NOT NULL REFERENCES BOOKMAKER(ID)," //??
                + "SCOMMETTITORE INTEGER NOT NULL REFERENCES SCOMMETTITORE(ID)," //??
                + "PRIMARY KEY(BOOKMAKER,SCOMMETTITORE)"
                + ")");
        
        st.executeUpdate("CREATE TABLE SCOMMESSA"
                + "(ID INTEGER PRIMARY KEY,"
                + "PUNTATA FLOAT NOT NULL, "
                + "TIPO VARCHAR(15) CONSTRAINT TIPO_CK CHECK (TIPO IN ('quotaFissa','totalizzatore')),"
                + "QUOTAZIONEFISSA FLOAT,"
                + "IMPORTOVINCITA FLOAT, "
                + "VINCENTE VARCHAR(5) CONSTRAINT VINCENTE_CK CHECK (VINCENTE IN ('true','false')), " //default false???
                + "PAGATA VARCHAR(5) CONSTRAINT PAGATA_CK CHECK (PAGATA IN ('true','false')), " //default false???
                + "TIPOGIOCATA VARCHAR(10) CONSTRAINT TIPOGIOCATA_CK CHECK (TIPOGIOCATA IN ('vincente','piazzato','accoppiata','trio')), " //default false???
                + "DATA DATE NOT NULL,"
                + "SCOMMETTITORE INTEGER REFERENCES SCOMMETTITORE(ID),"
                + "CORSA INTEGER REFERENCES CORSA(ID),"
                + "CAVALLO INTEGER REFERENCES CAVALLO(ID),"
                + "BOOKDELEGA integer,"
                + "SCOMDELEGA integer,"
                + "FOREIGN KEY (BOOKDELEGA,SCOMDELEGA) REFERENCES DELEGA (BOOKMAKER,SCOMMETTITORE)"
                + ")");

        st.executeUpdate("CREATE TABLE PIAZZAMENTO"
                + "(PARTECIPA VARCHAR(5) CONSTRAINT PARTECIPA_CK CHECK (PARTECIPA IN ('true','false')), "
                + "POSIZIONE  INTEGER NOT NULL,"
                + "CAVALLO VARCHAR(50) REFERENCES SINGOLOCAVALLO(NOME),"
                + "CORSA INTEGER REFERENCES CORSA(ID),"
                + "PRIMARY KEY (CAVALLO,CORSA)"
                + ")");
        st.executeUpdate("CREATE TABLE BONUS"
                + "(ID INTEGER PRIMARY KEY, "
                + "STATO VARCHAR(15) CONSTRAINT STATOBONUS_CK CHECK (STATO IN ('nonUtilizzato','semiUtilizzato','utilizzato')),"
                + "IMPORTO  FLOAT NOT NULL, "
                + "IMPORTORESIDUO  FLOAT NOT NULL, "
                + "DATASCADENZA DATE NOT NULL,"
                // "LISTAcorsegiocabili, " lo mettiamo in una tab separata
                + "TIPOSCOMMESSA VARCHAR(13) CONSTRAINT TIPOSCOM_CK CHECK (TIPOSCOMMESSA IN ('quotaFissa','totalizzatore')),"
                + "OWNER INTEGER NOT NULL REFERENCES SCOMMETTITORE(ID)" //SI
                + ")");

        st.executeUpdate("CREATE TABLE PARTECIPANTI"
                + "(CORSA INTEGER REFERENCES CORSA(ID),"
                + "CAVALLO VARCHAR(50) REFERENCES SINGOLOCAVALLO(NOME),"
                + "PRIMARY KEY(CORSA,CAVALLO)"
                + ")");
        st.executeUpdate("CREATE TABLE LISTACAVALLI"
                + "(IDCAVALLO INTEGER REFERENCES CAVALLO(ID),"
                + "NOMESINGOLOCAVALLO VARCHAR(50) REFERENCES SINGOLOCAVALLO(NOME),"
                + "PRIMARY KEY(IDCAVALLO,NOMESINGOLOCAVALLO)"
                + ")");
        st.executeUpdate("CREATE TABLE LISTACORSEGIOCABILI"
                + "(IDBONUS INTEGER REFERENCES BONUS(ID),"
                + "IDCORSA INTEGER REFERENCES CORSA(ID),"
                + "PRIMARY KEY(IDBONUS,IDCORSA)"
                + ")");

            st.close();
    }

    public static void popolaDB(Connection c)
            throws SQLException, FileNotFoundException, IOException {
        Statement st = c.createStatement();
        String[] tmp;
        String o1, o2, o3, o4, o5, o12, o13, o11, o10, o9, o8, o7, o6, linea, o14;
        int supp;
        InputStream contoStream = DbSetupMain.class.getResourceAsStream("conto.txt");
        InputStreamReader isr = new InputStreamReader(contoStream);
        BufferedReader br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];


            try {
                st.executeUpdate("INSERT INTO CONTO (ID,STATO,AMMONTARE,AMMONTARETOT) "
                        + "VALUES(" + o1 + ",'" + o2 + "'," + o3 + "," + o4 + ")");
            } catch (Exception e12) {
                System.out.println("error finsert ippo");
                e12.printStackTrace();
            }
            linea = br.readLine();
        }

        br.close();
        isr.close();

        InputStream cavalloStream = DbSetupMain.class.getResourceAsStream("cavallo.txt");
        isr = new InputStreamReader(cavalloStream);
        br = new BufferedReader(isr);

        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            try {
                st.executeUpdate("INSERT INTO CAVALLO (ID)"
                        + "VALUES(" + o1 + ")");
            } catch (Exception e11) {
                System.out.println("error finsert CAVALLO");
                e11.printStackTrace();
            }
            linea = br.readLine();
        }

        br.close();
        isr.close();

        InputStream utenteStream = DbSetupMain.class.getResourceAsStream("utente.txt");
        isr = new InputStreamReader(utenteStream);
        br = new BufferedReader(isr);

        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];


            try {
                st.executeUpdate("INSERT INTO UTENTE (ID, LOGIN, PASSWORD, CONTO) "
                        + "VALUES(" + o1 + ",'" + o2 + "','" + o3 + "'," + o4 + ")");
            } catch (Exception e11) {
                System.out.println("error finsert UTENTE");
                e11.printStackTrace();
            }
            linea = br.readLine();
        }

        br.close();
        isr.close();

        InputStream scommettitoreStream = DbSetupMain.class.getResourceAsStream("scommettitore.txt");
        isr = new InputStreamReader(scommettitoreStream);
        br = new BufferedReader(isr);

        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];
            o5 = tmp[4];
            //o6 = tmp[5];


            try {
                st.executeUpdate("INSERT INTO SCOMMETTITORE (ID,NOME,COGNOME,CF,IBAN) "
                        + "VALUES(" + o1 + ",'" + o2 + "','" + o3 + "','" + o4 + "','" + o5 + "')");
            } catch (Exception e11) {
                System.out.println("error finsert SCOMETTITORE");
                e11.printStackTrace();
            }
            linea = br.readLine();
        }

        br.close();
        isr.close();


        InputStream bookmakerStream = DbSetupMain.class.getResourceAsStream("bookmaker.txt");
        isr = new InputStreamReader(bookmakerStream);
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            try {
                st.executeUpdate("INSERT INTO BOOKMAKER (ID,PERCENTUALE) "
                        + "VALUES( " + o1 + "," + o2 + ")");
            } catch (Exception e12) {
                System.out.println("error finsert BOOK");
                e12.printStackTrace();
            }
            linea = br.readLine();
        }

        br.close();
        isr.close();

        InputStream ippodromoStream = DbSetupMain.class.getResourceAsStream("ippodromo.txt");
        isr = new InputStreamReader(ippodromoStream);
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];

            try {
                st.executeUpdate("INSERT INTO IPPODROMO (NOME) "
                        + "VALUES( '" + o1 + "')");
            } catch (Exception e12) {
                System.out.println("error finsert ippo");
                e12.printStackTrace();
            }
            linea = br.readLine();
        }

        br.close();
        isr.close();



        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("corsa.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];
            o5 = tmp[4];
            try {
                st.executeUpdate("INSERT INTO CORSA (ID, STATO, DATAORA, NUMCAVALLI , IPPODROMO) "
                        + "VALUES(" + o1 + ",'" + o2 + "','" + o3 + "'," + o4 + ",'" + o5 + "')");
            } catch (Exception e13) {
                System.out.println("error finsert corsi");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("singolocavallo.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];


            try {
                st.executeUpdate("INSERT INTO SINGOLOCAVALLO (NOME) "
                        + "VALUES('" + o1 + "')");
            } catch (Exception e13) {
                System.out.println("error finsert QsingCavallo");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("quotazione.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];
            o5 = tmp[4];
            try {
                st.executeUpdate("INSERT INTO QUOTAZIONE (TIPO,QUOTAZIONEFISSA, QUOTAZIONETOTALIZZATORE, CAVALLO, CORSA) "
                        + "VALUES('" + o1 + "'," + o2 + "," + o3 + "," + o4 + ","+o5+")");
            } catch (Exception e13) {
                System.out.println("error finsert QUOTAZIONE");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("delega.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];
            o5 = tmp[4];

            try {
                st.executeUpdate("INSERT INTO DELEGA (AMMONTAREMAX, AMMONTARERESIDUO, STATO, BOOKMAKER, SCOMMETTITORE) "
                        + "VALUES(" + o1 + "," + o2 + ",'" + o3 + "'," + o4 + "," + o5 + ")");
            } catch (Exception e13) {
                System.out.println("error finsert DELEGA");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("scommessa.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];
            o5 = tmp[4];
            o6 = tmp[5];
            o7 = tmp[6];
            o8 = tmp[7];
            o9 = tmp[8];
            o10 = tmp[9];
            o11 = tmp[10];
            o12 = tmp[11];
            o13 = tmp[12];
            o14 = tmp[13];


            try {
                st.executeUpdate("INSERT INTO SCOMMESSA (ID,PUNTATA,TIPO,QUOTAZIONEFISSA,IMPORTOVINCITA,VINCENTE,PAGATA,TIPOGIOCATA,DATA,SCOMMETTITORE,CORSA,CAVALLO,BOOKDELEGA,SCOMDELEGA) "
                        + "VALUES(" + o1 + "," + o2 + ",'" + o3 + "' ," + o4 + "," + o5 + ",'" + o6 + "','" + o7 + "','" + o8 + "','" + o9 + "'," + o10 + "," + o11 + "," + o12 + "," + o13 + "," + o14 + ")");
            } catch (Exception e14) {
                System.out.println("error finsert ascommessao");
                e14.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("partecipanti.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            try {
                st.executeUpdate("INSERT INTO PARTECIPANTI (CORSA , CAVALLO) "
                        + "VALUES(" + o1 + ",'" + o2 + "')");
            } catch (Exception e13) {
                System.out.println("error finsert PARTECIPANTI");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("piazzamento.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];

            try {
                st.executeUpdate("INSERT INTO PIAZZAMENTO (PARTECIPA, POSIZIONE, CAVALLO, CORSA) "
                        + "VALUES('" + o1 + "'," + o2 + ",'" + o3 + "'," + o4 + ")");
            } catch (Exception e13) {
                System.out.println("error finsert PIAZZAMENTO");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("bonus.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];
            o3 = tmp[2];
            o4 = tmp[3];
            o5 = tmp[4];
            o6 = tmp[5];
            o7 = tmp[6];
            try {
                st.executeUpdate("INSERT INTO BONUS (ID, STATO, IMPORTO, IMPORTORESIDUO , DATASCADENZA,  TIPOSCOMMESSA, OWNER) "
                        + "VALUES(" + o1 + ",'" + o2 + "'," + o3 + "," + o4 + ",'" + o5 + "','" + o6 + "'," + o7 + ")");
            } catch (Exception e13) {
                System.out.println("error finsert BONUS");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("listacavalli.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];



            try {
                st.executeUpdate("INSERT INTO LISTACAVALLI (IDCAVALLO , NOMESINGOLOCAVALLO) "
                        + "VALUES(" + o1 + ",'" + o2 + "')");
            } catch (Exception e13) {
                System.out.println("error finsert LISTACAVALLI");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();

        isr = new InputStreamReader(DbSetupMain.class.getResourceAsStream("listacorsegiocabili.txt"));
        br = new BufferedReader(isr);
        linea = br.readLine();
        while (linea != null) {
            tmp = linea.split(",");
            o1 = tmp[0];
            o2 = tmp[1];

            try {
                st.executeUpdate("INSERT INTO LISTACORSEGIOCABILI (IDBONUS, IDCORSA) "
                        + "VALUES(" + o1 + "," + o2 + ")");
            } catch (Exception e13) {
                System.out.println("error finsert LISTACORSEGIOCABILI");
                e13.printStackTrace();
            }
            linea = br.readLine();
        }
        br.close();
        isr.close();
    }

    public static String esegui_query(String url, String user, String pwd, String query) throws SQLException {
        Connection c = DriverManager.getConnection(url, user, pwd);
        Statement st = c.createStatement();
        ResultSet rs = st.executeQuery(query);
        ResultSetMetaData md = rs.getMetaData();
        int i, num_col = md.getColumnCount();

        String tmp = "";
        while (rs.next()) {
            tmp += "\n -- ";
            for (i = 1; i <= num_col; i++) {
                tmp += " | " + rs.getString(i);
            }
        }


        rs.close();
        st.close();
        c.close();

        return tmp;
    }
}
