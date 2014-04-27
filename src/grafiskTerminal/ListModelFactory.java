package grafiskTerminal;

/**
 * Class that emulates a list model factory which is responsible for creating
 * listModels of different types
 * 
 * @author Gabriel Nieva 2 dec 2013 12:05:25
 * 
 * 
 */
public class ListModelFactory {

    /**
     * Skapar ett konto av önskad typ Factory design pattern
     * 
     * @param typ
     *            typ av konto som ska skapas
     * @return referens till det skapade kontot
     */
    static ListModel skapaKontoLista(int type) {
	ListModel listModel = null;

	switch (type) {

	// Sparkonto
	case 0:
	    // konto = SparKonto.getInstance();
	    listModel = new SparKontoModel();
	    break;

	// Maxikonto
	case 1:
	    // konto = MaxiKonto.getInstance();
	    listModel = new MaxiKontoModel();
	    break;
	// Lonekonto
	case 2:
	    // konto = LoneKonto.getInstance();
	    listModel = new LoneKontoModel();
	    break;

	}

	return listModel;
    }

}
