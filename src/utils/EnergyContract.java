package utils;

import agents.GenericAgent;
import java.io.NotSerializableException;

public class EnergyContract {

    /**
     * Agent that supplies/sells energy.
     */
    private GenericAgent energySupplier;

    /**
     * Agent that buys energy.
     */
    private GenericAgent energyClient;

    /**
     * Ticks (days) since the beginning of the contract.
     */
    private int ticks = 0;

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

    public EnergyContract(EnergyContractProposal proposal, GenericAgent supplier, GenericAgent client) {
        if (! (proposal.isSigned()
                && supplier.getAID() == proposal.getEnergySupplierAID()
                && client.getAID() == proposal.getEnergyClientAID() ) ) {
            throw new IllegalArgumentException();
        }

        this.energySupplier = supplier;
        this.energyClient = client;
        this.energyAmountPerCycle = proposal.getEnergyAmountPerCycle();
        this.energyCostPerUnit = proposal.getEnergyCostPerUnit();
        this.duration = proposal.getDuration();
        this.paymentCycle = proposal.getPaymentCycle();
    }

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
        if (this.hasEnded()) {
            // TODO Inform client and supplier of finished contract.
            return;
        }

        // Pay day
        if (ticks % paymentCycle == 0) {
            int totalEnergyCost = energyCostPerUnit * energyAmountPerCycle;
            energySupplier.getEnergyWallet().withdraw(energyAmountPerCycle, energyClient.getEnergyWallet());
            energyClient.getMoneyWallet().withdraw(totalEnergyCost, energySupplier.getMoneyWallet());
        }

        // Ticks are updated at the end of each step, as such,
        // contracts are paid at the beginning of each payment cycle.
        this.ticks += 1;
    }


    /**
     * Class is not serializable.
     * @throws NotSerializableException
     */
    private void writeObject(java.io.ObjectOutputStream stream) throws NotSerializableException {
        throw new NotSerializableException();
    }

    /**
     * Class is not serializable.
     * @throws NotSerializableException
     */
    private void readObject(java.io.ObjectInputStream stream) throws NotSerializableException {
        throw new NotSerializableException();
    }

}
