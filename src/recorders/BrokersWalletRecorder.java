package recorders;

import agents.Broker;
import launchers.EnergyMarketLauncher;
import uchicago.src.sim.analysis.NumericDataSource;

public abstract class BrokersWalletRecorder extends GenericRecorder implements NumericDataSource {

    Broker broker;

    BrokersWalletRecorder(EnergyMarketLauncher em, Broker b) {
        super(em);
        this.broker = b;
    }

    public abstract float getWalletBalance();

    @Override
    public double execute() {
        return getWalletBalance();
    }
}
