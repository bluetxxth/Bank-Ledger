package grafiskTerminal;

/**
 * Class that emulates the Spar Konto Model
 * @author  Gabriel Nieva 4 dec 2013 08:42:32
 *
 *
 */
public class SparKontoModel extends ListModel{


    private static final long serialVersionUID = 1L;
    
    static SparKontoModel m_instance;

    /**
     * Construct an object of type SparKontoModel
     */
    public SparKontoModel() {
	super();
    }

    /**
     * Get unique instance of SparKontoModel
     * 
     * @return m_instance - an object representing the unique instance of
     *         SparKontoModel
     */
    public static SparKontoModel getInstance() {

	m_instance = new SparKontoModel();

	return m_instance;
    }

    /**
     * Get the getSparListModelSize
     * 
     * @return an int representing the size
     */
    public int getSparListModelSize() {

	int size = 0;

	size = getM_listModel().size();

	return size;
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
