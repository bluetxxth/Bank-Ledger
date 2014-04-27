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
     * Konstructor för klass KontoRegister, utför inte något
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
     * Sätter in en referens till ett konto i registret
     * 
     * @param nyttKonto referensen som ska sättas in i registret
     */
    public void add(Konto nyttKonto)
    {
        listan.add(nyttKonto);
    }
   
    
    /**
     * Tar bort registrets referens till kontot
     * 
     * @param  kontot den referens som ska avlägsnas från registret
     * @return true om kontots referens fanns i registret och kunde tas bort, i annat fall false
     */
    boolean remove(Konto kontot)
    {
        return listan.remove(kontot);
    }
    
      
    /**
     * Returnerar true om inga konton är registrerade
     * 
     * @return true om registret är tomt, i annat fall false
     */
    boolean isEmpty()
    {
        return listan.size() == 0;
    }    
    

    /**
     * Returnerar en referens till det första kontot i registet
     * 
     * @return den första kontoreferensen i registret, om registret är tomt returneras null
     */
    Konto first()
    {   
        iteratorn = listan.iterator();
        
        if (iteratorn.hasNext())
            return (Konto)iteratorn.next();        
            
        return null;
    }


    /**
     * Returnerar en referens till nästa konto i registet
     * 
     * @return Nästa kontoreferens i registret returneras, om inget fler konto finns så returneras null
     */
    Konto next()
    {
        if (iteratorn.hasNext())
            return (Konto)iteratorn.next();        
        
        return null;
    }    
    
    
    /**
     * Returnerar en referens till det konto vars nummer är lika med kontoNr
     * 
     * @param kontoNr det sökta kontots kontonummer
     * @return en referens till det sökta kontot, om kontot inte finns så returneras null
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
