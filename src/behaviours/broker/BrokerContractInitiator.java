package behaviours.broker;

import agents.Broker;
import agents.Producer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import launchers.EnergyMarketLauncher;
import sajas.core.AID;
import utils.EnergyContract;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class BrokerContractInitiator extends FIPAContractNetInitiator {

//    private List<String> producers;

    public BrokerContractInitiator(Broker agent) {
        super(agent);
//        producers = agent.getPromisingProducers();
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {
        ArrayList<Producer> orderedListOfPreferences = getOrderedListOfPreferences();

        Vector v = new Vector();

        boolean contactedAtLeastOne = false;
        float brokersCash = ((Broker) myAgent).getMoneyWallet().getBalance();
        for (Producer p: orderedListOfPreferences){
            // get price for producer's whole energy
            int energyProduction = p.getEnergyProductionPerMonth();
            int energyCost = p.getEnergyUnitSellPrice();

            if (brokersCash > (energyCost * energyProduction)){
                contactedAtLeastOne = true;
                cfp.addReceiver(p.getAID());
                brokersCash -= energyCost * energyProduction;
            }

        }

        if (contactedAtLeastOne){
            // if at least one producer can supply this broker, create a draft contract
            EnergyContract ec = EnergyContract.makeContractDraft((Broker) myAgent, ((Broker) myAgent).getDuration() );
            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }

            v.add(cfp);
        }
        else {
            // can't buy any more energy from anyone
            ((Broker) myAgent).setCanStillBuyEnergy(false);
        }

        // TODO: check if there is a problem when this comes empty ?
        return v;
    }

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        for (Object response : responses){
            ACLMessage msg = ((ACLMessage) response).createReply();
            if (msg.getPerformative() == ACLMessage.PROPOSE){   // performative of the answer
                try {
                    EnergyContract ec = (EnergyContract) msg.getContentObject();
                    // TODO: check if this makes sense, the transaction of energy for money...
                    // adding contract to the world model
                    ((Broker) myAgent).getWorldModel().addContract(ec);
                    ((Broker) myAgent).getMoneyWallet().withdraw(ec.getEnergyAmount()*ec.getEnergyCostPerUnit(), ec.getEnergySupplier().getMoneyWallet());
                    ((Broker) myAgent).getEnergyWallet().deposit(ec.getEnergyAmount(), ec.getEnergySupplier().getEnergyWallet());

                    msg.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    acceptances.add(msg);

                } catch (UnreadableException e) {
                    e.printStackTrace();

                    msg.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.add(msg);
                }
            }
            else {
                msg.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.add(msg);
            }

        }

    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        System.out.println(myAgent.getLocalName() + " received " + resultNotifications.size() + "results.");
    }

    private ArrayList<Producer> getOrderedListOfPreferences() {
        // getting all the agents
        ArrayList<Producer> result = ((Broker) myAgent).getWorldModel().getProducers();

        // this sorting can lead to a lot of agents trying to get the same producer
//      result.sort(Comparator.comparingInt(Producer::getEnergyUnitSellPrice).reversed());
        Collections.shuffle(result);

        return result;
    }
}
