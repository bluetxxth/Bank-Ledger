package grafiskTerminal;

/**
 * Class that emulates a MaxiKontoModel based on a ListModel which uses a
 * Default List Model
 * 
 * @author Gabriel Nieva 2 dec 2013 12:03:06
 * 
 * 
 */
public class MaxiKontoModel extends ListModel {

    private static MaxiKontoModel m_uniqueInstance;

    /**
     * Sonstructs an object of type MaxiKontoModel
     */
    public MaxiKontoModel() {

    }

    /**
     * Get the unique instance of MaxiKontoModel
     * 
     * @return - m_uniqueInstance an object representing the unique instance of
     *         MaxiKontoModel
     */
    public static MaxiKontoModel getInstance() {

	if (m_uniqueInstance == null) {
	    m_uniqueInstance = new MaxiKontoModel();
	}
	return m_uniqueInstance;
    }

    /**
     * Removes an account
     */
    public void removeAccount() {

	System.out.println("firing remove MaxiKonto account");
	createRemoveAccount();

    }

    /**
     * Add elements to account
     * @param kontoInfo - the infomation relevanto to the account
     */
    public void addElement(String kontoInfo) {
	super.addItems(kontoInfo);
	
    }
    
    /**
     * Remove elements from account at the specified index
     * @param index - the position in the list model
     */
    public void removeElementAt(int index) {
	super.myRemoveElementAt(index);
	
    }


}
