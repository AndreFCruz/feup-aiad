package recorders;

import agents.Broker;
import agents.Consumer;
import agents.GenericAgent;
import launchers.EnergyMarketLauncher;
import uchicago.src.sim.analysis.DataSource;
import uchicago.src.sim.analysis.NumericDataSource;
import utils.EnergyContract;

public class ConsumerContractDataSource implements DataSource {

    private Consumer consumer;

    private Integer previousContractHash = null;

    public ConsumerContractDataSource(Consumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public Object execute() {
        EnergyContract currentContract = consumer.getEnergyContract();
        if (currentContract == null || (previousContractHash != null && currentContract.hashCode() == previousContractHash))
            return "";
        previousContractHash = currentContract.hashCode();
        return contractToString(currentContract);
    }

    private static String contractToString(EnergyContract currentContract) {
        String s = "" + currentContract.getEnergyCostPerUnit()
                + " " + ((Broker) currentContract.getEnergySupplier()).getPercentageOfRenewableEnergy()
                + " " + distance(currentContract.getEnergyClient(), currentContract.getEnergySupplier());
        if (s.contains("NaN"))
            return "";
        return s;
    }

    private static float distance(GenericAgent a1, GenericAgent a2) {
        return (float) Math.sqrt(
                Math.pow(a1.getX() - a2.getX(), 2) + Math.pow(a1.getY() - a2.getY(), 2)
        );
    }

}
