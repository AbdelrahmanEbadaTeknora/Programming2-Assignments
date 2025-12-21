package Models;

/**
 * Represents the catalog of available games
 */
public class Catalog {
    private boolean current;        // True if there is an unfinished game
    private boolean allModesExist;  // True if at least one game per difficulty exists

    public Catalog(boolean current, boolean allModesExist) {
        this.current = current;
        this.allModesExist = allModesExist;
    }

    public boolean hasCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean hasAllModes() {
        return allModesExist;
    }

    public void setAllModesExist(boolean allModesExist) {
        this.allModesExist = allModesExist;
    }

    @Override
    public String toString() {
        return "Catalog{" +
                "current=" + current +
                ", allModesExist=" + allModesExist +
                '}';
    }
}
