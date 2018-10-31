package agents;

import behaviours.broker.BusinessWaker;
import behaviours.broker.ContractBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;

import java.util.ArrayList;

public class Broker extends DFRegisterAgent {

    private static final int TIMEOUT = 2000;

    private DFSearchAgent search;

    public Broker(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);
        this.search = new DFSearchAgent(model, graphicSettings);

        this.setType(AgentType.BROKER);
        this.search.setType(AgentType.PRODUCER);
    }

    @Override
    protected void setup() {
        super.setup();
        this.register();
        this.addBehaviour(new BusinessWaker(this, TIMEOUT));
    }

    public ArrayList<String> getProducers() {
        ArrayList<String> producersNames = new ArrayList<>();

        for (DFAgentDescription p : this.search.searchAndGet()) {
            producersNames.add(p.getName().getLocalName());
        }
        return producersNames;
    }

}
