package behaviours.broker;

import agents.Broker;
import jade.lang.acl.ACLMessage;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.WrapperBehaviour;

public class ContractBehaviourWrapper extends WrapperBehaviour {

    public ContractBehaviourWrapper(Behaviour wrapped, Broker agent) {
        super(wrapped);
    }

    public int onEnd() {
        // Missing Logic here - only call to brokers not full?
        myAgent.addBehaviour(new ContractBehaviour((Broker) myAgent, new ACLMessage(ACLMessage.CFP)));
        return 0;
    }

}