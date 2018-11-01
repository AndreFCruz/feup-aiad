package behaviours.broker;

import agents.Broker;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.WrapperBehaviour;

public class ContractBehaviourWrapper extends WrapperBehaviour {

    public ContractBehaviourWrapper(Behaviour wrapped) {
        super(wrapped);
    }

    public int onEnd() {
        if(((Broker) myAgent).isSatisfied()){
            // TODO: put here next step logic
            // AKA: listening for consumer requests (?)
        }
        else {
            // This behavior is responsible for choosing producers
            BrokerContractInitiator contract = new BrokerContractInitiator((Broker) myAgent);
            myAgent.addBehaviour(new ContractBehaviourWrapper(contract));
        }

        return 0;
    }

}