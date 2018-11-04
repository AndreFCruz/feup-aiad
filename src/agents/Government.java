package agents;

import launchers.EnergyMarketLauncher;
import sajas.core.behaviours.OneShotBehaviour;
import utils.EnergyContract;
import utils.GraphicSettings;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 *  This agent is responsible for periodically break up monopolies.
 */
public class Government extends GenericAgent {

    private float percentageMonopoly;

    Government(EnergyMarketLauncher model, GraphicSettings graphicSettings, float pm) {
        super(model, graphicSettings);
        this.percentageMonopoly = pm;
    }

    /**
     *  This function is called by the energy market from time to time.
     */
    public void breakUpMonopoly(){
        this.addBehaviour(new MonopolyBreakerBehaviour());
    }

    /**
     *  This behaviour breaks up a possible monopoly and redistributes the wealth among the other brokers.
     */
    class MonopolyBreakerBehaviour extends OneShotBehaviour {

        @Override
        public void action() {
            List<Broker> brokers = worldModel.getBrokers();

            List<Integer> brokersEnergyReceiving = brokers.stream().mapToInt(Broker::getMonthlyEnergy).boxed().collect(Collectors.toList());
            //int totalEnergyBeingReceived = worldModel.getProducers().stream().mapToInt(Producer::getEnergyProductionPerMonth).sum();
            int totalEnergyBeingReceived = brokersEnergyReceiving.stream().mapToInt(Integer::intValue).sum();

            for (int i = 0; i < brokersEnergyReceiving.size(); ++i){
                Integer energyReceiving = brokersEnergyReceiving.get(i);
                if (((float) energyReceiving/totalEnergyBeingReceived) >= percentageMonopoly){
                    Broker monopolyGuy = brokers.get(i);   // this guys has a monopoly

                    // TODO: add punishment here
                    break;
                }
            }

        }
    }
}
