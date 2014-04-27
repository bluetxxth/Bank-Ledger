package bankVerksamhet;


/**
 * KontoFabrik skapar olika typer av konton. Endast kontofabriken
 * behöver engagera sig i vilken kontotyp det är frågan om, resten
 * av systemet behöver enbart hantera konton, utan att ta hänsyn
 * till vilken kontotyp det är frågan om.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KontoFabrik
{
    
    /**
     * Skapar ett konto av önskad typ
     * gabriels notes: Factory design pattern
     * @param typ typ av konto som ska skapas
     * @return referens till det skapade kontot
     */
    static Konto skapaKonto(int type)
    {
	Konto konto = null;
	
	switch(type){
	
	//Sparkonto
	case 0: 
	   // konto = SparKonto.getInstance();
	    konto = new SparKonto(0);
	    break;
	    
	//Maxikonto
	case 1:
	   // konto = MaxiKonto.getInstance();
	     konto = new MaxiKonto(1);
	    break;
	//Lonekonto
	case 2: 
	   // konto = LoneKonto.getInstance();
	    konto = new LoneKonto(2);
	    break;
	
	}
        
        return konto;
    } 
    
    /**
     * Remove account
     * @param type
     * @return
     */
    static Konto removeKonto(){
	Konto konto = null;
	
	return konto;
    }
    
}
