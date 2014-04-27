package bankVerksamhet;

import grafiskTerminal.BankGui;

/**
 * Class that emulates a LoneKonto
 * @author gabriel 20 okt 2013 07:15:12
 * 
 */
public class LoneKonto implements Konto {

    private static int nyttNr = 1000;
    private int kontoNr;
    private int m_dbKontoNummer = 0;
    private int kontoTyp;
    double saldo;
    private Transaktion transaktion;
    double creditInterest = 0;
    double savingsInterest = 0;
    double creditLimit = -3000;


    /**
     * Constructs an object of type LoneKonto
     * @param typ - An int representing the type of the object
     */
    public LoneKonto(int typ) {

	testKontoNummer();
	this.kontoTyp = typ;
    }

    /**
     * Creates an object of type SparKonto
     */
    public LoneKonto(int id, int typ, double saldo) {

	this.kontoNr = id;
	this.kontoTyp = typ;
	this.saldo = saldo;
    }


    /**
     * Update account number
     */
    public void testKontoNummer() {

	if (BankGui.getInstance().getM_dbSparKontoNummer() == 0) {
	    nyttNr++;
	    kontoNr = nyttNr;
	} else {
	    m_dbKontoNummer = BankGui.getInstance().getM_dbLoneKontoNummer();

	    System.out.println("BankGui.getInstance().getM_dbSparKontoNummer()"
		    + m_dbKontoNummer);

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
     * 
     */
    public void setKontoNr(int i) {
	this.kontoNr = i;
    }

   /**
    * Gets the saldo
    * @param a double representing the saldo in the account
    */
    public double getSaldo() {

	return saldo;
    }

    @Override
    /**
     * Gets the account type
     * @return an int representing the type of account
     */
    public int getKontoTyp() {

	return this.kontoTyp;
    }

    /**
     * Sätter in ett belopp på kontot
     * 
     * @param belopp
     *            det belopp som ska sättas in
     * @return true om beloppet kunde sättas in, i annat fall false
     */
    public boolean sättIn(int belopp) {
	if (belopp <= 0)
	    return false;

	saldo += belopp;
	return true;
    }

    /**
     * @author Gabriel Remove a sum of money from the account
     * @return a boolean value true / false depending if money has been withdrawn
     */
    public boolean withdraw(int ammount) {

	if (ammount <= 0) {
	    return false;
	}
	saldo -= ammount;

	return true;
    }

    /**
     * Gets the savings interest in the account
     * @return - creditInterest a double representing the interest in the account
     */
    public double getCreditInterest() {
	return creditInterest;
    }

    /**
     * Gets the credit interest 
     * @return  savingsInterest a double representing the interest in the account
     */
    public double getSavingsInterest() {
	return savingsInterest;
    }

    /**
     * Get the credit limit
     * @return creditLimit - a double representing the credit limit
     */
    public double getCreditLimit() {
	return creditLimit;
    }

    @Override
    /**
     * Get the interest
     * @return interest - a double representing the interest
     */
    public double calculateInterest() {
	transaktion = new Transaktion();
	double interest = 0;

	if (saldo > creditLimit) {

	    creditInterest = 0;
	    savingsInterest = 0.0011; // reward

	    interest = saldo * savingsInterest;

	} else {
	    creditInterest = 0.179; // penalty
	    savingsInterest = 0;

	    interest = saldo * creditInterest;
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
