package behaviours.broker;

import agents.Broker;
import sajas.core.behaviours.WakerBehaviour;

public class BrokerBusinessStarter extends WakerBehaviour {

    private static final long serialVersionUID = 1L;

    public BrokerBusinessStarter(Broker broker, long timeout) {
        super(broker, timeout);
    }

    @Override
    public void onWake() {
        // initiate Communication protocol
        BrokerContractInitiator contract = new BrokerContractInitiator((Broker) myAgent);
        myAgent.addBehaviour(new BrokerContractWrapperBehaviour(contract));
    }
}
