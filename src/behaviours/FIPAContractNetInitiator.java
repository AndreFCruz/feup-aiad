package behaviours;

import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.proto.ContractNetInitiator;

import java.util.Vector;

public abstract class FIPAContractNetInitiator extends ContractNetInitiator {

    public FIPAContractNetInitiator(Agent agent, ACLMessage msg) {
        super(agent, msg);
    }

    protected abstract Vector prepareCfps(ACLMessage cfp);

    protected void handleAllResponses(Vector responses, Vector acceptances) {

        System.out.println("got " + responses.size() + " responses!");

        for (int i = 0; i < responses.size(); i++) {
            ACLMessage msg = ((ACLMessage) responses.get(i)).createReply();
            msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            acceptances.add(msg);
        }
    }

    protected void handleAllResultNotifications(Vector resultNotifications) {
        System.out.println("got " + resultNotifications.size() + " result notifs!");
    }

}
