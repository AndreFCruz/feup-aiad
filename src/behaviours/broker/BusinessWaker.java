package behaviours.broker;

import agents.Broker;
import jade.lang.acl.ACLMessage;
import sajas.core.behaviours.WakerBehaviour;

public class BusinessWaker extends WakerBehaviour {

    private static final long serialVersionUID = 1L;

    private Broker broker;

    public BusinessWaker(Broker broker, long timeout) {
        super(broker, timeout);
        this.broker = broker;
    }

    @Override
    public void onWake() {
        // initiate CNet protocol
        ContractBehaviour contract = new ContractBehaviour(broker, new ACLMessage(ACLMessage.CFP));
        myAgent.addBehaviour(new ContractBehaviourWrapper(contract, broker));
    }
}
