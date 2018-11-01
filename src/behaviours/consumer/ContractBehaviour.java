package behaviours.consumer;

import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import sajas.core.Agent;

import java.util.Vector;

public class ContractBehaviour extends FIPAContractNetInitiator {
    public ContractBehaviour(Agent agent) {
        super(agent);
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        return null;
    }
}
