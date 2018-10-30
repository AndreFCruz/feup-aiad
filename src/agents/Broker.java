package agents;

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
    }

    @Override
    protected void setup() {
        super.setup();
        this.register();
        this.searchService.search();
        System.out.println("Broker " + this.getLocalName() + " was created.");
    }

}
