package agents;

import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;

/**
 * Only subscribes ONE Broker;
 * Some Consumers have higher inertia to changing Brokers;
 * Some Consumers prefer green energy;
 */
public class Consumer extends DFSearchAgent {

    private int energyConsumptionPerMonth;

    public Consumer(EnergyMarketLauncher model, GraphicSettings graphicSettings, int energyConsumptionPerMonth) {
        super(model, graphicSettings);
        this.setType(AgentType.BROKER);
        this.energyConsumptionPerMonth = energyConsumptionPerMonth;
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Consumer " + this.getLocalName() + " was created.");
    }

    public int getEnergyConsumptionPerMonth() {
        return energyConsumptionPerMonth;
    }

}
