package behaviours.broker;

import agents.Broker;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.WrapperBehaviour;

public class ContractBehaviourWrapper extends WrapperBehaviour {

    public ContractBehaviourWrapper(Behaviour wrapped) {
        super(wrapped);
    }

    public int onEnd() {
        // Missing Logic here - try to find more producers until when?
        // This behavior is responsible for choosing behaviors (?)
        myAgent.addBehaviour(new ContractBehaviour((Broker) myAgent));
        return 0;
    }

}