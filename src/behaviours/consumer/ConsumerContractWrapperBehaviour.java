package behaviours.consumer;

import agents.Consumer;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.WrapperBehaviour;

public class ConsumerContractWrapperBehaviour extends WrapperBehaviour {

    public ConsumerContractWrapperBehaviour(Behaviour wrapped) {
        super(wrapped);
    }

    public int onEnd() {
        if (((Consumer) myAgent).hasBrokerService()) {
            // TODO: put here next step logic
            // AKA: listening for consumer requests (?)
        } else {
            // This behavior is responsible for choosing producers
            ConsumerContractInitiator contract = new ConsumerContractInitiator((Consumer) myAgent);
            myAgent.addBehaviour(new ConsumerContractWrapperBehaviour(contract));
        }

        return 0;
    }
}
