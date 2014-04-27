package dbPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import bankVerksamhet.Konto;
import bankVerksamhet.KontoRegister;
import bankVerksamhet.LoneKonto;
import bankVerksamhet.MaxiKonto;
import bankVerksamhet.SparKonto;

/**
 * Class responsible for connecting and changing data in the database
 * @author  Gabriel Nieva 2 dec 2013 12:07:24
 *
 */
public class KontoMutator {
    private final String m_JDBC_DriveRutine = "sun.jdbc.odbc.JdbcOdbcDriver";
    private final String m_url = "jdbc:odbc:mikrobank";
    private Connection m_connection;
    private Statement m_statement;

    private List m_sparKontoList;
    private List m_maxiKontoList;
    private List m_loneKontoList;

    private int[] m_kontoNummer;
    private int[] m_kontotTyp;
    private double[] m_saldo;

    private List<Konto> list;// = new LinkedList<Konto>();
    private KontoRegister registret = new KontoRegister();

    /**
     * Constructs an object of type KontoMutator
     */
    public KontoMutator() {

	m_sparKontoList = new ArrayList<SparKonto>();
	m_maxiKontoList = new ArrayList<MaxiKonto>();
	m_loneKontoList = new ArrayList<LoneKonto>();

	this.list = (LinkedList<Konto>) KontoRegister.getListan();

	int size = list.size();
	m_kontoNummer = new int[size];
	m_kontotTyp = new int[size];
	m_saldo = new double[size];

    }

    /**
     * Connects to database
     * @throws Exception
     */
    private void connectToDatabase() throws Exception {

	Class.forName(m_JDBC_DriveRutine);
	m_connection = DriverManager.getConnection(m_url, "", "");
	m_statement = m_connection.createStatement();

    }

    /**
     * Closes database connection
     * @throws Exception
     * 
     */
    private void closeConnection() throws Exception {

	m_statement.close();
	m_connection.close();
    }

    /**
     * Add data to tables
     * 
     * @throws Exception
     */
    public void addDataToTables() throws Exception {

	deleteAll();

	int rowsAdded;
	int index = 0;

	// create statement
	try {
	    connectToDatabase();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	System.out
		.println("m_sparKontoList size is: " + m_sparKontoList.size());
	System.out.println("lsit size is: " + list.size());

	// traverse the whole list
	for (int i = 0; i < list.size(); i++) {
	    // System.out.println("KontoNummer" +
	    // list.get(i).calculateInterest());

	    // search for sparkonto's data
	    if (list.get(i).getKontoTyp() == 0) {

		m_kontoNummer[i] = list.get(i).getKontoNr();
		m_kontotTyp[i] = list.get(i).getKontoTyp();
		m_saldo[i] = list.get(i).getSaldo();

		// test
		System.out.println("sparKontoNummer: " + m_kontoNummer[i] + " "
			+ "Typ" + m_kontotTyp[i] + " " + "Saldo" + " "
			+ m_saldo[i]);

		// insert in tables
		String queryString0 = "INSERT INTO SPARKONTO (ID, KONTOTYP, SALDO) VALUES ("
			+ m_kontoNummer[i]
			+ ","
			+ m_kontotTyp[i]
			+ ","
			+ m_saldo[i] + ")";

		// test
		System.out.println(queryString0);
		System.out.println(m_kontoNummer[i]
			+ "(myid) added to the database");

		try {
		    // Add rows
		    rowsAdded = m_statement.executeUpdate(queryString0);
		    System.out.println("rows updated: " + rowsAdded);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}

	    }
	    // search for maxikonto's data
	    if (list.get(i).getKontoTyp() == 1) {
		m_kontoNummer[i] = list.get(i).getKontoNr();
		m_kontotTyp[i] = list.get(i).getKontoTyp();
		m_saldo[i] = list.get(i).getSaldo();

		System.out.println("maxiKonto Nummer: " + m_kontoNummer[i]
			+ " " + "Typ" + m_kontotTyp[i] + " " + "Saldo" + " "
			+ m_saldo[i]);

		// insert in tables
		String queryString1 = "INSERT INTO MAXIKONTO (ID, KONTOTYP, SALDO) VALUES ("
			+ m_kontoNummer[i]
			+ ","
			+ m_kontotTyp[i]
			+ ","
			+ m_saldo[i] + ")";

		// test
		System.out.println(queryString1);
		System.out.println(m_kontoNummer[i]
			+ "(myid) added to the database");

		try {
		    // Add rows
		    rowsAdded = m_statement.executeUpdate(queryString1);
		    System.out.println("rows updated: " + rowsAdded);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}

	    }
	    // search for lonekonto's data
	    if (list.get(i).getKontoTyp() == 2) {
		m_kontoNummer[i] = list.get(i).getKontoNr();
		m_kontotTyp[i] = list.get(i).getKontoTyp();
		m_saldo[i] = list.get(i).getSaldo();

		System.out.println("loneKonto Nummer: " + m_kontoNummer[i]
			+ " " + "Typ" + m_kontotTyp[i] + " " + "Saldo" + " "
			+ m_saldo[i]);

		// insert in tables
		String queryString2 = "INSERT INTO LONEKONTO (ID, KONTOTYP, SALDO) VALUES ("
			+ m_kontoNummer[i]
			+ ","
			+ m_kontotTyp[i]
			+ ","
			+ m_saldo[i] + ")";

		// test
		System.out.println(queryString2);
		System.out.println(m_kontoNummer[i]
			+ "(myid) added to the database");

		try {
		    // Add rows
		    rowsAdded = m_statement.executeUpdate(queryString2);
		    System.out.println("rows updated: " + rowsAdded);
		} catch (Exception ex) {
		    ex.printStackTrace();
		}
	    }
	}
    }// end method

    /**
     * Delete SparKontos from database
     * 
     * @throws Exception
     */
    public void deleteSparKontos() throws Exception {

	int rowsDeleted;

	try {
	    // connect to database
	    connectToDatabase();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	// delete from tables
	String queryString = "DELETE FROM SPARKONTO";

	rowsDeleted = m_statement.executeUpdate(queryString);

	closeConnection();
    }

    /**
     * Delete Maxikontos from database
     * 
     * @throws Exception
     */
    public void deleteMaxiKontos() throws Exception {

	int rowsDeleted;

	try {
	    // connect to database
	    connectToDatabase();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	// delete from tables
	String queryString = "DELETE FROM MAXIKONTO";

	rowsDeleted = m_statement.executeUpdate(queryString);

	closeConnection();

    }

    /**
     * Delete Lonekontos from database
     * 
     * @throws Exception
     */
    public void deleteLoneKontos() throws Exception {

	int rowsDeleted;

	try {
	    // connect to database
	    connectToDatabase();
	} catch (Exception e) {
	    e.printStackTrace();
	}

	// delete from tables
	String queryString = "DELETE FROM LONEKONTO";

	rowsDeleted = m_statement.executeUpdate(queryString);

	closeConnection();
    }

    /**
     * Delete all accounts from database
     * 
     * @throws Exception
     */
    public void deleteAll() throws Exception {

	int rowsDeleted;

	deleteSparKontos();
	deleteMaxiKontos();
	deleteLoneKontos();

    }
}
