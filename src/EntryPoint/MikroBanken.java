package EntryPoint;

import javax.swing.SwingUtilities;

import grafiskTerminal.BankGui;
import grafiskTerminal.SparKontoModel;

/**
 * Inneh�ller applikationens main-metod with listModel
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class MikroBanken {

    /**
     * Skapar en instans av klassen radorienterad.Terminal
     * 
     * @param args
     *            anv�nds inte
     */
    public static void main(String[] args) {

	SwingUtilities.invokeLater(new Runnable() {
	    public void run() {

		SparKontoModel sparKontoModel = SparKontoModel.getInstance();
		BankGui bankGui = BankGui.getInstance();

	    }
	});

    }
}
