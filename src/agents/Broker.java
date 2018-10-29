package agents;

import launchers.EnergyMarketLauncher;
import utils.GraphicSettings;


public class Broker extends GenericAgent {


    public Broker(EnergyMarketLauncher model, GraphicSettings graphicSettings){
        super(model, graphicSettings);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Broker " + this.getLocalName() + " was created.");
    }



}
