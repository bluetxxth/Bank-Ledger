package grafiskTerminal;

import java.util.List;

import interfacePattern.Observer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.logging.StreamHandler;

import javax.swing.*;

import dbPersistence.KontoAccessor;
import dbPersistence.KontoMutator;
import bankVerksamhet.Konto;
import bankVerksamhet.KontoRegister;
import bankVerksamhet.Transaktion;

/**
 * Class responsible for the GUI
 * 
 * @author Gabriel Nieva 2 dec 2013 09:34:51
 * 
 * 
 */
public class BankGui implements ActionListener, Observer, Runnable {

    // main frame
    private JFrame m_frame;

    // misc components
    private JComboBox m_accountSelectorComboBox;

    // menu
    private JMenu m_topMenu;
    private JMenuItem m_menuItemOpen;
    private JMenuItem m_menuItemSave;
    private JMenuBar m_menuBar;

    private JMenuItem m_menuItemAbout;
    private JMenuItem m_clearSpar;
    private JMenuItem m_clearMaxi;
    private JMenuItem m_clearLone;
    private JMenuItem m_clearAll;
    private JMenuItem m_clearTextEditor;
    private JMenuItem m_clearKontoLists;

    private JMenu menuFile;
    private JMenu menuOption;
    private JFileChooser fileChooser;

    // buttons
    private JButton m_addAccountButton;
    private JButton m_removeAccountButton;
    private JButton m_calculateInterestButton;
    private JButton m_okButton;
    private JButton m_activateAccountButton;
    private JButton m_deActivateAccountButton;

    private JRadioButton m_withdrawJRadioButton;
    private JRadioButton m_depositJRadioButton;

    // text
    private JTextArea m_textArea;
    private JTextField m_inputAmmount;
    private JTextField m_inputAccNoTextField;

    // panels
    private JPanel m_mainRightPanel;
    private JPanel m_accountListPanel;
    private JPanel m_outputPanel;
    private JPanel m_accountActionsPanel;
    private JPanel m_mainLeftPanel;
    private JPanel m_accountTransactionsPanel;
    private JPanel m_comboBoxPanel;
    private JPanel m_accActivationPanel;
    private JScrollPane m_scrollRegister;
    private KontoMutator m_kontoMutator;

    // Objects
    private Transaktion banken;

    // List
    private JList<String> m_list;
    private DefaultListModel<String> m_listModel;
    private KontoRegister m_kontoRegister;

    private ListModelFactory m_listModelFactory;

    // lists
    private SparKontoModel m_sparKontoList;
    private MaxiKontoModel m_maxiKontoList;
    private LoneKontoModel m_loneKontoList;

    private ListModel m_myGenericListModel;
    private int m_kontoTyp;
    private int m_kontoNummer;

    // for observer
    private String m_accountSelected;
    private String[] m_tokenArray;
    private int m_index;
    private int m_aktivtKonto;
    private LinkedList<Konto> m_llist;

    // counters
    private int m_dbMaxiKontoNummer;
    private int m_dbLoneKontoNummer;
    private int m_dbSparKontoNummer;

    // private int m_sparKontoCounter;

    // list manipulation database and non-database
    private boolean m_isLoaded = false;
    private boolean m_saldoChanged = false;
    private boolean m_removeAccountSelected = false;



    private boolean m_interrupted;

    // Guis unique instance
    private static BankGui m_bankGuiUniqueInstance;

    /**
     * Creates an object of type BankGUI
     */
    private BankGui() {

	// m_myGenericListModel = new ListModel();
	setupBankGui();
	registerEvents();
	m_listModelFactory = new ListModelFactory();
	banken = new Transaktion();
	m_kontoRegister = new KontoRegister();
	m_kontoMutator = new KontoMutator();
	m_llist = (LinkedList<Konto>) KontoRegister.getListan();

    }

    /**
     * Starts to listen for messages from other clients
     */
    public void startThread() {
	m_interrupted = false;
	new Thread(this).start();

    }

    @Override
    /**
     * run method of the runnable interface 
     */
    public void run() {

	while (!m_interrupted) {
	    try {
		Thread.sleep(500);
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }

	    try {
		if (banken.getM_kontoTyp() == 0) {
		    // Check if money deposited or removed from account
		    if (m_saldoChanged == true) {
			m_sparKontoList.removeElementAt(m_index);
			m_sparKontoList.addElement(banken.kontoInfo());
			// make a beep
			Toolkit.getDefaultToolkit().beep();
		    }
		}
		
		if (banken.getM_kontoTyp() == 1) {
		    // Check if money deposited or removed from account
		    if (m_saldoChanged == true) {
			m_maxiKontoList.removeElementAt(m_index);
			m_maxiKontoList.addElement(banken.kontoInfo());
			// make a beep
			Toolkit.getDefaultToolkit().beep();
		    }
		}
		
		if (banken.getM_kontoTyp() == 2) {
		    // Check if money deposited or removed from account
		    if (m_saldoChanged == true) {
			m_loneKontoList.removeElementAt(m_index);
			m_loneKontoList.addElement(banken.kontoInfo());
			// make a beep
			Toolkit.getDefaultToolkit().beep();
		    }
		}
	    } catch (Exception ex) {
		System.out.println("nothing to update");
	    }
	    m_saldoChanged = false;

	    // if remove account button is pressed
	    if (m_removeAccountSelected == true) {
		if (m_index > -1) {
		    m_sparKontoList.removeElementAt(m_index);
		    // m_index --;
		}
	    }
	    // set to false
	    m_removeAccountSelected = false;
	}
    }


