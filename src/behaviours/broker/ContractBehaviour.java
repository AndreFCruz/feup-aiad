package behaviours.broker;

import agents.Broker;
import agents.Producer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import launchers.EnergyMarketLauncher;
import sajas.core.AID;
import utils.EnergyContract;

import java.io.IOException;
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

        int contactXProducers = Math.min(orderedListOfPreferences.size(), EnergyMarketLauncher.SEARCH_TOP_X_PRODUCERS);

        for (Producer p: orderedListOfPreferences){
            if (contactXProducers == 0)
                break;

            cfp.addReceiver(p.getAID());

            contactXProducers -= 1;
        }

        EnergyContract ec = EnergyContract.makeContractDraft((Broker) myAgent, ((Broker) myAgent).getDuration() );
        try {
            cfp.setContentObject(ec);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        ArrayList<Producer> result = ((Broker) myAgent).getWorldModel().getProducers();
        // ordering them
        result.sort(Comparator.comparingInt(Producer::getEneryUnitSellPrice).reversed());

        return result;
    }
}
