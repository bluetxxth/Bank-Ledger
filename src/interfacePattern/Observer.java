package interfacePattern;

/**
 * Interface implements observer pattern
 * @author  Gabriel Nieva 2 dec 2013 12:07:59
 *
 *
 */
public interface Observer {
    
    /**
     * Update method of the observer in the Observer design pattern
     * @param m_accountSelected - String with the selected account
     * @param m_index - index of the account
     * @param m_tokenArray - String []  an array of tokens
     * @param kontoNummer - an integer representing the account number
     * @param kontoTyp - an integer representing the account type
     * @param m_saldo -a double representing the balance
     */
    public void update(String m_accountSelected, int m_index, String[]m_tokenArray, int kontoNummer, int kontoTyp, double m_saldo);
    
}
