package grafiskTerminal;

import interfacePattern.Observer;
import interfacePattern.Subject;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingWorker;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bankVerksamhet.Transaktion;

/**
 * Class that emulates a list model it implements the observable interface
 * because
 * 
 * @author gabriel 30 okt 2013 17:04:44
 * 
 * 
 */
public class ListModel extends JPanel implements ListSelectionListener, Subject {

    private static final long serialVersionUID = 1L;
    private DefaultListModel<String> m_listModel;
    private JList<String> m_list;
    private JScrollPane m_scrollAccountList;
    static String m_accountSelected;
    private int m_index;
    private JButton m_removeAccount;
    private ArrayList<Observer> m_observers;
    private String[] m_tokenArray;
    private int m_kontoTyp;
    private int m_kontoNr;
    private double m_saldo;
    Transaktion banken = new Transaktion();
    private BankGui m_bankGui;
    private boolean m_interrupted;

    /**
     * Constructs an object of type ListModel
     */
    public ListModel() {
	super(new BorderLayout());
	// create a list model
	this.m_listModel = new DefaultListModel<String>();

	setupListGui();
	add(m_scrollAccountList, BorderLayout.CENTER);
	m_observers = new ArrayList<Observer>();

    }

    /**
     * Gets index
     * 
     * @return m_index an integer representing the index
     */
    public int getM_index() {
	return m_index;
    }

    /**
     * Sets up list GUI
     */
    public void setupListGui() {

	m_list = new JList<String>(m_listModel);
	// JList Selection Model
	m_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	m_list.setSelectedIndex(-1);
	m_list.addListSelectionListener(this);
	m_list.setVisibleRowCount(5);

	createScrollAccountList();
	createListCellRenderer();

    }

    /**
     * Method sends items to the parser read from a txt file
     */
    public void addItems(String str) {
	m_list.setSize(100, 50);
	this.m_listModel.addElement(str);
	if (m_listModel.isEmpty()) {
	    this.m_listModel.add(0, str);
	}

    }

    /**
     * remove all elements from list model
     */
    public void listRemoveAll() {

	// System.out.println("clear all items");
	if (!m_listModel.isEmpty()) {
	    this.m_listModel.removeAllElements();
	}
    }

    /**
     * Remove an element at a given index
     * @param index
     */
    public void myRemoveElementAt(int index) {
	m_listModel.removeElementAt(index);

    }

    /**
     * Gets the default list model
     * 
     * @return -m_listModel a Default list model
     */
    public DefaultListModel<String> getM_listModel() {
	return m_listModel;
    }

    /**
     * 
     * @author Gabriel 21 okt 2013 14:11:16
     * 
     * 
     */
    class MyCellRenderer extends JLabel implements ListCellRenderer<Object> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructs an object of type MyCellRenderer
	 */
	public MyCellRenderer() {
	    setOpaque(true);
	}

