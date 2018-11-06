package behaviours.consumer;

import agents.Consumer;
import sajas.core.behaviours.WakerBehaviour;

public class ConsumerBusinessStarter extends WakerBehaviour {

    private static final long serialVersionUID = 1L;

    public ConsumerBusinessStarter(Consumer consumer, long timeout) {
        super(consumer, timeout);
    }

    @Override
    public void onWake() {
        // initiate Communication protocol
        ConsumerContractInitiator contract = new ConsumerContractInitiator((Consumer) myAgent);
        myAgent.addBehaviour(new ConsumerContractWrapperBehaviour(contract));
    }
}
