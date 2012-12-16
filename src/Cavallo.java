package modello_di_progetto;

public abstract class Cavallo {

    private int idCavallo;

    public Cavallo(int id) {
        this.idCavallo = id;
    }

    public int getIdCavallo() {
        return idCavallo;
    }

    public void setIdCavallo(int idCavallo) {
        this.idCavallo = idCavallo;
    }

    public abstract int getNumSingoliCavalli();
	public abstract String getNome();

}
