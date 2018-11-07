package recorders;

import launchers.EnergyMarketLauncher;
import uchicago.src.sim.analysis.NumericDataSource;

public class SatisfiedConsumersRecorder extends GenericRecorder implements NumericDataSource {

    public SatisfiedConsumersRecorder(EnergyMarketLauncher em) {
        super(em);
    }

    @Override
    public double execute() {
        return worldModel.getEnergyContractsConsumerBroker().size();
    }
}
