package recorders;

import agents.Broker;
import launchers.EnergyMarketLauncher;

public class BrokersWalletMoneyRecorder extends BrokersWalletRecorder {

    public BrokersWalletMoneyRecorder(EnergyMarketLauncher em, Broker b) {
        super(em, b);
    }

    @Override
    public float getWalletBalance() {
        return broker.getMoneyWallet().getBalance();
    }
}
