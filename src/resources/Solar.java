package resources;

public class Solar implements EnergySource {
    @Override
    public float getCostPerUnit() {
        return 10f;
    }

    @Override
    public boolean isRenewable() {
        return true;
    }
}
