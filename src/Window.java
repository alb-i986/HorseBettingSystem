package modello_di_progetto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import static java.lang.System.*;

/**
 *
 * @author giovannibruno
 */
public class Window {

    static Scanner s = new Scanner(System.in);
    // i riferimenti ai Controller
    AgenziaIppica agenzia;
    ControllerScommettitore controllerScom;

    public Window(AgenziaIppica a, ControllerScommettitore c) {
        agenzia = a;
        controllerScom = c;
    }

    public void start() {

        int num_utente;
        String cmd = "";

        // simulo una procedura di login
        while (cmd.compareTo("q") != 0) {
            out.println("\nLogin come:");
            out.println("\t0. Agenzia Ippica");
            out.println("\t1. Osservatore");
            out.println("\t2. Scommettitore");
            out.println("\t3. Scommettitore VIP");
            out.println("\t4. Bookmaker");
            out.println("\t'q' per terminare\n");
            cmd = s.nextLine();

            if (cmd.compareTo("q") == 0) {
                break;
            }

            try {
                num_utente = Integer.parseInt(cmd);
            } catch (NumberFormatException e) {
                out.println("\nComando inserito non corretto.\n");
                continue;
            }

            Component utente;
            // non appena l'utente si "logga", creo l'oggetto utente e lo decoro in modo opportuno
            switch (num_utente) {
                case 0: // utente agenzia ippica
                    utente = agenzia;
                    break;
                case 1: // utente guest
                    utente = null;
                    break;
                case 2: // utente scommettitore
                    utente = new Scommettitore(new Utente(DBHandler.getConto(22), "vb", "vb"), "Viviana", "Bono", "VVBB", "IT97L00Hd6f54aaee", 22);
                    break;
                case 3: // utente scommettitore vip
                    utente = new ScommettitoreVIP(new Scommettitore(new Utente(DBHandler.getConto(33), "sb", "sb"), "Simona", "Bernardi", "SSBB", "IT97lawoiu23", 33), 33);
                    break;
                case 4: // utente bookmaker
                    utente = new Bookmaker(new Scommettitore(new Utente(DBHandler.getConto(44), "il", "il"), "Ilaria", "Lombardi", "IILL", "IT97f9809ksdf098", 44), 44, 0.2f);
                    break;
                default:
                    out.println("Utente sconosciuto.");
                    continue;
            }

            // caso particolare per l'osservatore, che non ha credenziali
            if (utente == null) {
                int i = 0;
                int cmd2 = -1;
                while (cmd2 != 0) {
                    if (i == 0) {
                        out.println("\nBenvenuto Guest!");
                        i++;
                    }
                    out.println("\n\t1. Visualizza corse e quotazioni");
                    out.println("\t0. Logout");
                    cmd2 = inputNumerico("Seleziona l'operazione che vuoi effettuare");
                    switch (cmd2) {
                        case 0:
                            continue;
                        case 1:
                            showCorseQuotazioniDialogue();
                            break;
                        default:
                            out.println("Operazione sconosciuta.");
                            continue;
                    }
                }
            } else {

                StatoUtente ruolo = utente.getRuolo();
                if (ruolo.compareTo(StatoUtente.agenzia_ippica) == 0) {
                    int i = 0;
                    int cmd2 = -1;
                    while (cmd2 != 0) {
                        if (i == 0) {
                            out.println("\nBentornato Padrone!");
                            i++;
                        }
                        out.println("\n\t1. Assegna le vincite relative ad una corsa");
                        out.println("\t0. Logout");
                        cmd2 = inputNumerico("Seleziona l'operazione che vuoi effettuare");
                        switch (cmd2) {
                            case 0:
                                continue;
                            case 1:
                                /* DAFARE!!!
                                String[] corseTerminate = agenzia.visualizzaCorseTerminate();
                                for (String corsa : corseTerminate) {
                                out.println(corsa);
                                 */
                                out.println("Scegli la corsa terminata di cui vuoi procedere con l'assegnamento delle vincite");
                                int idCorsa = inputNumerico("Inserisci l'id della corsa: ");

                                String[] scommesseDaPagare = agenzia.visualizzaScommesseDaPagare(idCorsa);
                                if (scommesseDaPagare.length == 0) {
                                    out.println("Relativamente alla corsa #" + idCorsa + " non ci sono vincite da pagare.");
                                } else {
                                    out.println("Di seguito, l'elenco delle scommesse vincenti relative alla corsa #" + idCorsa);
                                    for (String scommessa : scommesseDaPagare) {
                                        out.println(scommessa);
                                    }
                                    out.println("Inserisci gli id delle scommesse da pagare, separati da uno spazio.\nDigita 'x' per terminare l'inserimento");
                                    List<Integer> idsScommessa = new LinkedList<Integer>();
                                    while (s.hasNextInt()) {
                                        idsScommessa.add(s.nextInt());
                                    }
                                    try {
                                        agenzia.assegnaVincite(idsScommessa);
                                        out.println("Assegnamento vincite effettuato con successo");
                                    } catch (IllegalArgumentException e) {
                                        out.println("Si e' verificato un errore nel pagamento di una o piu' scommesse.");
                                        out.println(e);
                                        continue;
                                    }
                                }

                                break;
                            default:
                                out.println("Operazione sconosciuta.");
                                continue;
                        }
                    }
                } else { // utente della famiglia degli scommettitori
                    ComponentS utente_scom = (ComponentS) utente;
                    int i = 0;
                    int cmd2 = -1;
                    while (cmd2 != 0) {
                        if (i == 0) {
                            out.println("\nBentornato " + utente_scom.getUsername() + "!");
                            i++;
                        }
                        out.println("\n\t1. Visualizza corse e quotazioni");
                        out.println("\t2. Effettua scommesse");
                        if (ruolo.compareTo(StatoUtente.bookmaker) == 0) {
                            out.println("\t3. Effettua scommesse su delega");
                        }
                        out.println("\t0. Logout\n");
                        cmd2 = inputNumerico("Seleziona l'operazione che vuoi effettuare");
                        switch (cmd2) {
                            case 0:
                                continue;
                            case 1:
                                showCorseQuotazioniDialogue();
                                break;
                            case 2:
                                ComponentS tmp = utente_scom;
                                // tolgo temporaneamente la decorazione Bookmaker perchè il bookmaker sta scommettendo per conto proprio
                                if (ruolo.compareTo(StatoUtente.bookmaker) == 0) {
                                    utente_scom = (ComponentS) utente_scom.next();
                                }
                                showCorseQuotazioniProgrammateDialogue(utente_scom);
                                // ripristino la decorazione originale
                                utente_scom = tmp;
                                break;
                            case 3:
                                showCorseQuotazioniProgrammateDialogue(utente_scom);
                                break;
                            default:
                                out.println("Operazione sconosciuta.");
                                continue;
                        }
                    }
                }
            }
        } // end loop infinito
    }//end start()

