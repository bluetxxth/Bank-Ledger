package bankVerksamhet;

import grafiskTerminal.BankGui;
import java.util.LinkedList;

/**
 * Class that emulates a SparKonto
 * @author  Gabriel Nieva 2 dec 2013 09:02:20
 *
 */
public class SparKonto implements Konto{

    private static int nyttNr = 1000;
    private int kontoNr;
    private int kontoTyp;
    private int m_dbKontoNummer = 0;
    private double saldo;
    private double interest;
    double intereset = 0.15;
    private SparKonto m_sparkonto;

    /**
     * Construct an object of type SparKonto
     * @param typ int  the account type
     */
    public SparKonto(int typ) {

	updateKontoNummer();

	this.kontoTyp = typ;
    }

    /**
     * Creates an object of type SparKonto
     */
    public SparKonto(int id, int typ, double saldo) {

	this.kontoNr = id;
	this.kontoTyp = typ;
	this.saldo = saldo;
    }

    /**
     * Update account number
     */
    public void updateKontoNummer() {

	//check if there is an accout number from database
	if (BankGui.getInstance().getM_dbSparKontoNummer() == 0) {
	    nyttNr++;
	    kontoNr = nyttNr;
	} else {
	    /*if the account number originating from the database is present 
	    then use that as a base*/
	    m_dbKontoNummer = BankGui.getInstance().getM_dbSparKontoNummer();

	    // m_dbKontoNummer++;
	    System.out.println("BankGui.getInstance().getM_dbSparKontoNummer()"
		    + m_dbKontoNummer);
	    
	    //update number
	    nyttNr = m_dbKontoNummer;
	    nyttNr++;
	    kontoNr = nyttNr;
	}
    }


    /**
     * Returnerar kontots nummer
     * 
     * @return kontonumret - an int representing an account number
     */
    public int getKontoNr() {
	return kontoNr;
    }


    @Override
    /**
     * Set account number
     */
    public void setKontoNr(int i) {
	this.kontoNr = i;

    }

    @Override
    /**
     * Get account type
     */
    public int getKontoTyp() {

	return this.kontoTyp;
    }

    /**
     * Get saldo
     */
    public double getSaldo() {
	return saldo;
    }

    /**
     * Get interest
     * @return - a double representing the interest return
     */
    public double getIntereset() {
	return interest;
    }

    /**
     * get savings account
     * @return a SparKonto - the SparKonkto
     */
    public SparKonto getM_sparkonto() {
	return m_sparkonto;
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
	}else {
	saldo += belopp;
	}
	return true;
    }

    /**
     * @author gabriel Remove a sum of money from the account
     * @return - true or false depending if the money has been withdrawn or not
     */
    public boolean withdraw(int ammount) {

	boolean isOutOfBalance = false;

	if (ammount == 0) {
	    isOutOfBalance = true;

	} else {
	    isOutOfBalance = false;
	    saldo -= ammount;
	}

	return isOutOfBalance;
    }

    /**
     * Calculates interest for the sparkonto
     */
    public double calculateInterest() {

	double interest = 0;

	interest = saldo * 0.015;

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
