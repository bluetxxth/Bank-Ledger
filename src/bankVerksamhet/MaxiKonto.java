package bankVerksamhet;

import grafiskTerminal.BankGui;

/**
 * A class emulating a MaxiKonto
 * 
 * @author Gabriel Nieva 2 dec 2013 09:19:31
 * 
 */
public class MaxiKonto implements Konto {

    private static int nyttNr = 1000;
    private int kontoNr;
    private int m_dbKontoNummer = 0;
    private int kontoTyp;
    double saldo;
    double interestPlus = 0.2;
    double interestMinus = 0.45;
    static final double threshold = 80000;

    /**
     * Constructs an object of type MaxiKonto
     * 
     * @param typ
     */
    public MaxiKonto(int typ) {

	updateAccountNumber();
	this.kontoTyp = typ;
    }
    

    /**
     * Creates an object of type SparKonto
     */
    public MaxiKonto(int id, int typ, double saldo) {

	this.kontoNr = id;
	this.kontoTyp = typ;
	this.saldo = saldo;
    }

    /**
     * Updates the account number
     */
    public void updateAccountNumber() {

	// Test if there is any account number originating from the database
	if (BankGui.getInstance().getM_dbSparKontoNummer() == 0) {
	    nyttNr++;
	    kontoNr = nyttNr;
	} else {
	    // if account number has its origin in the database use that as a
	    // basis
	    m_dbKontoNummer = BankGui.getInstance().getM_dbMaxiKontoNummer();

	    System.out.println("BankGui.getInstance().getM_dbSparKontoNummer()"
		    + m_dbKontoNummer);
	    // update
	    nyttNr = m_dbKontoNummer;
	    nyttNr++;
	    kontoNr = nyttNr;
	}
    }

    /**
     * Returnerar kontots nummer
     * 
     * @return kontonumret
     */
    public int getKontoNr() {
	return kontoNr;
    }

    @Override
    /**
     * Sets the account number
     * @param i an int representing the account nummber
     */
    public void setKontoNr(int i) {
	this.kontoNr = i;

    }

    @Override
    /**
     * Gets the account type
     * @return kontoTyp - an int representing the account type
     */
    public int getKontoTyp() {
	return kontoTyp;
    }

    @Override
    /**
     * Gets the saldo
     * @return saldo - a double representing the saldo
     */
    public double getSaldo() {
	return saldo;
    }

    /**
     * Sätter in ett belopp på kontot
     * 
     * @param belopp
     *            det belopp som ska sättas in
     * @return true om beloppet kunde sättas in, i annat fall false
     */
    public boolean sättIn(int belopp) {
	if (belopp == 0) {
	    return false;
	} else {
	    saldo += belopp;
	    return true;
	}
    }

    /**
     * @author gabriel Remove a sum of money from the account
     * @return - isOutOfBalance a boolean representing wither the account is out
     *         of balance or not
     */
    public boolean withdraw(int ammount) {

	int penalty = 20;

	boolean isOutOfBalance = false;

	if (ammount == 0) {
	    isOutOfBalance = true;

	} else {
	    isOutOfBalance = false;
	    saldo -= (ammount + penalty);
	}

	return isOutOfBalance;
    }

    @Override
    /**
     * Calculates interest
     * @return interest- a double representing the interest
     */
    public double calculateInterest() {

	double interest = 0;

	if (saldo < threshold) {
	    interest = saldo * 0.02;
	} else {
	    interest = saldo * 0.045;
	}

	return interest;
    }

    /**
     * Returnerar information om kontot
     * 
     * @return information om kontot
     */
    public String toString() {
	return "Konto nummer: " + kontoNr + " " + "type: " + kontoTyp
		+ " saldo: " + saldo;
    }

}
