package utils;

import agents.GenericAgent;
import jade.core.AID;
import sajas.core.Agent;

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
public class EnergyContractProposal implements Serializable {
    static final long serialVersionUID = 142L;

    private enum ContractState {
        DRAFT,      // No price defined
        PROPOSAL,   // Features complete contract information, but hasn't yet been accepted
        SIGNED      // Contract has been accepted by both parts
    }

    /**
     * AID of the agent that supplies/sells energy.
     */
    private AID energySupplierAID;

    /**
     * AID of the agent that buys energy.
     */
    private AID energyClientAID;

    /**
     * Total energy to be transferred every payment cycle.
     */
    private int energyAmountPerCycle;

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
     * The state of this EnergyContractProposal.
     */
    private ContractState state = ContractState.DRAFT;

    private EnergyContractProposal() {}

    /**
     * Creates a contract draft, sent from the Client to the Producer.
     *
     * @param energyClientAID   The Energy Client.
     * @param duration          The duration of the contract.
     * @return
     */
    public static EnergyContractProposal makeContractDraft(AID energyClientAID, int duration) {
        EnergyContractProposal contract = new EnergyContractProposal();
        contract.energyClientAID = energyClientAID;
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
    public void updateEnergyAmount(int energyAmount) { // For future iterative contract communications
        if (this.state == ContractState.SIGNED) {
            System.err.println("Can't update energy amount of already signed contract.");
            return;
        }
        this.energyAmountPerCycle = energyAmount;
    }

    /**
     * Updates the total energy cost to be paid per cycle.
     * Marks this contract as a proposal (no longer draft).
     *
     * @param energyCost new total energy cost per payment cycle.
     */
    @Deprecated
    public void updateEnergyCostPerUnit(int energyCost) { // For future iterative contract communications
        if (this.state == ContractState.SIGNED) {
            System.err.println("Can't update energy cost of already signed contract.");
            return;
        }
        this.energyCostPerUnit = energyCost;
    }

    /**
     * Makes a contract proposal from a contract draft, by specifying the energy supplier
     * the energy amount to be traded per cycle, and cost per unit.
     * @param energySupplierAID     AID of the energy supplier.
     * @param energyAmount          Amount of energy to be traded every cycle.
     * @param energyCostPerUnit     Cost per energy unit.
     * @return
     */
    public EnergyContractProposal makeContractProposal(AID energySupplierAID, int energyAmount, int energyCostPerUnit) {
        if (this.state == ContractState.SIGNED)
            throw new IllegalArgumentException("Making contract proposal from already signed contract.");
        this.energySupplierAID = energySupplierAID;
        this.energyAmountPerCycle = energyAmount;
        this.energyCostPerUnit = energyCostPerUnit;
        this.state = ContractState.PROPOSAL;
        return this;
    }

    /**
     * Client signs contract proposal.
     */
    public void signContract(Agent agent) {
        if (! agent.getAID().equals(energyClientAID))
            throw new IllegalArgumentException("Signing agent was not the client.");
        this.state = ContractState.SIGNED;
    }

    /**
     * Checks whether contract is signed (final).
     * @return whether contract is signed.
     */
    public boolean isSigned() {
        return this.state == ContractState.SIGNED;
    }

    public AID getEnergySupplierAID() {
        return energySupplierAID;
    }

    public AID getEnergyClientAID() {
        return energyClientAID;
    }

    public int getEnergyAmountPerCycle() {
        return energyAmountPerCycle;
    }

    public int getEnergyCostPerUnit() {
        return energyCostPerUnit;
    }

    public int getDuration() {
        return duration;
    }

    public int getPaymentCycle() {
        return paymentCycle;
    }
}