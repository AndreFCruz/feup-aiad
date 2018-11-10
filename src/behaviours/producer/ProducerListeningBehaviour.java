package behaviours.producer;

import agents.Producer;
import behaviours.FIPAContractNetResponder;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import sajas.core.Agent;
import utils.EnergyContractProposal;

import java.io.IOException;

/**
 * Producer listens for contract proposals (Contract Net Initiators) from Brokers.
 */
public class ProducerListeningBehaviour extends FIPAContractNetResponder {

    private Producer myProducer;

    public ProducerListeningBehaviour(Producer agent) {
        super(agent);
        myProducer = agent;
    }

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) {
        ACLMessage reply = cfp.createReply();

        if (myProducer.hasContract()) {
            reply.setPerformative(ACLMessage.REFUSE);
            System.out.println("Producer refused contract as it already has one.");
            return reply;
        }

        // If Producer (self) has no contract assigned, respond with proposal
        reply.setPerformative(ACLMessage.PROPOSE);
        try {
            EnergyContractProposal ec = (EnergyContractProposal) cfp.getContentObject();
            ec = ec.makeContractProposal(
                    myAgent.getAID(),
                    myProducer.getEnergyProductionPerMonth(),
                    myProducer.getEnergyUnitSellPrice()
            );

            reply.setContentObject(ec);
            // assuming we will gain this contract, to reject others
            myProducer.setContract(ec);


        } catch (UnreadableException e) {
            e.printStackTrace();

            System.out.println("Could not get Content Object.");
            reply.setPerformative(ACLMessage.REFUSE);
        } catch (IOException e) {
            e.printStackTrace();

            reply.setPerformative(ACLMessage.REFUSE);
        }

        return reply;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        // invalidate previous contract to be able to accept new ones
        myProducer.setContract(null);
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
        return super.handleAcceptProposal(cfp, propose, accept);
    }


}
