package agents;

import behaviours.consumer.ConsumerBusinessStarter;
import behaviours.consumer.ConsumerContractInitiator;
import behaviours.consumer.ConsumerContractWrapperBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;

import java.util.*;
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

    private int preferredContractDuration = 7200;

    public Consumer(EnergyMarketLauncher model, GraphicSettings graphicSettings, int energyConsumptionPerMonth) {
        super(model, graphicSettings);
        this.setSearchType(AgentType.BROKER);
        this.energyConsumptionPerMonth = energyConsumptionPerMonth;
    }

    /**
     * @return Ordered list of brokers, sorted by preference, from highest to lowest.
     */
    public List<Broker> getBrokersByPreference() {
        return orderBrokersByPreference(this.getBrokers());
    }

    /**
     * Factory method for sorting Brokers by preference, for Consumer specializations.
     *
     * @param brokers the available Brokers.
     * @return ordered list of brokers, from highest preference to lowest.
     */
    protected List<Broker> orderBrokersByPreference(List<Broker> brokers) {
        Collections.sort(brokers, Comparator.comparingInt(Broker::getEnergyUnitSellPrice));
        return brokers;
    }

    /**
     * @return List of Brokers registered in the DF service (yellow-pages).
     */
    private List<Broker> getBrokers() {
        List<AID> brokersAID = new ArrayList<>();

        for (DFAgentDescription p : this.searchAndGet()) {
            brokersAID.add(p.getName());
        }
        return brokersAID.stream()
                .map((p) -> (Broker) getWorldModel().getAgentByAID(p))
                .filter((Broker b) -> b.monthsThatMayFulfillContract(getEnergyConsumptionPerMonth()) > 1)
                .collect(Collectors.toList());
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

        if (!brokerService) {
            this.addBehaviour(
                    new ConsumerContractWrapperBehaviour(
                            new ConsumerContractInitiator(this)
                    )
            );
        }
    }

    public int getNewContractDuration() {
        Random rand = new Random();
        int duration = (int) (rand.nextGaussian() * (preferredContractDuration / 2f) + preferredContractDuration);
        return duration > 60 ? duration : 60;
    }

    public void consume() {
        if (hasBrokerService())
            getEnergyWallet().consume(getEnergyConsumptionPerMonth() / 30f);
    }
}
