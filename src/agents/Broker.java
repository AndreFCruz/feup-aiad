package agents;

import behaviours.broker.ContractBehaviour;
import jade.lang.acl.ACLMessage;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;


public class Broker extends DFRegisterAgent {

    private DFSearchAgent searchService;

    public Broker(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);
        this.searchService = new DFSearchAgent(model, graphicSettings);

        this.setType(AgentType.BROKER);
        this.searchService.setType(AgentType.PRODUCER);
        addBehaviour(new ContractBehaviour(this, new ACLMessage(ACLMessage.CFP)));
    }

    @Override
    protected void setup() {
        super.setup();
        this.register();
        this.searchService.search();
        System.out.println("Broker " + this.getLocalName() + " was created.");
    }

}
