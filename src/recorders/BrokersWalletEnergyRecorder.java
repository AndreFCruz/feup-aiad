package recorders;

import agents.Broker;
import launchers.EnergyMarketLauncher;

public class BrokersWalletEnergyRecorder extends BrokersWalletRecorder {

    public BrokersWalletEnergyRecorder(EnergyMarketLauncher em, Broker b) {
        super(em, b);
    }

    @Override
    public float getWalletBalance() {
        return broker.getEnergyWallet().getBalance();
    }
}
