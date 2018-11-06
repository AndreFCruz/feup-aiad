package agents;

import behaviours.broker.BrokerBusinessStarter;
import behaviours.broker.BrokerListeningBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import launchers.EnergyMarketLauncher;
import jade.core.AID;
import utils.AgentType;
import utils.EnergyContract;
import utils.GraphicSettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The Broker class represents the middleman between the energy suppliers and the final consumers.
 */
public class Broker extends DFRegisterAgent {

    private List<EnergyContract> producerContracts = new ArrayList<>();

    private List<EnergyContract> consumerContracts = new ArrayList<>();

    private float profitMargin = 0.10f;

    private float profitMarginIncrements = 0.05f;

    private static final int TIMEOUT = 2000;

    private DFSearchAgent search;

    private int duration = 730; // TODO: remove this at a further development stage

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
    public List<Producer> getOrderedProducers() {
        List<AID> producersAID = new ArrayList<>();

        for (DFAgentDescription p : this.search.searchAndGet()) {
            producersAID.add(p.getName());
        }
        List<Producer> producers = producersAID.stream().map((p) -> (Producer) getWorldModel().getAgentByAID(p)).collect(Collectors.toList());

        // TODO sort Producers here
        Collections.shuffle(producers);

        return producers;
    }

    public List<EnergyContract> getProducerContracts() {
        return producerContracts;
    }

    public List<EnergyContract> getConsumerContracts() {
        return consumerContracts;
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
        return getMonthlyEnergy() - getMonthlySoldEnergy();
    }

    /**
     * Update the current energy
     */
    public void updateEnergyUnitSellPrice() {
        updateProfitMargin();

        float costPerUnit = (float) getMonthlyCosts() / getMonthlyEnergy();
        this.energyUnitSellPrice = (int) (costPerUnit * (1f + this.profitMargin));
    }

    /**
     * Rethinks the agent's profit margin, according to expected revenue/expenses.
     */
    private void updateProfitMargin() {
        int revenue = getMonthlyRevenue();
        int costs = getMonthlyCosts();

        if (revenue < costs) {
            this.profitMargin += profitMarginIncrements;
            System.out.println("Increased profit margin to " + profitMargin);
        } else if (revenue * (1 + profitMargin - profitMarginIncrements) / (1 + profitMargin) > costs) {
            this.profitMargin -= profitMarginIncrements;
            System.out.println("Reduced profit margin to " + profitMargin);
        }
    }

    public int getEnergyUnitSellPrice() {
        return energyUnitSellPrice;
    }

    public int getMonthlyCosts() {
        return (int) producerContracts.stream().mapToDouble(EnergyContract::getMonthlyEnergyCost).sum();
    }

    public int getMonthlyRevenue() {
        return (int) consumerContracts.stream().mapToDouble(EnergyContract::getMonthlyEnergyCost).sum();
    }

    public int getMonthlyEnergy() {
        return (int) producerContracts.stream().mapToDouble(EnergyContract::getEnergyAmountPerMonth).sum();
    }

    private int getMonthlySoldEnergy() {
        return (int) consumerContracts.stream().mapToDouble(EnergyContract::getEnergyAmountPerMonth).sum();
    }

    public int getMonthlyRenewableEnergy() {
        return (int) producerContracts.stream()
                .filter((EnergyContract ec) -> ((Producer) ec.getEnergySupplier()).getEnergySource().isRenewable())
                .mapToDouble(EnergyContract::getEnergyAmountPerMonth).sum();
    }

    public int monthsThatMayFulfillContract(int energyConsumptionPerMonth) {
        if (getAvailableMonthlyEnergyQuota() > energyConsumptionPerMonth)
            return 12;
        else if (energyWallet.getBalance() > energyConsumptionPerMonth) {
            return (energyWallet.getBalance() / energyConsumptionPerMonth) > 12 ?
                    12 : (int) (getEnergyWallet().getBalance() / energyConsumptionPerMonth);
        } else {
            return 0;
        }
    }
    int getNumberProducers() {
        return producerContracts.size();
    }

}
