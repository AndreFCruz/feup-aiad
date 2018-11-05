package agents;

import behaviours.consumer.ConsumerBusinessStarter;
import behaviours.consumer.ConsumerContractInitiator;
import behaviours.consumer.ConsumerContractWrapperBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Only subscribes ONE Broker;
 * Some Consumers have higher inertia to changing Brokers;
 * Some Consumers prefer green energy;
 */
public class Consumer extends DFSearchAgent {

    private static final int TIMEOUT = 2000;

    private int energyConsumptionPerMonth;

    private boolean brokerService = false;

    private int contractDuration = 360; // One year contracts

    public Consumer(EnergyMarketLauncher model, GraphicSettings graphicSettings, int energyConsumptionPerMonth) {
        super(model, graphicSettings);
        this.setSearchType(AgentType.BROKER);
        this.energyConsumptionPerMonth = energyConsumptionPerMonth;
    }

    /**
     * Fetches the list of promising (available) brokers for new contracts.
     *
     * @return the list of brokers.
     */
    public List<Broker> getOrderedBrokers() {
        List<AID> brokersAID = new ArrayList<>();

        for (DFAgentDescription p : this.searchAndGet()) {
            brokersAID.add(p.getName());
        }
        List<Broker> brokers = brokersAID.stream().map((p) -> (Broker) getWorldModel().getAgentByAID(p)).collect(Collectors.toList());

        // TODO sort Producers here
        // this sorting can lead to a lot of agents trying to get the same producer
        // result.sort(Comparator.comparingInt(Broker::getAvailableMonthlyEnergyQuota));
        Collections.shuffle(brokers);

        return brokers;
    }

    public EnergyMarketLauncher getWorldModel() {
        return worldModel;
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new ConsumerBusinessStarter(this, TIMEOUT));
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

        if (! brokerService) {
            this.addBehaviour(
                    new ConsumerContractWrapperBehaviour(
                            new ConsumerContractInitiator(this)
                    )
            );
        }
    }

    public int getContractDuration() {
        return contractDuration;
    }
}
