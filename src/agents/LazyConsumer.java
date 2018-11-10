package agents;

import launchers.EnergyMarketLauncher;
import sajas.core.Agent;
import utils.GraphicSettings;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class LazyConsumer extends Consumer {
    public LazyConsumer(EnergyMarketLauncher model, GraphicSettings graphicSettings, int energyConsumptionPerMonth) {
        super(model, graphicSettings, energyConsumptionPerMonth);
    }

    @Override
    protected List<Broker> orderBrokersByPreference(List<Broker> brokers) {
        Collections.sort(brokers, Comparator.comparingDouble(
                (Broker b) -> distance(b, this)
        ));
        return brokers;
    }

    private static float distance(GenericAgent a1, GenericAgent a2) {
        return (float) Math.sqrt(
                Math.pow(a1.getX() - a2.getX(), 2) + Math.pow(a1.getY() - a2.getY(), 2)
        );
    }
}