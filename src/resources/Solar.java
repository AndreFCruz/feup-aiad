package resources;

public class Solar implements EnergySource {
    @Override
    public float getCostPerUnit() {
        return 10f;
    }
}
