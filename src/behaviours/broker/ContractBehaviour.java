package behaviours.broker;

import agents.Broker;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import sajas.core.AID;
import sajas.core.Agent;

import java.util.ArrayList;
import java.util.Vector;

public class ContractBehaviour extends FIPAContractNetInitiator {

    private ArrayList<String> producers;

    public ContractBehaviour(Broker agent, ACLMessage msg) {
        super(agent, msg);
        this.producers = agent.getProducers();
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        Vector v = new Vector();

        for (String p : this.producers) {
            System.out.println("fds");
            cfp.addReceiver(new AID(p, false));
        }
        cfp.setContent("Please trade with me");
        System.out.println("wur " + this.producers);
        v.add(cfp);

        return v;
    }
}
