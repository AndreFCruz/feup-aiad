package resources;

public class Hydro implements EnergySource {
    @Override
    public float getCostPerUnit() {
        return 8f;
    }

    @Override
    public boolean isRenewable() {
        return true;
    }
}
