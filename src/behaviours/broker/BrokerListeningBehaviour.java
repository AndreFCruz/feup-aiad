package behaviours.broker;

import behaviours.FIPAContractNetResponder;
import jade.lang.acl.ACLMessage;
import sajas.core.Agent;

/**
 * Brokers listens for contract proposals from Consumers.
 */
public class BrokerListeningBehaviour extends FIPAContractNetResponder {
    public BrokerListeningBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) {
        ACLMessage reply = cfp.createReply();

        // Check if Broker (self) can fulfill energy order

        return super.handleCfp(cfp);
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