    /**
     * Returns wether the account is selected or not
     * 
     * @return m_removeAccountSelected - true or false depending on whether the
     *         account is selected or not
     */
    public boolean isM_removeAccountSelected() {
	return m_removeAccountSelected;
    }

    /**
     * Sets the state of the remove account request to true or false
     * 
     * @param m_removeAccountSelected
     *            - true or false depending on wether the account is to be
     *            removed or not
     */
    public void setM_removeAccountSelected(boolean m_removeAccountSelected) {
	this.m_removeAccountSelected = m_removeAccountSelected;
    }

    /**
     * Get the MaxikontoAccountNumber that stems from the DB
     * 
     * @return - m_dbMaxiKontoNummer an int representing the
     *         MaxikontoAccountNumber which comes from the DB
     */
    public int getM_dbMaxiKontoNummer() {
	return m_dbMaxiKontoNummer;
    }

    /**
     * Get the m_dbLoneKontoNummer that stems from the DB
     * 
     * @return - m_dbLoneKontoNummer an int representing the m_dbLoneKontoNummer
     *         which comes from the DB
     */
    public int getM_dbLoneKontoNummer() {
	return m_dbLoneKontoNummer;
    }

    /**
     * Get the m_dbSparKontoNummer that stems from the DB
     * 
     * @return - m_dbSparKontoNummer an int representing the m_dbSparKontoNummer
     *         which comes from the DB
     */
    public int getM_dbSparKontoNummer() {
	return m_dbSparKontoNummer;
    }

    /**
     * Accesses the remove account button
     * 
     * @return A button which represents the removeAccountButton
     */
    public JButton getM_removeAccountButton() {
	return m_removeAccountButton;
    }

    /**
     * Gets info about whether the saldo has changed or not
     * 
     * @return m_ saldoChanged - true if changed false if not
     */
    public boolean isM_saldoChanged() {
	return m_saldoChanged;
    }

    /**
     * Set info on whether the saldo has changed or not
     * 
     * @param m_saldoChanged
     */
    public void setM_saldoChanged(boolean m_saldoChanged) {
	this.m_saldoChanged = m_saldoChanged;
    }

    /**
     * Constructor makes a unique instance of the gui
     * 
     * @return - m_bankGuiUniqueInstance the unique instance
     */
    public static BankGui getInstance() {

	if (m_bankGuiUniqueInstance == null) {

	    m_bankGuiUniqueInstance = new BankGui();
	}

	return m_bankGuiUniqueInstance;
    }

    /**
     * Create frame
     */
    public void createFrame() {
	m_frame = new JFrame();
	Dimension frameDimension = new Dimension(900, 600);
	m_frame.setSize(frameDimension);
	m_frame.setDefaultCloseOperation(m_frame.EXIT_ON_CLOSE);
	m_frame.setVisible(true);

    }

    /**
     * Create add account button
     */
    public void createAddAccountButton() {
	m_addAccountButton = new JButton("Add Account");
	Dimension d = new Dimension(10, 20);
	m_addAccountButton.setSize(d);
	m_addAccountButton.setVisible(true);

    }

    /**
     * Create remove account button
     */
    public void createRemoveAccountButton() {
	m_removeAccountButton = new JButton("Remove Account");
	Dimension d = new Dimension(10, 20);
	m_removeAccountButton.setSize(d);
	m_removeAccountButton.setEnabled(false);
	m_removeAccountButton.setVisible(true);

    }

    /**
     * Create remove account button
     */
    public void createActivateAccountButton() {
	m_activateAccountButton = new JButton("Activate");
	Dimension d = new Dimension(10, 20);
	m_activateAccountButton.setSize(d);
	m_activateAccountButton.setEnabled(false);
	m_activateAccountButton.setVisible(true);

    }

    /**
     * Create DeactivateAccount button
     */
    public void createDeActivateAccountButton() {
	m_deActivateAccountButton = new JButton("Deactivate");
	Dimension d = new Dimension(10, 20);
	m_deActivateAccountButton.setSize(d);
	m_deActivateAccountButton.setEnabled(false);
	m_deActivateAccountButton.setVisible(true);

    }

    /**
     * create createDepositMoneyRadioButton
     */
    public void createDepositMoneyRadioButton() {

	m_depositJRadioButton = new JRadioButton();
	m_depositJRadioButton.setText("Deposit");
	m_depositJRadioButton.setVisible(true);
	m_depositJRadioButton.setEnabled(false);
	m_depositJRadioButton.setSelected(false);
    }

    /**
     * Create createWithdrawMoneyRadioButton
     */
    public void createWithdrawMoneyRadioButton() {

	m_withdrawJRadioButton = new JRadioButton();
	m_withdrawJRadioButton.setText("Withdraw");
	m_withdrawJRadioButton.setVisible(true);
	m_withdrawJRadioButton.setEnabled(false);
	m_withdrawJRadioButton.setSelected(false);
    }

    /**
     * Gets a WithDraw radio button
     * 
     * @return m_withdrawJRadioButton - a radio button representing the withdraw
     *         radio button
     */
    public JRadioButton getM_withdrawJRadioButton() {
	return m_withdrawJRadioButton;
    }

    /**
     * Gets a m_depositJRadioButton
     * 
     * @return m_depositJRadioButton a radio button reponsible for the deposits.
     */
    public JRadioButton getM_depositJRadioButton() {
	return m_depositJRadioButton;
    }

    /**
     * Create calculateInterestButton
     */
    public void createCalculateInteresButton() {
	m_calculateInterestButton = new JButton("Interest");
	Dimension d = new Dimension(10, 20);
	m_calculateInterestButton.setSize(d);
	m_calculateInterestButton.setEnabled(false);
	m_calculateInterestButton.setVisible(true);

    }

