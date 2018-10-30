package behaviours.broker;

import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import sajas.core.Agent;

public class ContractBehaviour extends FIPAContractNetInitiator {

    public ContractBehaviour(Agent agent, ACLMessage msg) {
        super(agent, msg);
    }
}
