package behaviours.broker;

import agents.Broker;
import agents.Producer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.EnergyContractProposal;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * This class represents the behaviour of the Brokers when they are trying to
 * talk with the Producers to get energy supplied.
 */
public class BrokerContractInitiator extends FIPAContractNetInitiator {

    private Broker myBroker;

    public BrokerContractInitiator(Broker agent) {
        super(agent);
        myBroker = agent;
    }

    /**
     * In this function the Broker decides which Producers he want to contact.
     *
     * @param cfp A template message.
     * @return A Vector of messages that will be delivered.
     */
    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        Vector v = new Vector();

        if (((Broker) myAgent).monthsThatMayFulfillAllContracts() > Broker.MONTHS_STOPS_BUYING_ENERGY)
            return v;

        List<Producer> orderedListOfPreferences = myBroker.getProducersByPreference();

        boolean willContactAtLeastOne = false;
        float brokersCash = myBroker.getMoneyWallet().getBalance();

        for (Producer p : orderedListOfPreferences) {
            // get price for producer's total energy per month
            int contractCost = p.getEnergyProductionPerMonth() * p.getEnergyUnitSellPrice();

            if (brokersCash > contractCost) {
                willContactAtLeastOne = true;
                cfp.addReceiver(p.getAID());
                brokersCash -= contractCost;
            }
        }

        int newContractDuration = myBroker.getNewContractDuration();
        if (willContactAtLeastOne) {
            // if at least one producer can supply this broker, create a draft contract
            EnergyContractProposal ec = EnergyContractProposal.makeContractDraft(myBroker.getAID(), newContractDuration);
            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }

            v.add(cfp);
        }

        return v;
    }

    /**
     * In this function the Broker checks if all the Producers he contacted are able to enter in a contract
     * with him and acts accordingly.
     *
     * @param responses   Each response from the Producers.
     * @param acceptances A Vector where all the messages will be stored to answer the Producers.
     */
    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        for (Object response : responses) {
            ACLMessage received = ((ACLMessage) response);
            ACLMessage reply = ((ACLMessage) response).createReply();

            if (received.getPerformative() == ACLMessage.PROPOSE) {
                try {
                    EnergyContractProposal ec = (EnergyContractProposal) received.getContentObject();
                    ec.signContract(myBroker);

                    myBroker.getWorldModel().addBrokerProducerContract(ec);

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

    /**
     * This function is called when all messages from the contacted Producers have been received.
     *
     * @param resultNotifications The notifications of the messages received.
     */
    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }
}
