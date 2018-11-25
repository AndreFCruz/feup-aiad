package recorders;

import launchers.EnergyMarketLauncher;
import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.NumericDataSource;

public class TypeOfConsumerRecorder extends GenericRecorder implements DataSource {

    public TypeOfConsumerRecorder(EnergyMarketLauncher em) {
        super(em);
    }

    @Override
    public Object execute() {
        return null;
    }
}
