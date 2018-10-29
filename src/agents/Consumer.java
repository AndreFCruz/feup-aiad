package agents;

import launchers.EnergyMarketLauncher;
import utils.GraphicSettings;

import java.awt.*;

/**
 * Only subscribes ONE Broker;
 * Some Consumers have higher inertia to changing Brokers;
 * Some Consumers prefer green energy;
 */
public class Consumer extends GenericAgent {

    public Consumer(EnergyMarketLauncher model, GraphicSettings graphicSettings){
        super(model, graphicSettings);

    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Consumer " + this.getLocalName() + " was created.");
    }

}
