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

    protected Vector prepareCfps(ACLMessage cfp) {
        Vector v = new Vector();

        cfp.addReceiver(new AID("a1", false));
        cfp.addReceiver(new AID("a2", false));
        cfp.addReceiver(new AID("a3", false));
        cfp.setContent("this is a call...");

        v.add(cfp);

        return v;
    }

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
