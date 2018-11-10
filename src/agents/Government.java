package agents;

import launchers.EnergyMarketLauncher;
import sajas.core.behaviours.OneShotBehaviour;
import utils.GraphicSettings;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This agent is responsible for periodically break up monopolies.
 */
public class Government extends GenericAgent {

    private float percentageMonopoly;

    public Government(EnergyMarketLauncher model, GraphicSettings graphicSettings, float pm) {
        super(model, graphicSettings);
        this.percentageMonopoly = pm;
    }

    /**
     * This function is called by the energy market from time to time.
     */
    public void breakUpMonopoly() {
        this.addBehaviour(new MonopolyBreakerBehaviour());
    }

    /**
     * This behaviour breaks up a possible monopoly and redistributes the wealth among the other brokers.
     */
    class MonopolyBreakerBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            List<Broker> brokers = worldModel.getBrokers();

            List<Integer> brokersEnergyReceiving = brokers.stream().mapToInt(Broker::getMonthlyEnergy).boxed().collect(Collectors.toList());
            //int totalEnergyBeingReceived = worldModel.getProducers().stream().mapToInt(Producer::getEnergyProductionPerMonth).sum();
            int totalEnergyBeingReceived = brokersEnergyReceiving.stream().mapToInt(Integer::intValue).sum();

            for (int i = 0; i < brokersEnergyReceiving.size(); ++i) {
                Integer energyReceiving = brokersEnergyReceiving.get(i);
                if (((float) energyReceiving / totalEnergyBeingReceived) >= percentageMonopoly) {
                    Broker monopolyGuy = brokers.get(i);   // this guys has a monopoly
                    float difference = ((float) energyReceiving / totalEnergyBeingReceived) - percentageMonopoly;
                    float correspondingMoney = monopolyGuy.getMoneyWallet().getBalance() * difference;

                    if (correspondingMoney <= 0) {
                        // punish by removing energy (might lead to some contracts being revoked)
                        float correspondingEnergy = monopolyGuy.getEnergyWallet().getBalance() * difference;
                        if (correspondingEnergy < 0) {
                            // TODO: find another way to punish here
                        } else {
                            float energyForEach = correspondingEnergy / (brokers.size() - 1);
                            for (Broker b : brokers) {
                                if (b.getLocalName().equals(monopolyGuy.getLocalName()))
                                    b.getEnergyWallet().consume(correspondingEnergy);
                                else
                                    b.getEnergyWallet().inject(energyForEach);
                            }
                        }

                    } else {
                        // punish by removing money and redistributing it
                        float moneyForEach = correspondingMoney / (brokers.size() - 1);
                        for (Broker b : brokers) {
                            if (b.getLocalName().equals(monopolyGuy.getLocalName()))
                                b.getMoneyWallet().consume(correspondingMoney);
                            else
                                b.getMoneyWallet().inject(moneyForEach);
                        }
                    }
                    break;

                }
            }

        }
    }
}
