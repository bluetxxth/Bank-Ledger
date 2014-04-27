package dbPersistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import bankVerksamhet.Konto;
import bankVerksamhet.KontoRegister;
import bankVerksamhet.LoneKonto;
import bankVerksamhet.MaxiKonto;
import bankVerksamhet.SparKonto;

public class KontoAccessor {
    private final String m_JDBC_DriveRutine = "sun.jdbc.odbc.JdbcOdbcDriver";
    private final String m_url = "jdbc:odbc:mikrobank";
    private Connection m_connection;
    private Statement m_myStatement;

    private List<Konto> list;
    private KontoRegister registret = new KontoRegister();

    public KontoAccessor() {
	this.list = (LinkedList<Konto>) registret.getListan();

	System.out.println("list Size: " + list.size());
    }

    /**
     * 
     * @throws Exception
     */
    private void connectToDatabase() throws Exception {

	Class.forName(m_JDBC_DriveRutine);
	m_connection = DriverManager.getConnection(m_url, "", "");
	m_myStatement = m_connection.createStatement();
    }

    /**
     * @throws Exception
     * 
     */
    private void closeConnection() throws Exception {

	m_myStatement.close();
	m_connection.close();
    }

    /**
     * 
     * @return
     * @throws Exception
     */
    public List<Konto> createSparKontoList() throws Exception {

	// sqlqueries
	String sqlQuerySparKonto = "SELECT * FROM SPARKONTO ";

	// connect to database
	connectToDatabase();

	// create resultset
	ResultSet resultSet1 = m_myStatement.executeQuery(sqlQuerySparKonto);

	// // for testing purposes
	// ResultSetMetaData meta = resultSet1.getMetaData();
	//
	// // Check the column names
	// for (int index = 1; index <= meta.getColumnCount(); index++) {
	// System.out.println("Column " + index + " is named "
	// + meta.getColumnName(index) + " " + "Table Name: "
	// + meta.getTableName(1));
	// }

	while (resultSet1.next()) {

	    int kontoNummerSparKonto = resultSet1.getInt(1);
	    int kontoTypSparKonto = resultSet1.getInt(2);
	    double saldoSparKonto = resultSet1.getDouble(3);

	    // add to register
	    registret.add(new SparKonto(kontoNummerSparKonto,
		    kontoTypSparKonto, saldoSparKonto));

	}

	closeConnection();
	System.out.println("list Size::: " + list.size());

	return list;
    }

    public List<Konto> createMaxiKontoList() throws Exception {

	// sqlqueries
	String sqlQueryMaxiKonto = "SELECT * FROM MAXIKONTO ";

	// connect to database
	connectToDatabase();

	// create resultset
	ResultSet resultSet = m_myStatement.executeQuery(sqlQueryMaxiKonto);

	// // for testing purposes
	// ResultSetMetaData meta = resultSet1.getMetaData();
	//
	// // Check the column names
	// for (int index = 1; index <= meta.getColumnCount(); index++) {
	// System.out.println("Column " + index + " is named "
	// + meta.getColumnName(index) + " " + "Table Name: "
	// + meta.getTableName(1));
	// }

	while (resultSet.next()) {

	    int kontoNummerMaxiKonto = resultSet.getInt(1);
	    int kontoTypMaxiKonto = resultSet.getInt(2);
	    double saldoMaxiKonto = resultSet.getDouble(3);

	    // add to register
	    registret.add(new MaxiKonto(kontoNummerMaxiKonto,
		    kontoTypMaxiKonto, saldoMaxiKonto));
	}

	System.out.println("list Size::: " + list.size());
	closeConnection();

	return list;

    }

    public List<Konto> createLoneKontoList() throws Exception {

	// sqlqueries
	String sqlQueryLoneKonto = "SELECT * FROM LONEKONTO ";

	// connect to database
	connectToDatabase();

	// create resultset
	ResultSet resultSet = m_myStatement.executeQuery(sqlQueryLoneKonto);

	// // for testing purposes
	// ResultSetMetaData meta = resultSet.getMetaData();
	//
	// // Check the column names
	// for (int index = 1; index <= meta.getColumnCount(); index++) {
	// System.out.println("Column " + index + " is named "
	// + meta.getColumnName(index) + " " + "Table Name: "
	// + meta.getTableName(1));
	// }

	int kontoNummerLoneKonto = 0;
	int kontoTypLoneKonto = 0;
	double saldoLoneKonto = 0;

	while (resultSet.next()) {

	    kontoNummerLoneKonto = resultSet.getInt(1);
	    kontoTypLoneKonto = resultSet.getInt(2);
	    saldoLoneKonto = resultSet.getDouble(3);

	    // add to register
	    registret.add(new LoneKonto(kontoNummerLoneKonto,
		    kontoTypLoneKonto, saldoLoneKonto));
	}

	System.out.println("list Size::: " + list.size());
	closeConnection();

	return list;

    }

}