    /**
     * Procedura relativa al caso d'uso "Effettuare scommesse"
     * precondizione: utente ha la decorazione Scommettitore
     */
    private void showEffettuaScommesseDialogue(ComponentS utente_scom, String[] cavalli, int idCorsa) {
        String[] nomiCavalli = new String[1];////per il momento permettiamo di scommettere su di un singolo cavallo
        TipoScommessa tipoScommessa = null;
        TipoGiocata tipoGiocata = null;
        float puntata;

        boolean altreScommesse = true;
        while (altreScommesse) {
            // stampo l'elenco dei cavalli che partecipano alla corsa scelta, con le relative quotazioni
            for (String c : cavalli) {
                out.println(c);
            }
            boolean cavalloOk = false;
            int i = 0;
            do {
                if (i > 0) {
                    out.println("Nome del cavallo inserito non valido.");
                }
                /* facilmente estensibile a scommessa su piu' cavalli */
                out.println("Inserisci il nome del cavallo su cui vuoi scommettere.");
                nomiCavalli[0] = s.nextLine();
                // controllo che il nome inserito sia di un cavallo che partecipa alla corsa selezionata
                for (String c : cavalli) {
                    if (c.equalsIgnoreCase(nomiCavalli[0])) {
                        cavalloOk = true;
                        break;
                    }
                }
                i++;
            } while (!cavalloOk);

            out.println("Inserisci la tipologia di giocata:");
            while (tipoGiocata == null) {
                out.println("\t1. Vincente");
                out.println("\t2. Piazzato");
                int tipoS = inputNumerico("Seleziona la tipologia di giocata");
                switch (tipoS) {
                    case 1:
                        tipoGiocata = TipoGiocata.vincente;
                        break;
                    case 2:
                        tipoGiocata = TipoGiocata.piazzato;
                        break;
                    default:
                        out.println("Operazione sconosciuta");
                        continue;
                }
            }
            out.println("Inserisci la tipologia di scommessa:");
            while (tipoScommessa == null) {
                out.println("\t1. Quota fissa");
                out.println("\t2. Totalizzatore");
                int tipoG = inputNumerico("Seleziona la tipologia di scommessa");
                switch (tipoG) {
                    case 1:
                        tipoScommessa = TipoScommessa.quotaFissa;
                        break;
                    case 2:
                        tipoScommessa = TipoScommessa.totalizzatore;
                        break;
                    default:
                        out.println("Operazione sconosciuta");
                        continue;
                }
            }

            StatoUtente ruolo = utente_scom.getRuolo();
            /* scenario alternativo di UC Effettuare scommesse, scommessa su delega */
            if (ruolo.compareTo(StatoUtente.bookmaker) == 0) {
                out.println("Seleziona l'ID dello scommettitore per cui vuoi scommettere");
                String[] deleghe = controllerScom.visualizzaDeleghe(utente_scom);
                for (String d : deleghe) {
                    out.println(d);
                }
                int idScomDelegante = inputNumerico("Inserisci l'ID dello scommettitore per cui vuoi scommettere");
                Delega d = DBHandler.getDelega(utente_scom.getId(), idScomDelegante);
                puntata = inputFloat("Inserisci l'importo della puntata");
                Scommessa scommessaAppCreata = controllerScom.nuovaScommessa(utente_scom, idCorsa, d.getScommettitore()); // non so se va bene il parametro utente_scom
                try {
                    controllerScom.inserisciDatiScommessa(utente_scom, scommessaAppCreata, tipoScommessa, tipoGiocata, nomiCavalli, puntata);
                } catch (IllegalArgumentException e) {
                    out.println(e);
                }

                out.println("Scommessa effettuata con successo");
                /*CONTROLLO*/ out.println("DATI SCOMMESSA\n" + scommessaAppCreata.toString());

            } /* scenari di UC Effettuare scommesse, principale e alternativo con bonus */ // caso utente scommettitore non decorato da bookmaker
            else {
                Scommessa sc = controllerScom.nuovaScommessa(utente_scom, idCorsa);

                /* scenario alternativo di UC Effettuare scommesse, scommessa con bonus */
                if (ruolo.compareTo(StatoUtente.scommettitore_vip) == 0 && inputBooleano("Vuoi usare un bonus per scommettere?")) {
                    String[] bonus = controllerScom.visualizzaBonus(utente_scom, idCorsa, tipoScommessa);
                    if (bonus.length == 0) {
                        out.println("Non hai bonus giocabili per questa scommessa.");
                    } else {
                        out.println("Di seguito, l'elenco dei bonus giocabili per la scommessa corrente");
                        for (String b : bonus) {
                            out.println(b);
                        }
                        int idBonus = inputNumerico("Inserisci l'ID del bonus che vuoi usare per scommettere");
                        puntata = inputFloat("Inserisci l'importo del bonus che vuoi puntare (non necessariamente per intero)");
                        try {
                            controllerScom.inserisciDatiScommessaConBonus(utente_scom, sc, tipoScommessa, tipoGiocata, nomiCavalli, idBonus, puntata);
                            out.println("Scommessa effettuata con successo.");
                            /*CONTROLLO*/ out.println("DATI SCOMMESSA\n" + sc.toString());

                        } catch (IllegalArgumentException e) {
                            out.println(e);
                        }
                    }
                } /* scenario principale di UC Effettuare scommesse */ else {
                    ComponentS tmp = utente_scom;
                    // se l'utente è uno scommettitore VIP ma ha deciso di scommettere senza usare bonus
                    // => togliamo la decorazione ScomVIP
                    if (ruolo.compareTo(StatoUtente.scommettitore_vip) == 0) {
                        utente_scom = (ComponentS) utente_scom.next();
                    }
                    puntata = inputFloat("Inserisci l'importo della puntata");
                    try {
                        controllerScom.inserisciDatiScommessa(utente_scom, sc, tipoScommessa, tipoGiocata, nomiCavalli, puntata);
                        out.println("Scommessa effettuata con successo.");
                        /*CONTROLLO*/ out.println("DATI SCOMMESSA\n" + sc.toString());

                    } catch (IllegalArgumentException e) {
                        out.println(e);
                    } finally {
                        utente_scom = tmp;
                    }
                }
            }
            altreScommesse = inputBooleano("Vuoi effettuare altre scommesse sulla corsa #" + idCorsa + "?");
        } // end loop "altre scommesse"
    }

