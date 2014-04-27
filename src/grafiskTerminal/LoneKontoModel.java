package grafiskTerminal;

/**
 * Class that emulates a LoneKontoModel Based on a ListModel which uses a
 * Default List Model
 * 
 * @author Gabriel Nieva 2 dec 2013 12:02:25
 * 
 * 
 */
public class LoneKontoModel extends ListModel {

    private static LoneKontoModel m_uniqueInstance;

    /**
     * Constructs an object of type LoneKonto
     */
    public LoneKontoModel() {
	super();
    }

    /**
     * Gets a unique instance of a LoneKontoModel object
     * 
     * @return m_uniqueInstance - a unique instance of a LoneKontoModel object
     */
    public static LoneKontoModel getInstance() {

	if (m_uniqueInstance == null) {

	    m_uniqueInstance = new LoneKontoModel();

	}
	return m_uniqueInstance;
    }

    /**
     * Removes an account
     */
    public void removeAccount() {
	super.createRemoveAccount();
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
