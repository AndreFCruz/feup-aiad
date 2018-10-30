package agents;

import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;


public class Broker extends DFRegisterAgent {

    private DFSearchAgent searchService;

    public Broker(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);

        //For DFServices
        this.setType(AgentType.BROKER);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Broker " + this.getLocalName() + " was created.");
    }


}
