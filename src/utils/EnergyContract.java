package utils;

import agents.GenericAgent;

/**
 * TODO Can this be implemented by extending Behaviour?
 * behaviour.action() -> step()
 * behaviour.done() -> hasEnded()
 *
 * Actually, this can be a CompositeBehaviour, in which each part of the
 *  contract (monetary and energy) is itself a Contract Behaviour (allows
 *  for delivering energy daily with monthly payments).
 */
public class EnergyContract {

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
     * Total currency to be transferred every payment cycle.
     */
    private int energyCost;

    /**
     * Length of the contract, in days (ticks).
     */
    private int duration;

    /**
     * Periodicity of payment, in days (ticks).
     */
    private int paymentCycle = 30;

    /**
     * Constructor for an energy contract.
     * Supplier and Client agents are bound to this contract during its duration.
     * @param energySupplier    The agent that supplies/sells energy.
     * @param energyClient      The agent that buys energy.
     * @param energyAmount      The amount of energy to be traded.
     * @param energyCost        The amount of money to be paid.
     * @param duration          The duration of the contract.
     */
    public EnergyContract(GenericAgent energySupplier, GenericAgent energyClient,
                          int energyAmount, int energyCost, int duration) {
        this.energySupplier = energySupplier;
        this.energyClient = energyClient;
        this.energyAmount = energyAmount;
        this.energyCost = energyCost;
        this.duration = duration;
    }

    public EnergyContract(GenericAgent energySupplier, GenericAgent energyClient,
                          int energyAmount, int energyCost, int duration, int paymentCycle) {
        this(energySupplier, energyClient, energyAmount, energyCost, duration);
        this.paymentCycle = paymentCycle;
    }

    /**
     * Updates the amount to be transactioned by this contract.
     */
    protected void updateAmount() {}

    /**
     * @return Whether this contract has ended.
     */
    public boolean hasEnded() {
        return ticks >= duration;
    }

    /**
     * Steps this contract, meaning one day (tick) has passed.
     */
    public void step() {
        if (this.hasEnded())
            return;
        this.updateAmount();

        // Pay day
        if (ticks % paymentCycle == 0) {
            energySupplier.getEnergyWallet().withdraw(energyAmount, energyClient.getEnergyWallet());
            energyClient.getMoneyWallet().withdraw(energyCost, energySupplier.getMoneyWallet());
        }

        // Ticks are updated at the end of each step, as such,
        // contracts are paid at the beginning of each payment cycle.
        this.ticks += 1;
    }

}
