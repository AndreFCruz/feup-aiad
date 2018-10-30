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

    public Consumer(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);
        this.setType(AgentType.BROKER);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Consumer " + this.getLocalName() + " was created.");
    }

}
