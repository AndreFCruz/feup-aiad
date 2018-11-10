package agents;

import launchers.EnergyMarketLauncher;
import utils.GraphicSettings;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EcologicalConsumer extends Consumer {
    public EcologicalConsumer(EnergyMarketLauncher model, GraphicSettings graphicSettings, int energyConsumptionPerMonth) {
        super(model, graphicSettings, energyConsumptionPerMonth);
    }

    @Override
    protected List<Broker> orderBrokersByPreference(List<Broker> brokers) {
        Collections.sort(brokers,
                Comparator.comparingDouble(Broker::getPercentageOfRenewableEnergy).reversed()
        );

        return brokers.stream().filter(
                (Broker b) -> b.getAvailableMonthlyEnergyQuota() > this.getEnergyConsumptionPerMonth()
        ).collect(Collectors.toList());
    }

}
