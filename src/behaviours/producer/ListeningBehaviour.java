package behaviours.producer;

import agents.Producer;
import behaviours.FIPAContractNetResponder;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import sajas.core.Agent;
import utils.EnergyContract;

import java.io.IOException;

public class ListeningBehaviour extends FIPAContractNetResponder {

    @Override
    protected ACLMessage handleCfp(ACLMessage cfp) {
        ACLMessage reply = cfp.createReply();

        if(((Producer) myAgent).hasContract()){
            reply.setPerformative(ACLMessage.REFUSE);
        }
        else {
            reply.setPerformative(ACLMessage.PROPOSE);

            try {
                EnergyContract ec = (EnergyContract) cfp.getContentObject();
                 ec.updateSupplier((Producer) myAgent);
                 ec.updateEnergyAmount(((Producer) myAgent).getEnergyProductionPerMonth());
                 ec.updateEnergyCostPerUnit(((Producer) myAgent).getEneryUnitSellPrice());

                 reply.setContentObject(ec);
                // assuming we will gain this contract, to reject others
                ((Producer) myAgent).setContract(ec);


            } catch (UnreadableException e) {
                e.printStackTrace();

                System.out.println("Could not get Content Object.");
                reply.setPerformative(ACLMessage.REFUSE);
            } catch (IOException e) {
                e.printStackTrace();

                reply.setPerformative(ACLMessage.REFUSE);
            }
        }

        return reply;
    }

    @Override
    protected void handleRejectProposal(ACLMessage cfp, ACLMessage propose, ACLMessage reject) {
        // invalidate previous contract to be able to accept new ones
        ((Producer) myAgent).setContract(null);
    }

    @Override
    protected ACLMessage handleAcceptProposal(ACLMessage cfp, ACLMessage propose, ACLMessage accept) {
        ACLMessage result = accept.createReply();
        result.setPerformative(ACLMessage.INFORM);

        return result;
    }

    public ListeningBehaviour(Agent agent, MessageTemplate template) {
        super(agent, template);
    }



}
