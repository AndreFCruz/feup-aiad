package recorders;

import launchers.EnergyMarketLauncher;
import uchicago.src.sim.analysis.NumericDataSource;

public class SatisfiedConsumersDataSource implements NumericDataSource {

    private EnergyMarketLauncher worldModel;

    public SatisfiedConsumersDataSource(EnergyMarketLauncher em) {
        this.worldModel = em;
    }

    @Override
    public double execute() {
        return worldModel.getEnergyContractsConsumerBroker().size();
    }
}