	/**
	 * Gets the list cell renderer
	 * 
	 * @param list
	 * @param value
	 * @param index
	 * @param isSelected
	 * @param cellHasFocus
	 * @return the component with the bnuilt in ListCellRenderer
	 */
	public Component getListCellRendererComponent(JList<?> list,
		Object value, int index, boolean isSelected,
		boolean cellHasFocus) {

	    setText(value.toString());

	    Color background;
	    Color foreground;

	    // check if this cell represents the current DnD drop location
	    JList.DropLocation dropLocation = list.getDropLocation();
	    if (dropLocation != null && !dropLocation.isInsert()
		    && dropLocation.getIndex() == index) {

		background = Color.BLUE;
		foreground = Color.WHITE;

	    }
	    // check if this cell is selected
	    else if (isSelected) {
		background = Color.RED;
		foreground = Color.WHITE;

		banken.setM_kontoTyp(m_kontoTyp);
		banken.setM_kontoNr(index);

		// System.out.println("kontoNr: " + banken.getM_kontoNr());
		// System.out.println("kontoTyp: " + banken.getM_kontoTyp());

		banken.setM_kontoTyp(m_kontoTyp);
		banken.setM_kontoNr(index);
		banken.aktiveraKonto(m_kontoNr, m_kontoTyp);

		// unselected, and not the DnD drop location
	    } else {
		background = Color.WHITE;
		foreground = Color.BLACK;
	    }
	    ;
	    setBackground(background);
	    setForeground(foreground);

	    return this;
	}
    }

    /**
     * Method gets an element at a specific index
     * 
     * @param index
     * @return
     */
    public String getElementAtIndex(int index) {

	String strOut = null;
	strOut = (String) m_listModel.getElementAt(index);
	return strOut;
    }

    /**
     * Method changes the selected feed and initiates the parsing on the
     * selected feed.
     * 
     * @author Gabriel Nieva
     * 
     */
    @Override
    public void valueChanged(ListSelectionEvent event) {

	m_index = m_list.getSelectedIndex();

	if (m_index != -1) {
	    m_accountSelected = m_listModel.getElementAt(m_index);
	}

	// get the radio buttons
	m_bankGui.getInstance().getM_depositJRadioButton().setEnabled(true);
	m_bankGui.getInstance().getM_withdrawJRadioButton().setEnabled(true);
	getTokenizedString();
	dataChanged();
	BankGui.getInstance().processObservers();
	// System.out.println("KontoInfo" + banken.kontoInfo());
    }

    /**
     * Tokenize strings and set instance variables for account number acc type
     * and saldo
     */
    public void getTokenizedString() {

	int tokens = 0;
	int i = 0;

	// System.out.println("m_accountSelected:: " + m_accountSelected);
	StringTokenizer stringTokenizer = new StringTokenizer(m_accountSelected);
	tokens = stringTokenizer.countTokens();

	// Token array initialization
	m_tokenArray = new String[tokens];

	// put the tokens in an array
	while (stringTokenizer.hasMoreTokens()) {
	    m_tokenArray[i] = stringTokenizer.nextToken();

	    // System.out.println("tokens: " + m_tokenArray[i]);

	    i++;
	}
	// KontoNummer
	m_kontoNr = Integer.parseInt(m_tokenArray[2]);
	// kontoTyp
	m_kontoTyp = Integer.parseInt(m_tokenArray[4]);
	// saldo
	m_saldo = Double.parseDouble(m_tokenArray[6]);
    }

    /**
     * Removes account from listModel
     * 
     * @author Gabriel Nieva
     * 
     */
    class RemoveAccountListener implements ActionListener {
	/**
	 * Method action listener to remove an account
	 */
	public void actionPerformed(ActionEvent event) {
	    int index = -1;

	    index = m_list.getSelectedIndex();

	    // System.out.println("selected index is:" + index);

	    try {

		if (((m_listModel.size() >= 0) && !(m_list.getSelectedIndex() == -1))) {
		    // remove elements from list Model - Gui
		    m_listModel.removeElementAt(index);
		    // m_feeds.remove(index);
		}

	    } catch (Exception ex) {
		JFrame frame = new JFrame();

		JOptionPane.showMessageDialog(frame, ex.getMessage());
		ex.printStackTrace();
	    }

	    if ((m_listModel.size() == 0)) {
		m_list.setSelectedIndex(-1);
	    }
	}
    }

    /**
     * gets the account selected
     * 
     * @return - m_accountSelected - the selected account
     */
    public String getM_accountSelected() {
	return m_accountSelected;
    }

    /**
     * Crete scrroll and put it on the list
     */
    public void createScrollAccountList() {
	m_scrollAccountList = new JScrollPane(m_list);
    }

    /**
     * Create remove account button listener
     */
    public void createRemoveAccount() {
	RemoveAccountListener removeAccountListener = new RemoveAccountListener();
	BankGui myGuiObject = BankGui.getInstance();
	m_removeAccount = myGuiObject.getM_removeAccountButton();
	m_removeAccount.setActionCommand("remove");
	m_removeAccount.addActionListener(removeAccountListener);
    }

    /**
     * create list cell renderer
     */
    public void createListCellRenderer() {
	// cell rendered for the behavior of the visited feed links
	m_list.setCellRenderer(new MyCellRenderer());
    }

    /**
     * Register observer
     */
    @Override
    public void registerObserver(Observer observer) {
	this.m_observers.add(observer);
    }

    /**
     * Remove observer
     */
    @Override
    public void removeObserver(Observer observer) {
	int index = m_observers.indexOf(observer);
	if (index >= 0) {
	    m_observers.remove(index);
	}
    }

    /**
     * Data changed should update the rows with the updated values
     */
    public void dataChanged() {
	notifyObserver();
    }

    /**
     * Notifies the registered observers of change
     */
    @Override
    public void notifyObserver() {
	for (int index = 0; index < m_observers.size(); index++) {

	    Observer observer = (Observer) m_observers.get(index);
	    observer.update(m_accountSelected, m_index, m_tokenArray,
		    m_kontoNr, m_kontoTyp, m_saldo);
	}
    }

}
