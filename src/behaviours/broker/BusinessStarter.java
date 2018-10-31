package behaviours.broker;

import agents.Broker;
import sajas.core.behaviours.WakerBehaviour;

public class BusinessStarter extends WakerBehaviour {

    private static final long serialVersionUID = 1L;

    public BusinessStarter(Broker broker, long timeout) {
        super(broker, timeout);
    }

    @Override
    public void onWake() {
        // initiate Communication protocol
        ContractBehaviour contract = new ContractBehaviour((Broker) myAgent);
        myAgent.addBehaviour(new ContractBehaviourWrapper(contract));
    }
}