    /**
     * Procedura relativa al caso d'uso "Visualizzare corse e quotazioni"
     * @param utente: se NULL non permette di effettuare scommesse
     */
    private void showCorseQuotazioniDialogue() {
        out.println("Inserisci il nome dell'ippodromo di cui vuoi visualizzare la lista delle corse (terminate e non).");
        String nomeIppodromo = s.nextLine();
        String[] corse = controllerScom.visualizzaCorse(nomeIppodromo);

        boolean altreCorse = true;
        while (altreCorse) {
            if (corse.length == 0) {
                out.println("Attualmente non esistono corse associate all' ippodromo " + nomeIppodromo + ".");
                return;
            } else {
                out.println("Di seguito l'elenco delle corse in programmazione all'ippodromo " + nomeIppodromo + "\n");
                // stampo l'elenco di corse legate all'ippodromo scelto
                for (String c : corse) {
                    out.println(c);
                }
                out.println("Scegli la corsa di cui vuoi visualizzare i dettagli");
                //s.nextLine();
                int idCorsa = inputNumerico("Inserisci l'id della corsa");
                String[] cavalli = controllerScom.visualizzaDettagli(idCorsa);

                int cmd3 = -1;
                while (cmd3 != 0) {
                    out.println("\nData la corsa #" + idCorsa + " dell'ippodromo " + nomeIppodromo + ":");
                    out.println("1. Visualizza lo storico di un cavallo");
                    out.println("0. Visualizza un'altra corsa");
                    out.println("9. Torna al menu' principale");
                    cmd3 = inputNumerico("Seleziona l'operazione che vuoi effettuare");
                    switch (cmd3) {
                        case 0:
                            continue;
                        case 9:
                            return;
                        case 1:
                            showStoriciCavalliDialogue(cavalli);
                            break;
                        default:
                            out.println("Operazione sconosciuta.");
                            continue;
                    }
                }
            }
            //altreCorse = inputBooleano("Vuoi visualizzare altre corse relative all'ippodromo " + nomeIppodromo + "?");
        } // end loop "altre corse"
    }

