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
        boolean willSignFutureContract = false;

        if (myConsumer.hasEnergyContract() && myConsumer.getContractMonthsLeft() > 2)
            return v;
        if (myConsumer.hasEnergyContract())
            willSignFutureContract = true;

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
            if (willSignFutureContract)
                ec.setStartDate(myConsumer.getEnergyContract().getEndDate() + 1);

            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }
            v.add(cfp);
        }

        return v;
    }

    // TODO when receiving responses, add Future Contract if start date is in the future

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        for (Object response : responses) {
            ACLMessage received = ((ACLMessage) response);
            ACLMessage reply = ((ACLMessage) response).createReply();

            // If meanwhile a contract was signed
            if (received.getPerformative() == ACLMessage.PROPOSE && !myConsumer.hasEnergyContract()) {
                try {
                    EnergyContractProposal ec = (EnergyContractProposal) received.getContentObject();
                    ec.signContract(myAgent);

                    myConsumer.getWorldModel().addConsumerBrokerContractFromProposal(ec);

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
