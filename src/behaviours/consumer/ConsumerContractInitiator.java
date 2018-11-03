package behaviours.consumer;

import agents.Broker;
import agents.Consumer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
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

        for (Broker b : orderedListOfPreferences) {
            if (b.getAvailableMonthlyEnergyQuota() >= c.getEnergyConsumptionPerMonth()) {
                contactedAtLeastOne = true;
                cfp.addReceiver(b.getAID());
            }
        }

        if (contactedAtLeastOne) {
            EnergyContractProposal ec = EnergyContractProposal.makeContractDraft(myAgent.getAID(), c.getContractDuration());
            ec.updateEnergyAmount(c.getEnergyConsumptionPerMonth());

            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }
            v.add(cfp);
        }

        // TODO: check if there is a problem when this comes empty ?
        return v;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        for (Object response : responses) {
            ACLMessage received = ((ACLMessage) response);
            ACLMessage reply = ((ACLMessage) response).createReply();

            if (received.getPerformative() == ACLMessage.PROPOSE) {
                try {
                    EnergyContractProposal ec = (EnergyContractProposal) received.getContentObject();
                    ec.signContract(myAgent);
                    ((Consumer) myAgent).setHasBrokerService(true);

                    ((Consumer) myAgent).getWorldModel().addConsumerBrokerContract(ec);

                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptances.add(reply);

                } catch (UnreadableException e) {
                    e.printStackTrace();

                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.add(reply);
                }
            } else {
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.add(reply);
            }
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
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