    /**
     * Procedura relativa al caso d'uso "Visualizzare corse e quotazioni", scenario alternativo "corse programmate"
     * precondizione: utente ha la decorazione Scommettitore
     * @param utente: se NULL non permette di effettuare scommesse
     */
    private void showCorseQuotazioniProgrammateDialogue(Component utente) {
        out.println("Inserisci il nome dell'ippodromo di cui vuoi visualizzare la lista delle corse da disputare");
        String nomeIppodromo = s.nextLine();
        String[] corse = controllerScom.visualizzaCorseProgrammate(nomeIppodromo);

        boolean altreCorse = true;
        while (altreCorse) {
            if (corse.length == 0) {
                out.println("Attualmente non ci sono corse in programmazione all'ippodromo " + nomeIppodromo + ".");
                return;
            } else {
                out.println("Di seguito l'elenco delle corse disputate e da disputarsi all'ippodromo " + nomeIppodromo + "\n");
                // stampo l'elenco di corse legate all'ippodromo scelto
                for (String c : corse) {
                    out.println(c);
                }
                out.println("Scegli la corsa di cui vuoi visualizzare i dettagli");
                //s.nextLine();
                int idCorsa = inputNumerico("Inserisci l'id della corsa");
                String[] cavalli = controllerScom.visualizzaDettagli(idCorsa);

                int cmd3 = -1;
                while (cmd3 != 0) {
                    out.println("\nData la corsa #" + idCorsa + ":");
                    out.println("1. Visualizza lo storico di un cavallo");
                    out.println("2. Scommetti su un cavallo");
                    out.println("0. Visualizza un'altra corsa");
                    out.println("9. Torna al menu' principale");
                    cmd3 = inputNumerico("Seleziona l'operazione che vuoi effettuare");
                    switch (cmd3) {
                        case 0:
                            continue;
                        case 9:
                            return;
                        case 1:
                            showStoriciCavalliDialogue(cavalli);
                            break;
                        case 2:
                            showEffettuaScommesseDialogue((ComponentS) utente, cavalli, idCorsa);
                            break;
                        default:
                            out.println("Operazione sconosciuta.");
                            continue;
                    }
                }
            }
            //altreCorse = inputBooleano("Vuoi visualizzare altre corse relative all'ippodromo " + nomeIppodromo + "?");
        } // end loop "altre corse"
    }

