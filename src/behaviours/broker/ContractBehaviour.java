package behaviours.broker;

import agents.Broker;
import agents.Producer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import launchers.EnergyMarketLauncher;
import sajas.core.AID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class ContractBehaviour extends FIPAContractNetInitiator {

    private ArrayList<String> producers;

    public ContractBehaviour(Broker agent) {
        super(agent);
        producers = agent.getProducers();
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        ArrayList<Producer> orderedListOfPreferences = getOrderedListOfPreferences();

        Vector v = new Vector();

        for (String p : producers) {
            cfp.addReceiver(new AID(p, false));
        }
        cfp.setContent("What energy do you have?");

        v.add(cfp);
        return v;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {
        super.handleAllResponses(responses, acceptances);
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }

    private ArrayList<Producer> getOrderedListOfPreferences() {
        // getting all the agents
        ArrayList<Producer> result = ((Broker)myAgent).getWorldModel().getProducers();
        // ordering them
        result.sort(Comparator.comparingInt(Producer::getEneryUnitSellPrice).reversed());

        return result;
    }
}
