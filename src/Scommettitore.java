package modello_di_progetto;


public class Scommettitore extends RuoloScommettitore {

    private String nome;
    private String cognome;
    private String cf;
    private String iban;
    
    

    public Scommettitore(ComponentS u, String nome, String cognome, String cf, String iban ,int idScommettitore) throws  DecoratorException {
        super(u,idScommettitore);
        // controllo che le regole di composizione del Decorator siano rispettate
        if (!(u instanceof Utente)) {
            throw new DecoratorException("Composizione non valida: a Scommettitore puo' essere aggiunto solo Utente.");
        }
        this.nome = nome;
        this.cognome = cognome;
        this.cf = cf;
        this.iban = iban;
        
    }

    public void incrementaAmmontareConto(float importo)  {
        super.incrementaAmmontareConto(importo);
    }

    public void decrementaAmmontareConto(float importo, StrategyDecrementaAmmontareConto strategy) {
        super.decrementaAmmontareConto(importo, strategy);
    }

    public String toString() {
        return super.toString() + "\n" + nome + " " + cognome + "\n" + cf + "\n" + iban + "\n";
    }

    public StatoUtente getRuolo() {
        return StatoUtente.valueOf("scommettitore");
    }

    public String getUsername() {
        return super.getUsername();
    }

    public Component next() {
        return super.next();
    }
}
