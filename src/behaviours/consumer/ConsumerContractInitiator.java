package behaviours.consumer;

import agents.Broker;
import agents.Consumer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.EnergyContractProposal;
import utils.OurPair;

import java.io.IOException;
import java.util.*;

public class ConsumerContractInitiator extends FIPAContractNetInitiator {

    private Consumer myConsumer;

    public ConsumerContractInitiator(Consumer agent) {
        super(agent);
        myConsumer = agent;
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {

        Vector<ACLMessage> v = new Vector<>();

        List<Broker> orderedListOfPreferences = myConsumer.getBrokersByPreference();
        boolean contactedAtLeastOne = false;

        // If already has an associated Broker
        if (myConsumer.hasBrokerService()) {
            return v;
        }

        for (Broker b : orderedListOfPreferences) {
            if (b.monthsThatMayFulfillContract(myConsumer.getEnergyConsumptionPerMonth()) > 1) {
                contactedAtLeastOne = true;
                cfp.addReceiver(b.getAID());
            }
        }

        int newContractDuration = myConsumer.getNewContractDuration();
        if (contactedAtLeastOne) {
            EnergyContractProposal ec = EnergyContractProposal.makeContractDraft(
                    myConsumer.getAID(),
                    newContractDuration
            );
            ec.updateEnergyAmount(myConsumer.getEnergyConsumptionPerMonth());

            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }
            v.add(cfp);
        }

        return v;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        ArrayList<OurPair<ACLMessage, EnergyContractProposal>> potentialAcceptances = new ArrayList<>();

        for (Object response : responses) {
            ACLMessage received = ((ACLMessage) response);
            ACLMessage reply = ((ACLMessage) response).createReply();

            // If meanwhile a contract was signed
            if (received.getPerformative() == ACLMessage.PROPOSE && !myConsumer.hasBrokerService()) {
                try {
                    EnergyContractProposal ec = (EnergyContractProposal) received.getContentObject();
                    // add potential acceptances
                    potentialAcceptances.add(new OurPair<>(reply, ec));

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

        // order acceptances
        Collections.sort(potentialAcceptances, (o1, o2) -> Float.compare(o1.second.getMonthlyEnergyCost(), o2.second.getMonthlyEnergyCost()));
        float percentage = 0.2f;
        int numberOfBrokers = Math.max((int) (percentage * potentialAcceptances.size()), 1);
        Random rand = new Random();
        int indexToChoose = rand.nextInt(numberOfBrokers);

        for (int i = 0; i < potentialAcceptances.size(); ++i){
            OurPair<ACLMessage, EnergyContractProposal> pair = potentialAcceptances.get(i);
            if (i == indexToChoose){
                pair.second.signContract(myAgent);
                myConsumer.setHasBrokerService(true);
                myConsumer.getWorldModel().addConsumerBrokerContract(pair.second);
                pair.first.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            } else {
                pair.first.setPerformative(ACLMessage.REJECT_PROPOSAL);
            }
            acceptances.add(pair.first);
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }
}
