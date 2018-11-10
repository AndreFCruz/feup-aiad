package utils;

import jade.core.AID;
import sajas.core.Agent;

import java.io.Serializable;


/**
 * Proposal for an Energy Contract, to be sent by the client to the supplier, and possibly back-and-forth.
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
     * Start date of the contract, in ticks since the start of the simulation.
     */
    private Integer startDate = null;

    /**
     * Periodicity of payment, in days (ticks).
     */
    private int paymentCycle = 30;

    /**
     * The state of this EnergyContractProposal.
     */
    private ContractState state = ContractState.DRAFT;

    /**
     * Private default constructor.
     */
    private EnergyContractProposal() {}

    /**
     * Creates a contract draft, sent from the Client to the Producer.
     *
     * @param energyClientAID   The Energy Client.
     * @param duration          The duration of the contract.
     * @return a new EnergyContractProposal object with the given draft settings.
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
    public void updateEnergyAmount(int energyAmount) {
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
    public void updateEnergyCostPerUnit(int energyCost) {
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

    public void setStartDate(int startDate) {
        if (this.state == ContractState.SIGNED)
            throw new IllegalArgumentException("Making contract proposal from already signed contract.");
        this.startDate = startDate;
    }

    public int getStartDate() {
        return startDate;
    }

    public int getEndDate() {
        return startDate + duration;
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