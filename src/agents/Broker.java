package agents;

import behaviours.broker.BrokerBusinessStarter;
import behaviours.broker.BrokerListeningBehaviour;
import sajas.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.EnergyContract;
import utils.EnergyContractProposal;
import utils.GraphicSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Broker class represents the middleman between the energy suppliers and the final consumers.
 */
public class Broker extends DFRegisterAgent {
    public List<EnergyContract> getProducerContracts() {
        return producerContracts;
    }

    public List<EnergyContract> getConsumerContracts() {
        return consumerContracts;
    }

    private List<EnergyContract> producerContracts = new ArrayList<>();

    private List<EnergyContract> consumerContracts = new ArrayList<>();

    private static final int TIMEOUT = 2000;

    private DFSearchAgent search;

    private int duration = 365; // TODO: remove this at a further development stage

    private int energyUnitSellPrice;

    public Broker(EnergyMarketLauncher model, GraphicSettings graphicSettings, int initialBudget) {
        super(model, graphicSettings, AgentType.BROKER);
        this.search = new DFSearchAgent(model, graphicSettings);

        this.search.setSearchType(AgentType.PRODUCER);

        this.moneyWallet.inject(initialBudget);
    }

    @Override
    protected void setup() {
        super.setup();
        addBehaviour(new BrokerBusinessStarter(this, TIMEOUT));
        addBehaviour(new BrokerListeningBehaviour(this));
    }

    /**
     * Fetches the list of promising (available) producers for new contracts.
     *
     * @return the list of producers.
     */
    public List<String> getPromisingProducers() {
        ArrayList<String> producersNames = new ArrayList<>();

        for (DFAgentDescription p : this.search.searchAndGet()) {
            producersNames.add(p.getName().getLocalName());
        }
        return producersNames;
    }

    public void addEnergyContract(EnergyContract ec) {
        producerContracts.add(ec);
        updateEnergyUnitSellPrice();
    }

    public void addConsumerContract(EnergyContract ec) {
        consumerContracts.add(ec);
    }

    public EnergyMarketLauncher getWorldModel() {
        return worldModel;
    }

    public int getDuration() {
        return duration;
    }

    public int getAvailableMonthlyEnergyQuota() {
        int sold = (int) consumerContracts.stream().mapToDouble(EnergyContract::getEnergyAmountPerMonth).sum();
        return getMonthlyEnergy() - sold;
    }

    /**
     * Update the current energy
     */
    public void updateEnergyUnitSellPrice() {
        float costPerUnit = (float) getMonthlyCosts() / getMonthlyEnergy();
        this.energyUnitSellPrice = (int) (costPerUnit * 1.2f);
    }

    public int getEnergyUnitSellPrice() {
        return energyUnitSellPrice;
    }

    public int getMonthlyCosts() {
        return (int) producerContracts.stream().mapToDouble(
                (EnergyContract c) -> c.getEnergyCostPerUnit() * c.getEnergyAmountPerMonth()
        ).sum();
    }

    public int getMonthlyEnergy() {
        return (int) producerContracts.stream().mapToDouble(EnergyContract::getEnergyAmountPerMonth).sum();
    }

    public int getMonthlyRenewableEnergy() {
        return (int) producerContracts.stream()
                .filter((EnergyContract ec) -> ((Producer) ec.getEnergySupplier()).getEnergySource().isRenewable())
                .mapToDouble(EnergyContract::getEnergyAmountPerMonth).sum();
    }

    public int getNumberProducers() {
        return producerContracts.size();
    }

}
