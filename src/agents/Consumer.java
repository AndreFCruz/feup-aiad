package agents;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;

import java.util.ArrayList;
import java.util.List;

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

    // TODO return actual Brokers instead of Strings
    public List<String> getPromisingBrokers() {
        ArrayList<String> brokersNames = new ArrayList<>();

        for (DFAgentDescription p : this.searchAndGet()) {
            brokersNames.add(p.getName().getLocalName());
        }
        return brokersNames;
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