    /**
     * Create okButton
     */
    public void createOkButton() {
	m_okButton = new JButton("OK");
	Dimension d = new Dimension(10, 20);
	m_okButton.setSize(d);
	m_okButton.setEnabled(false);
	m_okButton.setVisible(true);

    }

    /**
     * Create text area
     */
    public void createTextArea() {

	m_textArea = new JTextArea();
	Dimension d = new Dimension(400, 350);
	m_textArea.setSize(d);
	m_textArea.setEditable(false);
	m_textArea.setVisible(true);
    }

    /**
     * create input quantity field
     */
    public void createInputQuantityTextField() {

	m_inputAmmount = new JTextField("Enter qty");
	m_inputAmmount.setColumns(20);
	m_inputAmmount.setVisible(true);

	m_inputAmmount.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		m_inputAmmount.setText("");
	    }
	});
    }

    /**
     * create account number text field
     */
    public void createAccountNumberTextField() {

	m_inputAccNoTextField = new JTextField("Enter AccNo.");
	m_inputAccNoTextField.setColumns(10);
	m_inputAccNoTextField.setVisible(true);
	m_inputAccNoTextField.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		m_inputAccNoTextField.setText("");
	    }
	});
    }

    /*
     * create top menu
     */
    public void createTopMenu() {

	m_topMenu = new JMenu();
	m_topMenu.setVisible(true);

    }

    /**
     * create menu bar
     */
    public void createMenuBar() {
	m_menuBar = new JMenuBar();
	m_menuBar.setVisible(true);
    }

    /**
     * Create a combo box
     */
    public void createActivateAccountComboBox() {
	m_accountSelectorComboBox = new JComboBox();
	m_accountSelectorComboBox.setPrototypeDisplayValue("account type");
	m_accountSelectorComboBox.setVisible(true);
    }

    /**
     * Fills a combo box
     */
    public void fillComboBox() {

	m_accountSelectorComboBox.addItem("SparKonto");
	m_accountSelectorComboBox.addItem("MaxiKonto");
	m_accountSelectorComboBox.addItem("LoneKonto");

    }

    /**
     * create scroll for register window (text area)
     */
    public void createScrollRegister() {
	m_scrollRegister = new JScrollPane(m_textArea);
	m_scrollRegister
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
	m_scrollRegister.setVisible(true);

    }

    /**
     * Update the text area
     * 
     * @param text
     */
    public void updateTextAreaText(String text) {

	m_textArea.append(text + "\n");
    }

    /**
     * Method - to create the menu view
     */
    public void createMenu() {

	// a menu bar on the frame
	m_menuBar = new JMenuBar();

	// Adding first menu - FILE
	menuFile = new JMenu("File"); // a menu Item

	menuFile.setMnemonic(KeyEvent.VK_A);

	menuFile.getAccessibleContext().setAccessibleDescription(
		"Choose options");

	// menu bar
	m_menuBar.add(menuFile, BorderLayout.NORTH);

	// Adding Menu Item - OPEN
	m_menuItemOpen = new JMenuItem("Load from DB", KeyEvent.VK_T);

	m_menuItemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	m_menuItemOpen.getAccessibleContext().setAccessibleDescription(
		"Add feeds");
	// a file chooser to open savedfeed files
	fileChooser = new JFileChooser();

	menuFile.add(m_menuItemOpen);

	// Adding Menu Item - SAVE
	m_menuItemSave = new JMenuItem("Save to DB", KeyEvent.VK_T);
	m_menuItemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	m_menuItemSave.getAccessibleContext().setAccessibleDescription(
		"Add feeds");
	menuFile.add(m_menuItemSave);
	// a file chooser to open savedfeed files
	fileChooser = new JFileChooser();

	// Adding menu OPTIONS
	menuOption = new JMenu("Options");
	menuOption.setMnemonic(KeyEvent.VK_A);
	menuOption.getAccessibleContext().setAccessibleDescription(
		"Choose options");
	m_menuBar.add(menuOption, BorderLayout.NORTH);

	// Adding Menu Item - Clear spar konto list
	m_clearSpar = new JMenuItem("Clear Spar accounts", KeyEvent.VK_T);
	m_clearSpar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	m_clearSpar.getAccessibleContext().setAccessibleDescription(
		"Clear feed list");
	menuOption.add(m_clearSpar);

	// Adding Menu Item - Clear maxi konto list
	m_clearMaxi = new JMenuItem("Clear Maxi accounts", KeyEvent.VK_T);
	m_clearMaxi.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	m_clearMaxi.getAccessibleContext().setAccessibleDescription(
		"Clear feed list");
	menuOption.add(m_clearMaxi);

	// Adding Menu Item - Clear lone konto list
	m_clearLone = new JMenuItem("Clear Lone accounts", KeyEvent.VK_T);
	m_clearLone.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	m_clearLone.getAccessibleContext().setAccessibleDescription(
		"Clear feed list");
	menuOption.add(m_clearLone);

	// Adding Menu Item - Clear all lists
	m_clearAll = new JMenuItem("Clear all account lists", KeyEvent.VK_T);
	m_clearAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	m_clearAll.getAccessibleContext().setAccessibleDescription(
		"Clear feed list");
	menuOption.add(m_clearAll);

	// Add clear editor Pane
	m_clearTextEditor = new JMenuItem("Clear Editor Panel", KeyEvent.VK_T);
	m_clearTextEditor.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,
		ActionEvent.ALT_MASK));
	m_clearTextEditor.getAccessibleContext().setAccessibleDescription(
		"Clear feed list");
	menuOption.add(m_clearTextEditor);

	// Add about section
	m_menuItemAbout = new JMenuItem("About ", KeyEvent.VK_1);
	m_menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
		ActionEvent.ALT_MASK));
	menuOption.add(m_menuItemAbout);

	// Add clear lists section
	m_clearKontoLists = new JMenuItem("Clear Lists ", KeyEvent.VK_1);
	m_menuItemAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2,
		ActionEvent.ALT_MASK));
	menuOption.add(m_clearKontoLists);
    }

    /**
     * Create all components
     */
    public void createAll() {

	// create compontents
	createFrame();

	// menu
	createTopMenu();
	createMenuBar();
	createMenu();

	// Account Operations
	createAddAccountButton();
	createRemoveAccountButton();
	createActivateAccountButton();
	createDeActivateAccountButton();
	createActivateAccountComboBox();
	createAccountNumberTextField();

	// Account transactions
	createDepositMoneyRadioButton();
	createWithdrawMoneyRadioButton();
	createCalculateInteresButton();
	createOkButton();
	createInputQuantityTextField();

	// output
	createTextArea();

	// scroll
	createScrollRegister();

	// combo boxes
	fillComboBox();
    }

    /**
     * Set up the GUI
     */
    public void setupBankGui() {

	int rgap = 5;
	int lgap = 5;

	m_sparKontoList = new SparKontoModel();
	m_sparKontoList.setVisible(false);
	m_maxiKontoList = new MaxiKontoModel();
	m_maxiKontoList.setVisible(false);
	m_loneKontoList = new LoneKontoModel();
	m_loneKontoList.setVisible(false);

	// set up all components
	createAll();

	// create main content panel
	JPanel contentPanel = new JPanel(new FlowLayout());
	contentPanel.setOpaque(true);
	contentPanel.setBackground(Color.LIGHT_GRAY);
	contentPanel.setBorder(BorderFactory.createEmptyBorder());
	contentPanel.setLayout(new BorderLayout(lgap, rgap));
	contentPanel.setVisible(true);

	// create list panel
	m_accountListPanel = new JPanel(new GridLayout(1, 1));
	m_accountListPanel.setOpaque(true);
	m_accountListPanel.setBackground(Color.WHITE);
	m_accountListPanel.setBorder(BorderFactory
		.createTitledBorder("Account List"));

	this.m_listModel = new DefaultListModel<String>();
	m_list = new JList<String>(m_listModel);

	m_accountSelectorComboBox.setSelectedIndex(-1);

	m_accountListPanel.setVisible(true);

	// create output panel
	m_outputPanel = new JPanel(new GridLayout(1, 1));
	m_outputPanel.setOpaque(true);
	m_outputPanel.setBackground(Color.WHITE);
	m_outputPanel.setForeground(Color.BLACK);

	// add text area
	// m_outputPanel.add(m_textArea);
	m_outputPanel.add(m_scrollRegister);

	// set up the output panel
	m_mainRightPanel = new JPanel(new GridLayout(2, 1));
	m_mainRightPanel.setOpaque(true);
	m_mainRightPanel.setBackground(Color.WHITE);
	m_mainRightPanel.setBorder(BorderFactory.createTitledBorder("output"));
	m_mainRightPanel.setSize(200, 150);
	m_mainRightPanel.setVisible(true);

	// add output and list panels
	m_mainRightPanel.add(m_outputPanel);

	m_mainRightPanel.add(m_accountListPanel);

	// set account actions panel
	m_accountActionsPanel = new JPanel();
	m_accountActionsPanel.setOpaque(true);
	m_accountActionsPanel.setBackground(Color.WHITE);
	m_accountActionsPanel.setBorder(BorderFactory
		.createTitledBorder("Actions"));

	m_accountActionsPanel.setVisible(true);

	// Combo box panel
	m_comboBoxPanel = new JPanel();
	m_comboBoxPanel.setOpaque(true);
	m_comboBoxPanel.setBackground(Color.WHITE);
	m_comboBoxPanel.setBorder(BorderFactory
		.createTitledBorder("Account type"));

	m_comboBoxPanel.add(m_accountSelectorComboBox);
	m_comboBoxPanel.setVisible(true);

	// Account activation panel
	m_accActivationPanel = new JPanel(new FlowLayout());
	m_accActivationPanel.setOpaque(true);
	m_accActivationPanel.setBackground(Color.WHITE);
	m_accActivationPanel.setBorder(BorderFactory
		.createTitledBorder("Account activation"));

	m_accActivationPanel.add(m_activateAccountButton, BorderLayout.EAST);
	m_accActivationPanel.add(m_deActivateAccountButton, BorderLayout.EAST);
	m_accActivationPanel.add(m_inputAccNoTextField, BorderLayout.WEST);

	m_accActivationPanel.setVisible(true);

	// left content panel
	m_mainLeftPanel = new JPanel(new GridLayout(3, 1));
	m_mainLeftPanel.setOpaque(true);
	m_mainLeftPanel.setBackground(Color.WHITE);
	m_mainLeftPanel.setBorder(BorderFactory
		.createTitledBorder("Account Operations"));
	// m_mainLeftPanel.setSize(200, 200);

	m_mainLeftPanel.add(m_accountActionsPanel);
	m_mainLeftPanel.add(m_accActivationPanel);
	m_mainLeftPanel.add(m_comboBoxPanel);
	m_mainLeftPanel.setVisible(true);

	// buttons
	m_accountActionsPanel.add(m_addAccountButton, new BorderLayout());
	m_accountActionsPanel.add(m_removeAccountButton);

	// combo box
	// m_accountActionsPanel.add(m_accountSelectorComboBox);

	// set account transactions panel
	m_accountTransactionsPanel = new JPanel();
	m_accountTransactionsPanel.setOpaque(true);
	m_accountTransactionsPanel.setBackground(Color.WHITE);
	m_accountTransactionsPanel.setBorder(BorderFactory
		.createTitledBorder("Transactions"));

	// radio buttons
	m_accountTransactionsPanel.add(m_withdrawJRadioButton);
	m_accountTransactionsPanel.add(m_depositJRadioButton);

	// textField
	m_accountTransactionsPanel.add(m_inputAmmount);

	// ok button
	m_accountTransactionsPanel.add(m_okButton);

	// calculate interest button
	m_accountTransactionsPanel.add(m_calculateInterestButton);

	// add all panels to contentPanel
	contentPanel.add(m_menuBar, BorderLayout.NORTH);
	contentPanel.add(m_mainRightPanel, BorderLayout.CENTER);
	contentPanel.add(m_mainLeftPanel, BorderLayout.WEST);
	contentPanel.add(m_accountTransactionsPanel, BorderLayout.SOUTH);

	// set the content panel on the frame
	m_frame.setContentPane(contentPanel);
	m_frame.setVisible(true);

    }

    /**
     * clear all list models
     */
    public void clearLists() {

	m_llist = (LinkedList<Konto>) m_kontoRegister.getListan();
	//System.out.println("m_llist.size()" + m_llist.size());
	for (Konto aKonto : m_llist) {
	    if (aKonto.getKontoTyp() == 0) {
		// remove all items in sparkonto
		this.m_sparKontoList.listRemoveAll();
	    }
	    if (aKonto.getKontoTyp() == 1) {
		// remove all items in maxikonto
		this.m_sparKontoList.listRemoveAll();
	    }
	    if (aKonto.getKontoTyp() == 2) {
		// remove all items in lonekonto
		this.m_sparKontoList.listRemoveAll();
	    }
	}
    }

    /**
     * Register action listeners
     */
    public void registerEvents() {

	// add listeners for operation buttons
	m_addAccountButton.addActionListener(this);
	m_removeAccountButton.addActionListener(this);
	m_okButton.addActionListener(this);
	m_calculateInterestButton.addActionListener(this);
	m_withdrawJRadioButton.addActionListener(this);
	m_depositJRadioButton.addActionListener(this);
	m_activateAccountButton.addActionListener(this);
	m_accountSelectorComboBox.addActionListener(this);
	m_deActivateAccountButton.addActionListener(this);
	m_menuItemOpen.addActionListener(this);
	m_menuItemSave.addActionListener(this);
	m_clearAll.addActionListener(this);
	m_clearSpar.addActionListener(this);
	m_clearMaxi.addActionListener(this);
	m_clearLone.addActionListener(this);
	m_clearTextEditor.addActionListener(this);
	m_clearKontoLists.addActionListener(this);
    }

    /**
     * Provides behavior for interface
     */
    @Override
    public void actionPerformed(ActionEvent event) {

	int kontoType = -1;
	int kontoNr = -1;

	JFrame frame = new JFrame();

	// Set SparKonto visible
	if (m_accountSelectorComboBox.getSelectedIndex() == 0) {

	    m_accountListPanel.removeAll();
	    m_accountListPanel.add(m_sparKontoList, BorderLayout.EAST);
	    m_sparKontoList.setVisible(true);
	    m_accountListPanel.add(m_sparKontoList, BorderLayout.EAST);

	    m_sparKontoList.setVisible(true);
	    m_maxiKontoList.setVisible(false);
	    m_loneKontoList.setVisible(false);

	}

	// Create SparKonto
	if (event.getSource() == m_addAccountButton
		&& m_accountSelectorComboBox.getSelectedIndex() == 0) {

	    try {

		banken.skapaKonto(m_kontoTyp);

		// Activate operations and activation buttons
		if (m_removeAccountButton.isEnabled() == false
			&& m_activateAccountButton.isEnabled() == false
			&& m_deActivateAccountButton.isEnabled() == false
			&& m_withdrawJRadioButton.isEnabled() == false
			&& m_depositJRadioButton.isEnabled() == false) {

		    m_removeAccountButton.setEnabled(true);
		    m_activateAccountButton.setEnabled(true);
		    m_deActivateAccountButton.setEnabled(true);
		}

		// Check if loaded from DB
		if (m_isLoaded == true) {

		    m_dbSparKontoNummer++;
		}

	    } catch (Exception ex) {
		frame = new JFrame();
		JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
	    } finally {

		String text = banken.kontoInfo();
		Konto ettKonto = banken.getAktivtKonto();
		int myKontoNummer = ettKonto.getKontoNr();
		int myKontoType = ettKonto.getKontoTyp();
		double myKontoSaldo = ettKonto.getSaldo();
		String myKontoNummerString = String.valueOf(myKontoNummer);
		String myKontoTypString = String.valueOf(myKontoType);
		String myKontoSaldoString = String.valueOf(myKontoSaldo);
		m_textArea.append("Skapar" + text + "\n");

		String strInfo = "Konto nummer: " + myKontoNummerString + " "
			+ "- " + myKontoTypString + " " + "saldo: "
			+ myKontoSaldoString;

		// populate sparKontoList
		m_sparKontoList.addItems(strInfo);
		m_sparKontoList.setVisible(true);
		m_accountListPanel.add(m_sparKontoList, BorderLayout.EAST);
		m_sparKontoList.setVisible(true);
	    }
	}

	// Set MaxiKonto visible
	if (m_accountSelectorComboBox.getSelectedIndex() == 1) {
	    m_accountListPanel.removeAll();

	    m_accountListPanel.add(m_maxiKontoList, BorderLayout.EAST);
	    m_sparKontoList.setVisible(false);
	    m_maxiKontoList.setVisible(true);
	    m_loneKontoList.setVisible(false);

	}

	// Create MaxiKonto
	if (event.getSource() == m_addAccountButton
		&& m_accountSelectorComboBox.getSelectedIndex() == 1) {

	    try {
		banken.skapaKonto(m_kontoTyp);

		// Activate operations and activation buttons
		if (m_removeAccountButton.isEnabled() == false
			&& m_activateAccountButton.isEnabled() == false
			&& m_deActivateAccountButton.isEnabled() == false
			&& m_withdrawJRadioButton.isEnabled() == false
			&& m_depositJRadioButton.isEnabled() == false) {

		    m_removeAccountButton.setEnabled(true);
		    m_activateAccountButton.setEnabled(true);
		    m_deActivateAccountButton.setEnabled(true);

		}
		// check if loaded from db
		if (m_isLoaded == true) {

		    m_dbMaxiKontoNummer++;
		}

	    } catch (Exception ex) {
		frame = new JFrame();
		JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);
	    } finally {

		String text = banken.kontoInfo();
		m_textArea.append("Skapar" + text + "\n");

		Konto ettKonto = banken.getAktivtKonto();

		int myKontoNummer = ettKonto.getKontoNr();
		int myKontoType = ettKonto.getKontoTyp();
		double myKontoSaldo = ettKonto.getSaldo();

		String myKontoNummerString = String.valueOf(myKontoNummer);
		String myKontoTypString = String.valueOf(myKontoType);
		String myKontoSaldoString = String.valueOf(myKontoSaldo);

		String strInfo = "Konto nummer: " + myKontoNummerString + " "
			+ "- " + myKontoTypString + " " + "saldo: "
			+ myKontoSaldoString;

		m_maxiKontoList.addItems(strInfo);
		m_maxiKontoList.setVisible(true);

		m_accountListPanel.add(m_maxiKontoList, BorderLayout.EAST);
		m_maxiKontoList.setVisible(true);
	    }
	}

	// Set LoneKonto visible
	if (m_accountSelectorComboBox.getSelectedIndex() == 2) {
	    m_accountListPanel.removeAll();

	    m_accountListPanel.add(m_loneKontoList, BorderLayout.EAST);
	    m_sparKontoList.setVisible(false);
	    m_maxiKontoList.setVisible(false);
	    m_loneKontoList.setVisible(true);

	}

	//Create LoneKoknto
	if (event.getSource() == m_addAccountButton
		&& m_accountSelectorComboBox.getSelectedIndex() == 2) {

	    try {
		banken.skapaKonto(m_kontoTyp);

		// Activate operations and activation buttons
		if (m_removeAccountButton.isEnabled() == false
			&& m_activateAccountButton.isEnabled() == false
			&& m_deActivateAccountButton.isEnabled() == false
			&& m_withdrawJRadioButton.isEnabled() == false
			&& m_depositJRadioButton.isEnabled() == false) {

		    m_removeAccountButton.setEnabled(true);
		    m_activateAccountButton.setEnabled(true);
		    m_deActivateAccountButton.setEnabled(true);
		}
		// check if loaded from DB
		if (m_isLoaded == true) {

		    m_dbLoneKontoNummer++;
		}

	    } catch (Exception ex) {
		frame = new JFrame();
		JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);
	    } finally {

		String text = banken.kontoInfo();
		m_textArea.append("Skapar" + text + "\n");
		Konto ettKonto = banken.getAktivtKonto();
		int myKontoNummer = ettKonto.getKontoNr();
		int myKontoType = ettKonto.getKontoTyp();
		double myKontoSaldo = ettKonto.getSaldo();
		String myKontoNummerString = String.valueOf(myKontoNummer);
		String myKontoTypString = String.valueOf(myKontoType);
		String myKontoSaldoString = String.valueOf(myKontoSaldo);

		String strInfo = "Konto nummer: " + myKontoNummerString + " "
			+ "- " + myKontoTypString + " " + "saldo: "
			+ myKontoSaldoString;

		// populate sparKontoList
		m_loneKontoList.addItems(strInfo);
		m_accountListPanel.add(m_loneKontoList, BorderLayout.EAST);
		m_loneKontoList.setVisible(true);

	    }
	}

	// removing

	// Remove SparKonto
	if ((event.getSource() == m_removeAccountButton)
		&& (m_accountSelectorComboBox.getSelectedIndex() == 0)) {

	    // set active account
	    banken.aktiveraKonto(m_kontoNummer, m_kontoTyp);
	    // m_sparKontoList.createRemoveAccount();

	    m_removeAccountSelected = true; 

	    // start updating thread
	    this.startThread();

	    if (banken.harAktivtKonto()) {

		try {

		    kontoNr = banken.getM_kontoNr();
		    kontoType = m_accountSelectorComboBox.getSelectedIndex();
		    banken.taBortKonto(m_kontoTyp, kontoNr);

		} catch (Exception ex) {

		    frame = new JFrame();
		    JOptionPane.showMessageDialog(frame, ex.getCause(),
			    "Error", JOptionPane.ERROR_MESSAGE);
		    ex.printStackTrace();
		} finally {
		    m_textArea.append("Removing: " + banken.kontoInfo() + "\n");

		}
	    }
	}

	// Remove MaxiKonto
	if (event.getSource() == m_removeAccountButton
		&& m_accountSelectorComboBox.getSelectedIndex() == 1) {

	    // set active account
	    banken.aktiveraKonto(m_kontoNummer, m_kontoTyp);
	    m_maxiKontoList.removeAccount();

	    if (banken.harAktivtKonto()) {

		try {

		    kontoNr = banken.getM_kontoNr();
		    banken.taBortKonto(m_kontoTyp, kontoNr);

		} catch (Exception ex) {

		    frame = new JFrame();
		    JOptionPane.showMessageDialog(frame, ex.getMessage(),
			    "Error", JOptionPane.ERROR_MESSAGE);
		} finally {

		    m_textArea.append("Removing MaxiKonto" + banken.kontoInfo()
			    + "\n");
		}
	    }
	}

	// Remove LoneKonto
	if (event.getSource() == m_removeAccountButton
		&& m_accountSelectorComboBox.getSelectedIndex() == 2) {

	    // set active account
	    banken.aktiveraKonto(m_kontoNummer, m_kontoTyp);

	    // remove from listModel
	    m_loneKontoList.removeAccount();

	    if (banken.harAktivtKonto()) {

		try {

		    kontoNr = banken.getM_kontoNr();
		    banken.taBortKonto(m_kontoTyp, kontoNr);

		} catch (Exception ex) {

		    frame = new JFrame();
		    JOptionPane.showMessageDialog(frame, ex.getMessage(),
			    "Error", JOptionPane.ERROR_MESSAGE);
		} finally {

		    m_textArea.append("Removing LoneKonto" + banken.kontoInfo()
			    + "\n");
		}
	    }
	}

	// Transactions

	// radioButtons
	// disable radio button deposit if withdraw button is selected
	if (event.getSource() == m_withdrawJRadioButton) {
	    m_depositJRadioButton.setSelected(false);
	}

	// disable radio button withdraw if deposit is selected
	if (event.getSource() == m_depositJRadioButton) {
	    m_withdrawJRadioButton.setSelected(false);
	}

	// radioButton witdraw
	if (event.getSource() == m_withdrawJRadioButton) {
	    // Activate transaction buttons
	    if (m_okButton.isEnabled() == false
		    && m_calculateInterestButton.isEnabled() == false) {
		m_okButton.setEnabled(true);
		m_calculateInterestButton.setEnabled(true);
	    }
	}
	// radioButton deposit
	if (event.getSource() == m_depositJRadioButton) {
	    // Activate transaction buttons
	    if (m_okButton.isEnabled() == false
		    && m_calculateInterestButton.isEnabled() == false) {
		m_okButton.setEnabled(true);
		m_calculateInterestButton.setEnabled(true);
	    }
	}

	// deposit
	if (event.getSource() == m_okButton
		&& m_depositJRadioButton.isSelected()) {

	    // pertaining listModel
	    // set active account
	    banken.aktiveraKonto(m_kontoNummer, m_kontoTyp);

	    // check if konto active
	    if (banken.harAktivtKonto()) {

		try {
		    int belopp = Integer.parseInt(m_inputAmmount.getText());
		    //System.out.println("belopp at BankGui " + belopp);

		    banken.sättIn(belopp);

		    m_saldoChanged = true;

		    this.startThread();

		} catch (Exception ex) {

		    frame = new JFrame();
		    JOptionPane.showMessageDialog(frame, ex.getMessage(),
			    "Error", JOptionPane.ERROR_MESSAGE);
		    m_inputAmmount.requestFocusInWindow();

		}

		finally {

		    m_textArea.append("Depositing funds" + "on account "
			    + banken.kontoInfo() + "\n");

		}
	    }
	}

	// withdraw
	if (event.getSource() == m_okButton
		&& m_withdrawJRadioButton.isSelected()) {

//	    System.out.println("Kontonummer: " + m_kontoNummer);
//	    System.out.println("KontoTyp: " + m_kontoTyp);
	    // set active account
	    banken.aktiveraKonto(m_kontoNummer, m_kontoTyp);

	    if (banken.harAktivtKonto()) {

		try {

		    int belopp = Integer.parseInt(m_inputAmmount.getText());
		    banken.withdrawFromAccount(belopp, m_kontoNummer,
			    m_kontoTyp);

		    m_saldoChanged = true;

		} catch (Exception ex) {
		    frame = new JFrame();
		    JOptionPane.showMessageDialog(frame, ex.getMessage(),
			    "Error", JOptionPane.ERROR_MESSAGE);
		    m_inputAmmount.requestFocusInWindow();

		} finally {

		    m_textArea.append("Withdrawing funds" + "from account "
			    + banken.kontoInfo() + " " + banken.getM_strOut()
			    + "\n");
		}
	    }
	}

	// calculate interest
	if (event.getSource() == m_calculateInterestButton) {

	    double interest = 0;

	    interest = banken.calculateInterest();
	    String interestString = String.valueOf(interest);

	    m_textArea.append("Calculating interest: " + interestString + "\n");

	}

	// activate account
	if (event.getSource() == m_activateAccountButton
		&& !(m_inputAccNoTextField.getText().equals(null))) {

	    try {

		kontoNr = Integer.parseInt(m_inputAccNoTextField.getText());
		banken.aktiveraKonto(kontoNr, m_kontoTyp);

	    } catch (Exception ex) {

		frame = new JFrame();
		JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);

	    } finally {
		m_textArea.append(banken.getM_strOut() + "\n");
		m_inputAccNoTextField.requestFocusInWindow();
	    }
	}

	// Deactivate Account
	if (event.getSource() == m_deActivateAccountButton
		&& !(m_inputAccNoTextField.getText().equals(null))) {

	    try {
		kontoNr = Integer.parseInt(m_inputAccNoTextField.getText());

		banken.inaktiveraKonto(kontoNr, m_kontoTyp);
	    } catch (Exception ex) {
		frame = new JFrame();
		JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error",
			JOptionPane.ERROR_MESSAGE);

	    } finally {

		m_textArea.append(banken.getM_strOut() + "\n");
		m_inputAccNoTextField.requestFocusInWindow();
	    }
	}

	// combo box
	if (event.getSource() == m_accountSelectorComboBox) {

	    if (m_accountSelectorComboBox.getSelectedIndex() == 0) {

		//System.out.println("selected sparkonto");

		m_kontoTyp = 0;
	    }

	    if (m_accountSelectorComboBox.getSelectedIndex() == 1) {

		//System.out.println("selected maxikonto");
		m_kontoTyp = 1;
	    }

	    if (m_accountSelectorComboBox.getSelectedIndex() == 2) {

		//System.out.println("selected lonekonto");
		m_kontoTyp = 2;
	    }
	}

	// Handling save menu item action
	if (event.getSource() == m_menuItemSave) {

	    updateTextAreaText("saving to database");

	    try {

		KontoMutator kontoMutator = new KontoMutator();

		kontoMutator.addDataToTables();

	    } catch (Exception e) {

		e.printStackTrace();
	    }
	}

	// Handling open menu item action
	if (event.getSource() == m_menuItemOpen) {

	    m_sparKontoList.listRemoveAll();
	    m_maxiKontoList.listRemoveAll();
	    m_loneKontoList.listRemoveAll();

	    updateTextAreaText("loading from database");

	    List<Konto> sparKontoList = null;
	    List<Konto> maxiKontoList = null;
	    List<Konto> loneKontoList = null;

	    clearLists();
	    m_llist.clear();

	    m_isLoaded = true;

	    KontoAccessor kontoAccessor = new KontoAccessor();

	    try {

		sparKontoList = kontoAccessor.createSparKontoList();
		maxiKontoList = kontoAccessor.createLoneKontoList();
		loneKontoList = kontoAccessor.createMaxiKontoList();

		// Activate m_removeAccountButton
		if (sparKontoList.size() != 0 || maxiKontoList.size() != 0
			|| loneKontoList.size() != 0) {
		    m_removeAccountButton.setEnabled(true);
		}

		// sparkonto
		for (Konto sparKonto : sparKontoList) {

		    if (sparKonto.getKontoTyp() == 0) {

			m_dbSparKontoNummer = sparKonto.getKontoNr();

			//System.out.println("m_dbSparKontoNummer"+ m_dbSparKontoNummer);
			m_sparKontoList.addItems("Konto nummer: "
				+ sparKonto.getKontoNr() + " " + "- "
				+ sparKonto.getKontoTyp() + " " + "saldo: "
				+ sparKonto.getSaldo());
		    }
		}

		// maxkonto
		for (Konto maxKonto : maxiKontoList) {

		    if (maxKonto.getKontoTyp() == 1) {

			m_dbMaxiKontoNummer = maxKonto.getKontoNr();

			//System.out.println("m_dbMaxiKontoNummer" + m_dbMaxiKontoNummer);

			m_maxiKontoList.addItems("Konto nummer: "
				+ maxKonto.getKontoNr() + " " + "- "
				+ maxKonto.getKontoTyp() + " " + "saldo: "
				+ maxKonto.getSaldo());
		    }
		}
		// lonekonto
		for (Konto loneKonto : loneKontoList) {

		    if (loneKonto.getKontoTyp() == 2) {

			m_dbLoneKontoNummer = loneKonto.getKontoNr();

			//System.out.println("m_dbLoneKontoNummer" + m_dbLoneKontoNummer);
			m_loneKontoList.addItems("Konto nummer: "
				+ loneKonto.getKontoNr() + " " + "- "
				+ loneKonto.getKontoTyp() + " " + "saldo: "
				+ loneKonto.getSaldo());
		    }
		}
	    } catch (Exception e) {

		e.printStackTrace();
	    }
	}

	// Clear all accounts
	if (event.getSource() == m_clearAll) {

	    try {
		m_kontoMutator.deleteAll();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	// Clear all accounts
	if (event.getSource() == m_clearSpar) {

	    try {
		m_kontoMutator.deleteSparKontos();
	    } catch (Exception e) {

		e.printStackTrace();
	    }
	}

	// Clear all accounts
	if (event.getSource() == m_clearMaxi) {

	    try {
		m_kontoMutator.deleteMaxiKontos();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}

	// Clear all accounts
	if (event.getSource() == m_clearLone) {

	    try {
		m_kontoMutator.deleteLoneKontos();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	// clear text editor
	if (event.getSource() == m_clearTextEditor) {

	    m_textArea.setText("");
	}

	if (event.getSource() == m_clearKontoLists) {

	    m_llist.clear();
	    m_sparKontoList.listRemoveAll();
	    m_maxiKontoList.listRemoveAll();
	    m_loneKontoList.listRemoveAll();
	}
    }

    /**
     * Process all observers
     */
    public void processObservers() {

	Observer observer1 = BankGui.getInstance();
	Observer observer2 = BankGui.getInstance();
	Observer observer3 = BankGui.getInstance();

	m_sparKontoList.registerObserver(observer1);
	m_maxiKontoList.registerObserver(observer2);
	m_loneKontoList.registerObserver(observer3);

    }

    /**
     * Updates data passed from listModel
     */
    @Override
    public void update(String accountSelected, int index, String[] tokenArray,
	    int kontoNummer, int kontoTyp, double saldo) {

	this.m_accountSelected = accountSelected;
	this.m_index = index;
	this.m_tokenArray = tokenArray;
	this.m_kontoNummer = kontoNummer;
	this.m_kontoTyp = kontoTyp;
    }
}
