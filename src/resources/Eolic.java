package resources;

public class Eolic implements EnergySource {
    @Override
    public float getCostPerUnit() {
        return 9f;
    }

    @Override
    public boolean isRenewable() {
        return true;
    }
}
