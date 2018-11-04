package behaviours.consumer;

import agents.Broker;
import agents.Consumer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.EnergyContractProposal;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class ConsumerContractInitiator extends FIPAContractNetInitiator {

    public ConsumerContractInitiator(Consumer agent) {
        super(agent);
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {

        Vector<ACLMessage> v = new Vector<>();
        Consumer c = (Consumer) myAgent;

        List<Broker> orderedListOfPreferences = c.getOrderedBrokers();
        boolean contactedAtLeastOne = false;

        // If already has an associated Broker
        if (c.hasBrokerService()) {
            return v;
        }

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

            // If meanwhile a contract was signed
            if (received.getPerformative() == ACLMessage.PROPOSE && !((Consumer) myAgent).hasBrokerService()) {
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
}