    private void showStoriciCavalliDialogue(String[] cavalli) {
        boolean altriStorici = true;
        while (altriStorici) {
            // stampo l'elenco dei cavalli che partecipano alla corsa scelta, con le relative quotazioni
            for (String c : cavalli) {
                out.println(c);
            }
            out.println("Scegli il cavallo di cui vuoi visualizzare lo storico, e il periodo di riferimento.");
            out.print("Inserisci il nome del cavallo: ");
            String nomeCavallo = s.nextLine();
            Date da, a;
            try {
                da = inputDate("Inserisci la data di inizio");
                a = inputDate("Inserisci la data di fine");
                if (da.compareTo(a) > 0) {
                    out.println("Le date inserire non sono valide. Devono essere tali che: data inizio < data fine.");
                    continue;
                }
                String[] piazzamenti = controllerScom.visualizzaStorico(nomeCavallo, da, a);
                // stampo l'elenco dei piazzamenti del cavallo nel periodo scelto
                for (String p : piazzamenti) {
                    out.println(p);
                }
                altriStorici = inputBooleano("Vuoi visualizzare lo storico di altri cavalli?");
            } catch (ParseException e) {
                out.println("Il formato delle date non è corretto. Deve essere del tipo 'gg/mm/aaaa'");
            }
        }
    }
    /*
    private void showStoriciFantiniDialogue() {
    }
     */

    public float inputFloat(String msg) {
        out.print(msg + ": ");
        while (!s.hasNextFloat()) {
            out.print("\nInserimento non valido. Inserisci un numero con la virgola: ");
            s.nextLine();
        }
        return s.nextFloat();
    }

    public int inputNumerico(String msg) {
        out.print(msg + ": ");
        Scanner scc = new Scanner(System.in);
        while (!scc.hasNextInt()) {
            out.print("\nInserimento non valido. Inserisci un numero intero: ");
            scc.nextLine();
        }
        int ret = scc.nextInt();
        scc.nextLine(); // pulisco il buffer
        return ret;
    }

    public boolean inputBooleano(String msg) {
        out.print(msg + " [true/false] ");
        while (!s.hasNextBoolean()) {
            out.print("\nInserimento non valido. Digita 'true' per 'sì' o 'false' per 'no': ");
            s.nextLine();
        }
        return s.nextBoolean();
    }

    public Date inputDate(String msg) throws ParseException {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        out.print(msg + " (gg/mm/aaaa) ");
        return df.parse(s.nextLine());
    }
}//end class

