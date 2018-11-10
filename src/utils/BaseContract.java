package utils;

import java.io.Serializable;

public class BaseContract implements Serializable {
    /**
     * Total energy to be transferred every payment cycle.
     */
    protected int energyAmountPerCycle;

    /**
     * Currency to be transferred every payment cycle, per energy unit.
     */
    protected int energyCostPerUnit;

    /**
     * Length of the contract, in days (ticks).
     */
    protected int duration;

    /**
     * Start date of the contract, in ticks since the start of the simulation.
     */
    protected Integer startDate = null;

    /**
     * Periodicity of payment, in days (ticks).
     */
    protected int paymentCycle = 30;


    public Integer getStartDate() {
        return startDate;
    }

    public Integer getEndDate() {
        return startDate != null ? startDate + duration : null;
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
