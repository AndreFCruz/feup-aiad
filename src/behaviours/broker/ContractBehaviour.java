package behaviours.broker;

import agents.Broker;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import sajas.core.AID;

import java.util.ArrayList;
import java.util.Vector;

public class ContractBehaviour extends FIPAContractNetInitiator {

    private ArrayList<String> producers;

    public ContractBehaviour(Broker agent) {
        super(agent);
        producers = agent.getProducers();
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        Vector v = new Vector();

        for (String p : producers) {
            cfp.addReceiver(new AID(p, false));
        }
        cfp.setContent("What energy do you have?");

        v.add(cfp);
        return v;
    }
}
