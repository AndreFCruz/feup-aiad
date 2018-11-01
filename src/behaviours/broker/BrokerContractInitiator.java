package behaviours.broker;

import agents.Broker;
import agents.Producer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import launchers.EnergyMarketLauncher;
import sajas.core.AID;
import utils.EnergyContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

/**
 *  This class represents the behaviour of the Brokers when they are trying to
 *  talk with the Producers to get energy supplied.
 */
public class BrokerContractInitiator extends FIPAContractNetInitiator {

//    private List<String> producers;

    public BrokerContractInitiator(Broker agent) {
        super(agent);
//        producers = agent.getPromisingProducers();
    }

    /**
     * In this function the Broker decides which Producers he want to contact.
     *
     * @param cfp A template message.
     * @return A Vector of messages that will be delivered.
     */
    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        ArrayList<Producer> orderedListOfPreferences = getOrderedListOfPreferences();

        Vector v = new Vector();

        boolean contactedAtLeastOne = false;
        float brokersCash = ((Broker) myAgent).getMoneyWallet().getBalance();
        for (Producer p: orderedListOfPreferences){
            // get price for producer's whole energy
            int energyProduction = p.getEnergyProductionPerMonth();
            int energyCost = p.getEnergyUnitSellPrice();

            if (brokersCash > (energyCost * energyProduction)){
                contactedAtLeastOne = true;
                cfp.addReceiver(p.getAID());
                brokersCash -= energyCost * energyProduction;
            }

        }

        if (contactedAtLeastOne){
            // if at least one producer can supply this broker, create a draft contract
            EnergyContract ec = EnergyContract.makeContractDraft((Broker) myAgent, ((Broker) myAgent).getDuration() );
            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }

            v.add(cfp);
        }
        else {
            // can't buy any more energy from anyone
            ((Broker) myAgent).setCanStillBuyEnergy(false);
        }

        // TODO: check if there is a problem when this comes empty ?
        return v;
    }

    /**
     * In this function the Broker checks if all the Producers he contacted are able to enter in a contract
     * with him and acts accordingly.
     *
     * @param responses Each response from the Producers.
     * @param acceptances A Vector where all the messages will be stored to answer the Producers.
     */
    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        for (Object response : responses){
            ACLMessage msg = ((ACLMessage) response).createReply();
            if (msg.getPerformative() == ACLMessage.PROPOSE){   // performative of the answer
                try {
                    EnergyContract ec = (EnergyContract) msg.getContentObject();
                    // TODO: check if this makes sense, the transaction of energy for money...
                    // adding contract to the world model
                    ((Broker) myAgent).getWorldModel().addContract(ec);

                    msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptances.add(msg);

                } catch (UnreadableException e) {
                    e.printStackTrace();

                    msg.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.add(msg);
                }
            }
            else {
                msg.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.add(msg);
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
        System.out.println(myAgent.getLocalName() + " received " + resultNotifications.size() + "results.");
    }

    /**
     *
     * @return An ordered ArrayList of the preferred order to contact the Producers.
     */
    private ArrayList<Producer> getOrderedListOfPreferences() {
        // getting all the agents
        ArrayList<Producer> result = ((Broker) myAgent).getWorldModel().getProducers();

        // this sorting can lead to a lot of agents trying to get the same producer
//      result.sort(Comparator.comparingInt(Producer::getEnergyUnitSellPrice).reversed());
        Collections.shuffle(result);

        return result;
    }
}
