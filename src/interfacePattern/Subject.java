package interfacePattern;

/**
 * Interface represents the Subject in the Observer design pattern
 * @author  Gabriel Nieva 2 dec 2013 12:10:15
 *
 *
 */
public interface Subject {

    /**
     * Registers observer
     * @param o - the observer being registered
     */
    public void registerObserver(Observer o);
    
    
    /**
     * Removes observer 
     * @param o - the observer in question
     */
    public void removeObserver(Observer o);
       
    /**
     * Notify ofbserver of any changes
     */
    public void notifyObserver();
     
    
}
