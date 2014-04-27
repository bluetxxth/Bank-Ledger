package bankVerksamhet;
import java.util.*;

import dbPersistence.KontoMutator;

/**
 * Administrerar samtliga konton genom att lagra referenser till dem.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KontoRegister
{
    static List<Konto> listan = new LinkedList<Konto>();
    private Iterator<Konto> iteratorn;
    
    /**
     * Konstructor f�r klass KontoRegister, utf�r inte n�got
     */
    public KontoRegister()
    {
	
    }
    

    /**
     * 
     * @return
     */
    public static List<Konto> getListan() {
        return listan;
    }
    
    public int getListSize() {

	return listan.size();
	
    }
    

    /**
     * S�tter in en referens till ett konto i registret
     * 
     * @param nyttKonto referensen som ska s�ttas in i registret
     */
    public void add(Konto nyttKonto)
    {
        listan.add(nyttKonto);
    }
   
    
    /**
     * Tar bort registrets referens till kontot
     * 
     * @param  kontot den referens som ska avl�gsnas fr�n registret
     * @return true om kontots referens fanns i registret och kunde tas bort, i annat fall false
     */
    boolean remove(Konto kontot)
    {
        return listan.remove(kontot);
    }
    
      
    /**
     * Returnerar true om inga konton �r registrerade
     * 
     * @return true om registret �r tomt, i annat fall false
     */
    boolean isEmpty()
    {
        return listan.size() == 0;
    }    
    

    /**
     * Returnerar en referens till det f�rsta kontot i registet
     * 
     * @return den f�rsta kontoreferensen i registret, om registret �r tomt returneras null
     */
    Konto first()
    {   
        iteratorn = listan.iterator();
        
        if (iteratorn.hasNext())
            return (Konto)iteratorn.next();        
            
        return null;
    }


    /**
     * Returnerar en referens till n�sta konto i registet
     * 
     * @return N�sta kontoreferens i registret returneras, om inget fler konto finns s� returneras null
     */
    Konto next()
    {
        if (iteratorn.hasNext())
            return (Konto)iteratorn.next();        
        
        return null;
    }    
    
    
    /**
     * Returnerar en referens till det konto vars nummer �r lika med kontoNr
     * 
     * @param kontoNr det s�kta kontots kontonummer
     * @return en referens till det s�kta kontot, om kontot inte finns s� returneras null
     */
    Konto search(int kontoNr, int kontoTyp)
    {
        Konto ettKonto = first();

        while ((ettKonto != null) && (ettKonto.getKontoTyp() != kontoTyp)) {
            
            ettKonto = next();
        
        }

        while((ettKonto != null) && (ettKonto.getKontoNr() != kontoNr)) {
            
            ettKonto = next();
        }

        return ettKonto;
    }    
}
