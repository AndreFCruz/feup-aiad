package behaviours.consumer;

import agents.Consumer;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.WrapperBehaviour;

/**
 * Encapsulates a behaviour from the Consumer.
 */
public class ConsumerContractWrapperBehaviour extends WrapperBehaviour {

    public ConsumerContractWrapperBehaviour(Behaviour wrapped) {
        super(wrapped);
    }

    /**
     * This function is called when the behaviour wrapped ends.
     *
     * @return exit code
     */
    public int onEnd() {
        if (!((Consumer) myAgent).hasBrokerService()) {
            // This behavior is responsible for choosing brokers
            ConsumerContractInitiator contract = new ConsumerContractInitiator((Consumer) myAgent);
            myAgent.addBehaviour(new ConsumerContractWrapperBehaviour(contract));
        }
        return 0;
    }
}