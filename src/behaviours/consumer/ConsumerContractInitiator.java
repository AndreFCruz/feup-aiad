package behaviours.consumer;

import agents.Broker;
import agents.Consumer;
import behaviours.FIPAContractNetInitiator;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import utils.EnergyContractProposal;
import utils.OurPair;

import java.io.IOException;
import java.util.*;

public class ConsumerContractInitiator extends FIPAContractNetInitiator {

    private Consumer myConsumer;

    private static float CHEAPEST_CONTRACTS_THRESHOLD = 0.5f;

    public ConsumerContractInitiator(Consumer agent) {
        super(agent);
        myConsumer = agent;
    }

    @Override
    protected Vector prepareCfps(ACLMessage cfp) {

        Vector<ACLMessage> v = new Vector<>();

        List<Broker> orderedListOfPreferences = myConsumer.getBrokersByPreference();

        boolean contactedAtLeastOne = false;
        boolean willSignFutureContract = false;

        if ( (myConsumer.hasEnergyContract() && myConsumer.getContractMonthsLeft() > 2) || myConsumer.hasFutureContractSigned() )
            return v;
        if (myConsumer.hasEnergyContract())
            willSignFutureContract = true;

        for (Broker b : orderedListOfPreferences) {
            if (b.monthsThatMayFulfillContract(myConsumer.getEnergyConsumptionPerMonth()) > 1) {
                contactedAtLeastOne = true;
                cfp.addReceiver(b.getAID());
            }
        }

        int newContractDuration = myConsumer.getNewContractDuration();
        if (contactedAtLeastOne) {
            EnergyContractProposal ec = EnergyContractProposal.makeContractDraft(
                    myConsumer.getAID(),
                    newContractDuration
            );
            ec.updateEnergyAmount(myConsumer.getEnergyConsumptionPerMonth());
            if (willSignFutureContract)
                ec.setStartDate(myConsumer.getEnergyContract().getEndDate() + 1);
            else
                ec.setStartDate((int) myConsumer.getWorldModel().getTickCount());

            try {
                cfp.setContentObject(ec);
            } catch (IOException e) {
                e.printStackTrace();
            }
            v.add(cfp);
        }

        return v;
    }

    // TODO when receiving responses, add Future Contract if start date is in the future

    @Override
    protected void handleAllResponses(Vector responses, Vector acceptances) {

        ArrayList<OurPair<ACLMessage, EnergyContractProposal>> potentialAcceptances = new ArrayList<>();

        for (Object response : responses) {
            ACLMessage received = ((ACLMessage) response);
            ACLMessage reply = ((ACLMessage) response).createReply();

            // If meanwhile a contract was signed
            if (received.getPerformative() == ACLMessage.PROPOSE && !myConsumer.hasEnergyContract()) {
                try {
                    EnergyContractProposal ec = (EnergyContractProposal) received.getContentObject();

                    // add potential acceptances
                    potentialAcceptances.add(new OurPair<>(reply, ec));
                } catch (UnreadableException e) {
                    e.printStackTrace();

                    reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                    acceptances.add(reply);
                }
            } else {
                reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
                acceptances.add(reply);
            }
        }

        // order acceptances
        Collections.sort(potentialAcceptances, (o1, o2) -> Float.compare(o1.second.getMonthlyEnergyCost(), o2.second.getMonthlyEnergyCost()));
        int numberOfBrokers = Math.max((int) (CHEAPEST_CONTRACTS_THRESHOLD * potentialAcceptances.size()), 1);

        Random rand = new Random();
        int indexToChoose = rand.nextInt(numberOfBrokers);

        for (int i = 0; i < potentialAcceptances.size(); ++i){
            OurPair<ACLMessage, EnergyContractProposal> pair = potentialAcceptances.get(i);
            EnergyContractProposal ecp = pair.second;
            if (i == indexToChoose){
                ecp.signContract(myAgent);
//                if (ecp.getStartDate() == null){
//                    ecp.signContract(myAgent);
//                    myConsumer.getWorldModel().addConsumerBrokerContractFromProposal(ecp);
//                }
//                else {
                    if (ecp.getStartDate() > myConsumer.getWorldModel().getTickCount())
                        myConsumer.getWorldModel().addFutureConsumerBrokerContractFromProposal(ecp);
                    else {
                        myConsumer.getWorldModel().addConsumerBrokerContractFromProposal(ecp);
                    }
//                }


                pair.first.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
            } else {
                pair.first.setPerformative(ACLMessage.REJECT_PROPOSAL);
            }
            acceptances.add(pair.first);
        }
    }

    @Override
    protected void handleAllResultNotifications(Vector resultNotifications) {
        super.handleAllResultNotifications(resultNotifications);
    }
}
