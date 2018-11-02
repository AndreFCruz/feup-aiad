package behaviours;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import sajas.core.Agent;
import sajas.proto.ContractNetResponder;

public abstract class FIPAContractNetResponder extends ContractNetResponder {

    public FIPAContractNetResponder(Agent agent) {
        super(agent, MessageTemplate.MatchPerformative(ACLMessage.CFP));
    }

    protected ACLMessage handleCfp(ACLMessage cfp) {
        ACLMessage reply = cfp.createReply();
        reply.setPerformative(ACLMessage.PROPOSE);
        reply.setContent("I will do it for free!!!");
        // ...
        return reply;
    }

    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        System.out.println(myAgent.getLocalName() + " got a reject...");
    }

    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
        System.out.println(myAgent.getLocalName() + " got an accept!");
        ACLMessage result = accept.createReply();
        result.setPerformative(ACLMessage.INFORM);
        result.setContent("this is the result");

        return result;
    }
}
