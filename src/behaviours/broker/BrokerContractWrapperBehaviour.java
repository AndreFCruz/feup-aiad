package behaviours.broker;

import agents.Broker;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.WrapperBehaviour;

public class BrokerContractWrapperBehaviour extends WrapperBehaviour {

    public BrokerContractWrapperBehaviour(Behaviour wrapped) {
        super(wrapped);
    }

    public int onEnd() {

        BrokerContractInitiator contract = new BrokerContractInitiator((Broker) myAgent);
        myAgent.addBehaviour(new BrokerContractWrapperBehaviour(contract));

        return 0;
    }

}