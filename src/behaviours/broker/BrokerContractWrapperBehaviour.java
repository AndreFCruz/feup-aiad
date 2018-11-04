package behaviours.broker;

import agents.Broker;
import sajas.core.behaviours.Behaviour;
import sajas.core.behaviours.WrapperBehaviour;

public class BrokerContractWrapperBehaviour extends WrapperBehaviour {

    public BrokerContractWrapperBehaviour(Behaviour wrapped) {
        super(wrapped);
    }

    public int onEnd() {
        /*if (!((Broker) myAgent).canStillBuyEnergy()) {
            // TODO: put here next step logic
            // AKA: listening for consumer requests (?)
        } else {
            // This behavior is responsible for choosing producers
            BrokerContractInitiator contract = new BrokerContractInitiator((Broker) myAgent);
            myAgent.addBehaviour(new BrokerContractWrapperBehaviour(contract));
        }*/
        BrokerContractInitiator contract = new BrokerContractInitiator((Broker) myAgent);
        myAgent.addBehaviour(new BrokerContractWrapperBehaviour(contract));

        return 0;
    }

}