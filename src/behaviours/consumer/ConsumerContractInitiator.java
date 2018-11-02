package behaviours.consumer;

import agents.Broker;
import agents.Consumer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import utils.EnergyContractProposal;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class ConsumerContractInitiator extends FIPAContractNetInitiator {

    List<String> brokers; // TODO get Brokers instead of Broker names from Consumer.getBrokers

    public ConsumerContractInitiator(Consumer agent) {
        super(agent);
        // brokers = agent.getPromisingBrokers();
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        List<Broker> orderedListOfPreferences = getOrderedListOfPreferences();

        Vector<ACLMessage> v = new Vector<>();
        Consumer c = (Consumer) myAgent;

        boolean contactedAtLeastOne = false;
        float consumerCash = c.getMoneyWallet().getBalance();

        for (Broker b : orderedListOfPreferences) {
            int contractCost = b.getEnergyUnitSellPrice() * c.getEnergyConsumptionPerMonth();

            if (b.getAvailableEnergy() >= c.getEnergyConsumptionPerMonth() && contractCost <= consumerCash) {
                contactedAtLeastOne = true;
                cfp.addReceiver(b.getAID());
                consumerCash -= contractCost;
            }
        }

        if (contactedAtLeastOne) {
            // if at least one broker can supply this consumer, create a draft contract
            EnergyContractProposal ec = EnergyContractProposal.makeContractDraft(myAgent.getAID(), ((Broker) myAgent).getDuration());
            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }
            c.setHasBrokerService(true);
            v.add(cfp);
        }

        // TODO: check if there is a problem when this comes empty ?
        return v;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        // TODO
        super.handleAllResponses(responses, acceptances);
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        // TODO
        super.handleAllResultNotifications(resultNotifications);
    }

    /**
     * @return An ordered ArrayList of the preferred order to contact the Producers.
     */
    private List<Broker> getOrderedListOfPreferences() {
        // getting all the agents
        List<Broker> result = ((Consumer) myAgent).getWorldModel().getBrokers();

        // this sorting can lead to a lot of agents trying to get the same producer
//      result.sort(Comparator.comparingInt(Producer::getEnergyUnitSellPrice).reversed());
        Collections.shuffle(result);

        return result;
    }
}
