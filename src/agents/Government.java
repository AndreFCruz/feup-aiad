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

    private float percentageMonopoly = 0.8f;

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

            List<Integer> brokersNumProducers = brokers.stream().mapToInt(Broker::getNumberProducers).boxed().collect(Collectors.toList());
            for (int i = 0; i < brokersNumProducers.size(); ++i){
                Integer numProducers = brokersNumProducers.get(i);
                if (numProducers/worldModel.getNUM_PRODUCERS() >= percentageMonopoly){
                    // this guys has a monopoly
                    // TODO: add punishment here
                    break;
                }
            }

        }
    }
}
