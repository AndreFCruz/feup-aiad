package resources;

/**
 * Models Fossil Fuels in the international market, price should move up/down according to demand.
 */
public class FossilFuels implements EnergySource {
    @Override
    public float getCostPerUnit() {
        return 11f;
    }
}
