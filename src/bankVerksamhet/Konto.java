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
     * Sätter in ett belopp på kontot
     * 
     * @param belopp
     *            det belopp som ska sättas in
     * @return true om beloppet kunde sättas in, i annat fall false
     */
    public abstract boolean sättIn(int belopp);


    public abstract boolean withdraw(int ammount);


    public abstract double calculateInterest();


    public abstract String toString();


    public abstract double getSaldo();


    public abstract void setKontoNr(int i);

}
