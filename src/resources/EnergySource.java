package resources;

public interface EnergySource {

    /**
     * Getter for this energy source's cost per energy unit produced.
     * Aimed at representing price, in cents per kwh.
     *
     * @return Cost per energy unit.
     */
    float getCostPerUnit();

    /**
     * Is this energy source renewable?
     *
     * @return whether this is renewable energy.
     */
    boolean isRenewable();
}
