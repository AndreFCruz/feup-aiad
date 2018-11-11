package agents;

import behaviours.broker.BrokerBusinessStarter;
import behaviours.broker.BrokerListeningBehaviour;
import jade.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.EnergyContract;
import utils.GraphicSettings;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The Broker class represents the middleman between the energy suppliers and the final consumers.
 */
public class Broker extends DFRegisterAgent {

    private List<EnergyContract> producerContracts = new ArrayList<>();

    private List<EnergyContract> consumerContracts = new ArrayList<>();

    private float profitMargin = 0.10f;

    private float profitMarginIncrements = 0.05f;

    private float minimumProfitMargin = 0.05f;

    private static final int TIMEOUT = 2000;

    private DFSearchAgent search;

    private int preferredContractDuration = 3600;

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
     * @return Ordered list of Producers, sorted by preference, from highest to lowest.
     */
    public List<Producer> getProducersByPreference() {
        return orderProducersByPreference(getProducers());
    }

    /**
     * Factory method for ordering Producers according to different preferences.
     *
     * @param producers the list of available producers.
     * @return the sorted list of producers, from highest to lowest preference.
     */
    protected List<Producer> orderProducersByPreference(List<Producer> producers) {
        Collections.sort(producers, Comparator.comparingInt(Producer::getEnergyUnitSellPrice));
        return producers;
    }

    /**
     * Returns all available producers from the DF service (yellow-pages).
     *
     * @return the list of producers.
     */
    private List<Producer> getProducers() {
        List<AID> producersAID = new ArrayList<>();
        for (DFAgentDescription p : this.search.searchAndGet()) {
            producersAID.add(p.getName());
        }

        return producersAID.stream()
                .map((p) -> (Producer) getWorldModel().getAgentByAID(p))
                .filter((Producer p) -> !p.hasContract())
                .collect(Collectors.toList());
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

    public int getNewContractDuration() {
        Random rand = new Random();
        int duration = (int) (rand.nextGaussian() * (preferredContractDuration / 3.) + preferredContractDuration);
        return duration > 180 ? duration : 180;
    }

    public int getAvailableMonthlyEnergyQuota() {
        return getMonthlyEnergy() - getMonthlySoldEnergy() + ((int) energyWallet.getBalance());
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
        int profitChange = 0;

        if (getMonthlySoldEnergy() < getMonthlyEnergy() * 0.5)
            profitChange -= 1;      // make energy cheaper because you have plenty to sell
        if (revenue < costs) {
            profitChange += 1;      // make energy pricier, because you're losing money
        } else if (revenue * (1 + profitMargin - profitMarginIncrements) / (1 + profitMargin) > costs) {
            profitChange -= 1;      // make energy cheaper because you have a large profit margin
        }

        System.out.println("Changed profit margin from " + profitMargin + " to "
                + (profitMargin + (profitChange * profitMarginIncrements))
        );
        this.profitMargin += profitChange * profitMarginIncrements;
        if (profitMargin < minimumProfitMargin)
            profitMargin = minimumProfitMargin;
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

    public float getPercentageOfRenewableEnergy() {
        return (float) getMonthlyRenewableEnergy() / getMonthlyEnergy();
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

    public int monthsThatMayFulfillAllContracts() {
        List<EnergyContract> consumerContractsClone = new ArrayList<>(consumerContracts);
        List<EnergyContract> producerContractsClone = new ArrayList<>(producerContracts);

        float energyBalance = energyWallet.getBalance();
        int monthsFulfilled = 0;
        while (energyBalance > 0 || !(consumerContractsClone.size() == 0 && producerContractsClone.size() == 0)) {
            energyBalance += contractsSimulator(producerContractsClone);
            energyBalance -= contractsSimulator(consumerContractsClone);

            monthsFulfilled += 1;
        }
        return monthsFulfilled;
    }

    /**
     * Simulates one month of contracts in the given list of contracts, removing the contracts that have ended
     *
     * @param contracts The array of contracts
     * @return returns the sum of all the contracts monthly costs
     */
    private int contractsSimulator(List<EnergyContract> contracts) {
        int total = 0;
        for (ListIterator<EnergyContract> iter = contracts.listIterator(); iter.hasNext(); ) {
            EnergyContract ec = iter.next();
            if (!ec.hasEnded()) {
                total += ec.getMonthlyEnergyCost();
                ec.simulateCycle();
            }
            else
                iter.remove();
        }
        return total;
    }
}
