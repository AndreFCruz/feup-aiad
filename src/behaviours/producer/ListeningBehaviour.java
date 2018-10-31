package behaviours.producer;

import behaviours.FIPAContractNetResponder;
import jade.lang.acl.MessageTemplate;
import sajas.core.Agent;

public class ListeningBehaviour extends FIPAContractNetResponder {

    public ListeningBehaviour(Agent agent, MessageTemplate template) {
        super(agent, template);
    }
}
