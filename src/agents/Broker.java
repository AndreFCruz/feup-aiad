package agents;

import behaviours.broker.ContractBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;

import java.util.ArrayList;

public class Broker extends DFRegisterAgent {

    private DFSearchAgent search;

    public Broker(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);
        this.search = new DFSearchAgent(model, graphicSettings);

        this.setType(AgentType.BROKER);
        this.search.setType(AgentType.PRODUCER);

        addBehaviour(new ContractBehaviour(this, new ACLMessage(ACLMessage.CFP)));
    }

    @Override
    protected void setup() {
        super.setup();
        this.register();
        this.search.search();
    }

    public ArrayList<String> getProducers() {
        ArrayList<String> producersNames = new ArrayList<>();

        for( DFAgentDescription p : this.search.getResult()) {
            producersNames.add(p.getName().getLocalName());
        }
        return producersNames;
    }

}
