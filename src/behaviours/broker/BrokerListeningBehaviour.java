package behaviours.broker;

import agents.Broker;
import behaviours.FIPAContractNetResponder;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.EnergyContractProposal;

import java.io.IOException;

/**
 * Brokers listens for contract proposals from Consumers.
 */
public class BrokerListeningBehaviour extends FIPAContractNetResponder {

    private Broker myBroker;

    public BrokerListeningBehaviour(Broker agent) {
        super(agent);
        myBroker = agent;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) {
        ACLMessage reply = cfp.createReply();

        try {
            EnergyContractProposal ec = (EnergyContractProposal) cfp.getContentObject();

            if ( myBroker.monthsThatMayFulfillContract((int) ec.getEnergyAmountPerMonth()) >= 2 ) {
                ec = ec.makeContractProposal(
                        myBroker.getAID(),
                        ec.getEnergyAmountPerCycle(),
                        myBroker.getEnergyUnitSellPrice()
                );
                reply.setPerformative(ACLMessage.PROPOSE);
                reply.setContentObject(ec);

            } else
                reply.setPerformative(ACLMessage.REFUSE);

        } catch (UnreadableException e) {
            e.printStackTrace();

            System.out.println("Could not get Content Object.");
            reply.setPerformative(ACLMessage.REFUSE);
        } catch (IOException e) {
            e.printStackTrace();

            reply.setPerformative(ACLMessage.REFUSE);
        }

        return reply;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        super.handleRejectProposal(cfp, propose, reject);
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
        return super.handleAcceptProposal(cfp, propose, accept);
    }
}
