package behaviours.consumer;

import agents.Consumer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;

import java.util.List;
import java.util.Vector;

public class ConsumerContractInitiator extends FIPAContractNetInitiator {

    List<String> brokers; // TODO get Brokers instead of Broker names from Consumer.getBrokers

    public ConsumerContractInitiator(Consumer agent) {
        super(agent);
        brokers = agent.getPromisingBrokers();
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        // TODO
        return null;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        // TODO
        super.handleAllResponses(responses, acceptances);
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        // TODO
        super.handleAllResultNotifications(resultNotifications);
    }
}
