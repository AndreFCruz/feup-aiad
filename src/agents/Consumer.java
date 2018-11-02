package agents;

import behaviours.consumer.ConsumeBehaviour;
import behaviours.consumer.ConsumerBusinessStarter;
import behaviours.consumer.IncomeBehaviour;
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

    private static final int TIMEOUT = 2000;

    private int energyConsumptionPerMonth;

    private boolean brokerService = false;

    public Consumer(EnergyMarketLauncher model, GraphicSettings graphicSettings, int energyConsumptionPerMonth) {
        super(model, graphicSettings);
        this.setSearchType(AgentType.BROKER);
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

    public EnergyMarketLauncher getWorldModel() {
        return worldModel;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ConsumerBusinessStarter(this, TIMEOUT));
        addBehaviour(new IncomeBehaviour(this, 120000)); // TODO change hardcoded monthly income
         addBehaviour(new ConsumeBehaviour(this));
        System.out.println("Consumer " + this.getLocalName() + " was created.");
    }

    public int getEnergyConsumptionPerMonth() {
        return energyConsumptionPerMonth;
    }

    public boolean hasBrokerService() {
        return brokerService;
    }

    public void setHasBrokerService(boolean b) {
        brokerService = b;
    }

}
