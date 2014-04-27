package bankVerksamhet;

import java.util.LinkedList;

/**
 * Klassen Transaktion ansvarar för bankens alla transaktioner
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Transaktion {

    private Konto aktivtKonto;
    private KontoRegister registret = new KontoRegister();
    private int m_kontoTyp = 0;
    private int m_kontoNr = 0;
    private String m_strOut;
    private LinkedList<Konto> list;

    /**
     * Constructs an object of type transaction
     */
    public Transaktion() {

	this.list = (LinkedList<Konto>) KontoRegister.getListan();
    }

    /**
     * Sets the account type
     * 
     * @param m_kontoTyp
     *            an int representing the account type
     */
    public void setM_kontoTyp(int m_kontoTyp) {
	this.m_kontoTyp = m_kontoTyp;
    }

    /**
     * Set the account number
     * 
     * @param m_kontoNr
     *            an int representing the account number
     */
    public void setM_kontoNr(int m_kontoNr) {
	this.m_kontoNr = m_kontoNr;
    }

    /**
     * Get the account type
     * 
     * @return m_kontoType an int representing the account type
     */
    public int getM_kontoTyp() {
	return m_kontoTyp;
    }

    /**
     * Gets the account number
     * 
     * @return m_kontoNummer - an int representing the account number
     */
    public int getM_kontoNr() {
	return m_kontoNr;
    }

    /**
     * Get the active account
     * 
     * @return Konto - an account representing the active account
     */
    public Konto getAktivtKonto() {
	return aktivtKonto;
    }

    /**
     * Set the active account
     * 
     * @param aktivtKonto
     *            - an account Represents the active account
     */
    public void setAktivtKonto(Konto aktivtKonto) {
	this.aktivtKonto = aktivtKonto;
    }

    /**
     * Get the string with the information pertinent to the account
     * 
     * @return m_strOut a string with the information relevant to the account
     */
    public String getM_strOut() {
	return m_strOut;
    }

    /**
     * Get the account register
     * 
     * @return - registret - a KontoRegister to be used to make operations on
     *         the accounts
     */
    public KontoRegister getRegistret() {
	return registret;
    }

    /**
     * Skapar ett konto och sätter det till aktivt konto Registrerar kontot till
     * kontoregistret och uppdaterar informationen i terminalen
     * 
     * @param kontotyp
     *            den typ av konto som ska skapas
     */
    public void skapaKonto(int kontotyp) {

	m_kontoTyp = kontotyp;

	aktivtKonto = KontoFabrik.skapaKonto(m_kontoTyp);

	registret.add(aktivtKonto);

	m_strOut = kontoInfo();
    }

    /**
     * @author gabriel Removes active account and updates information on the
     *         terminal.
     * @param kontoTyp
     *            the type of account to be removed.
     */
    public void taBortKonto(int kontoNr, int kontoTyp) {

	m_kontoTyp = kontoTyp;
	m_kontoNr = kontoNr;

	if (!(registret.isEmpty()) && (harAktivtKonto())) {

	    registret.remove(aktivtKonto);
	} else {

	    System.out.println("there is nothing to delete");
	}
	m_strOut = "Deleted " + "Account: " + aktivtKonto.getKontoNr()
		+ "type " + aktivtKonto.getKontoTyp();
    }

    /**
     * Aktiverar ett konto så att det blir möjligt att utföra olika
     * transaktioner på det och uppdaterar terminalen med information om det
     * aktiva kontot
     * 
     * @param kontoNr
     *            numret på det konto som ska aktiveras
     */
    public void aktiveraKonto(int kontoNr, int kontoTyp) {
	String strOut = null;
	this.m_kontoNr = kontoNr;
	this.m_kontoTyp = kontoTyp;
	aktivtKonto = registret.search(m_kontoNr, m_kontoTyp);

	m_strOut = "Activating: " + kontoInfo();
    }

    /**
     * Undersöker om något konto är aktivt
     * 
     * @return true om ett konto är aktivt, i annat fall false
     */
    public boolean harAktivtKonto() {
	return aktivtKonto != null;
    }

    /**
     * Returnerar information om aktivt konto
     * 
     * @return Om ett konto är aktivt så returneras information om kontot, i
     *         annat fall returneras ett felmeddelande
     */
    public String kontoInfo() {
	if (harAktivtKonto())
	    return aktivtKonto.toString();
	else
	    return "Inget konto är aktivt.";
    }

    /**
     * Sätter in ett belopp på aktivt konto. Om beloppet inte kunde sättas in så
     * kommer ett meddelande om detta att visas i terminalen
     * 
     * @param belopp
     *            Det belopp som ska sättas in på det aktiva kontot
     */
    public void sättIn(int belopp) {

	if (aktivtKonto.sättIn(belopp)) {
	    m_strOut = "Beloppet sätts in.";

	} else {
	    m_strOut = "Beloppet kunde ej sättas in.";
	}
    }

    /**
     * @author gabriel Withdraw money from the bank
     * @param the
     *            ammount that is to be withdrawn from the bank
     */
    public void withdrawFromAccount(int ammount, int kontoNr, int kontoTyp) {

	double minThreshold = -3000;

	System.out.println("kontoTyp: " + aktivtKonto.getKontoTyp() + " "
		+ "Saldo: " + aktivtKonto.getSaldo());

	// if sparkonto
	if (((aktivtKonto.getKontoTyp() == 0))) {

	    if (((aktivtKonto.getSaldo() - ammount) >= 0)) {
		m_strOut = "OK";
		aktivtKonto.withdraw(ammount);
	    } else {
		m_strOut = "Beloppet kunde ej tas ut";
	    }
	}

	// if maxikonto
	if ((aktivtKonto.getKontoTyp() == 1)) {

	    if (((aktivtKonto.getSaldo() >= (ammount+20))) ) {
		m_strOut = "OK ";
		aktivtKonto.withdraw(ammount);//normal withdrawal
		
	    } else {
		m_strOut = "Kunde ej tas ut, kom ihåg 20Kr i avgift";
	    }
	}
	// if it is a lonekonto -- this has credit allowed upto -3000
	if (aktivtKonto.getKontoTyp() == 2) {

	    if (((aktivtKonto.getSaldo() - ammount) >= minThreshold)) {
		m_strOut = "OK";
		aktivtKonto.withdraw(ammount);
	    } else {
		m_strOut = "Beloppet kunde ej tas ut";
	    }
	}
    }

    /**
     * Calculate interest
     * 
     * @return - interest - the interest
     */
    public double calculateInterest() {

	int interest = 0;

	System.out.println("account list size" + list.size());

	for (int i = 0; i < list.size(); i++) {
	    interest += list.get(i).calculateInterest();
	}
	return interest;
    }

    /**
     * Inaktiverar aktivt konto, så att inga andra transaktioner än
     * ränteberäkning går att utföra
     */
    public void inaktiveraKonto(int kontoNr, int kontoTyp) {

	this.m_kontoNr = kontoNr;
	this.m_kontoTyp = kontoTyp;

	aktivtKonto = registret.search(m_kontoNr, m_kontoTyp);
	m_strOut = "Deactivating: " + kontoInfo();
	aktivtKonto = null;
    }
}
