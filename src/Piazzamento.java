package modello_di_progetto;

public class Piazzamento {

    private TipoLink_Piazzamento piazzamento;

    public Piazzamento(TipoLink_Piazzamento piazzamento) {
        this.piazzamento = piazzamento;
    }

    //serve in un evetuale UC - fineCorsa (assegna piazzamenti ai partecipanti)
    public void inserire_link(Corsa co, SingoloCavallo ca) {
        co.addPiazzamento(piazzamento);
        ca.addPiazzamento(piazzamento);
    }
}
