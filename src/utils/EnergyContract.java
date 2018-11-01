package utils;

import agents.GenericAgent;

import java.io.Serializable;

/**
 * TODO Can this be implemented by extending Behaviour?
 * behaviour.action() -> step()
 * behaviour.done() -> hasEnded()
 * <p>
 * Actually, this can be a CompositeBehaviour, in which each part of the
 * contract (monetary and energy) is itself a Contract Behaviour (allows
 * for delivering energy daily with monthly payments).
 */
public class EnergyContract implements Serializable {
    private enum ContractState {
        DRAFT,      // No price defined
        PROPOSAL,   // Features complete contract information, but hasn't yet been accepted
        SIGNED      // Contract has been accepted by both parts
    }

    /**
     * Agent that sells energy.
     */
    private GenericAgent energySupplier;

    /**
     * Agent that pays for the energy.
     */
    private GenericAgent energyClient;

    /**
     * Ticks (days) since the beginning of the contract.
     */
    private int ticks = 0;

    /**
     * Total energy to be transferred every payment cycle.
     */
    private int energyAmount;

    /**
     * Currency to be transferred every payment cycle, per energy unit.
     */
    private int energyCostPerUnit;

    /**
     * Length of the contract, in days (ticks).
     */
    private int duration;

    /**
     * Periodicity of payment, in days (ticks).
     */
    private int paymentCycle = 30;

    /**
     * The state of this EnergyContract.
     */
    private ContractState state;

    /**
     * Constructor for an energy contract.
     * Supplier and Client agents are bound to this contract during its duration.
     *
     * @param energySupplier    The agent that supplies/sells energy.
     * @param energyClient      The agent that buys energy.
     * @param energyAmount      The amount of energy to be traded.
     * @param energyCostPerUnit The amount of money to be paid.
     * @param duration          The duration of the contract.
     */
    public EnergyContract(GenericAgent energySupplier, GenericAgent energyClient,
                          int energyAmount, int energyCostPerUnit, int duration) {
        this.energySupplier = energySupplier;
        this.energyClient = energyClient;
        this.energyAmount = energyAmount;
        this.energyCostPerUnit = energyCostPerUnit;
        this.duration = duration;

        this.state = ContractState.SIGNED;
    }

    private EnergyContract() {
    }

    /**
     * Creates a contract draft, sent from the Client to the Producer.
     *
     * @param energyClient The Energy Client.
     * @param duration     The duration of the contract.
     * @return
     */
    public static EnergyContract makeContractDraft(GenericAgent energyClient, int duration) {
        EnergyContract contract = new EnergyContract();
        contract.energyClient = energyClient;
        contract.duration = duration;

        contract.state = ContractState.DRAFT;
        return contract;
    }

    /**
     * Updates the amount to be transactioned by this contract.
     *
     * @param energyAmount the new energy amount to be traded every cycle.
     */
    @Deprecated
    public void updateEnergyAmount(int energyAmount) {
        if (this.state == ContractState.SIGNED) {
            System.err.println("Can't update energy amount of already signed contract.");
            return;
        }
        this.energyAmount = energyAmount;
    }

    /**
     * Updates the total energy cost to be paid per cycle.
     * Marks this contract as a proposal (no longer draft).
     *
     * @param energyCost new total energy cost per payment cycle.
     */
    @Deprecated
    public void updateEnergyCostPerUnit(int energyCost) {
        if (this.state == ContractState.SIGNED) {
            System.err.println("Can't update energy cost of already signed contract.");
            return;
        }
        this.energyCostPerUnit = energyCost;
    }

    public EnergyContract makeContractProposal(GenericAgent supplier, int energyAmount, int energyCostPerUnit) {
        if (this.state == ContractState.SIGNED)
            throw new IllegalArgumentException("Making contract proposal from already signed contract.");
        this.energySupplier = supplier;
        this.energyAmount = energyAmount;
        this.energyCostPerUnit = energyCostPerUnit;
        this.state = ContractState.PROPOSAL;
        return this;
    }

    /**
     * @return Whether this contract has ended.
     */
    public boolean hasEnded() {
        return ticks >= duration;
    }

    /**
     * Client signs contract proposal.
     */
    public void signContract(GenericAgent agent) {
        if (agent != energyClient)
            throw new IllegalArgumentException("Signing agent was not the client.");
        this.state = ContractState.SIGNED;
    }

    public boolean isSigned() {
        return this.state == ContractState.SIGNED;
    }

    /**
     * Steps this contract, meaning one day (tick) has passed.
     */
    public void step() {
        if (this.hasEnded() || this.state != ContractState.SIGNED)
            return;

        // Pay day
        if (ticks % paymentCycle == 0) {
            int totalEnergyCost = energyCostPerUnit * energyAmount;
            energySupplier.getEnergyWallet().withdraw(energyAmount, energyClient.getEnergyWallet());
            energyClient.getMoneyWallet().withdraw(totalEnergyCost, energySupplier.getMoneyWallet());
        }

        // Ticks are updated at the end of each step, as such,
        // contracts are paid at the beginning of each payment cycle.
        this.ticks += 1;
    }

}
