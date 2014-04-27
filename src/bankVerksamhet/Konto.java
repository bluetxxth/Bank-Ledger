package bankVerksamhet;

/**
 * Konto representerar ett bankkonto.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public interface Konto {

    /**
     * Returnerar kontots nummer
     * 
     * @return kontonumret
     */
    public abstract int getKontoNr();

    /**
     * 
     * @return
     */
    public abstract int getKontoTyp();

    /**
     * S�tter in ett belopp p� kontot
     * 
     * @param belopp
     *            det belopp som ska s�ttas in
     * @return true om beloppet kunde s�ttas in, i annat fall false
     */
    public abstract boolean s�ttIn(int belopp);


    public abstract boolean withdraw(int ammount);


    public abstract double calculateInterest();


    public abstract String toString();


    public abstract double getSaldo();


    public abstract void setKontoNr(int i);

}
