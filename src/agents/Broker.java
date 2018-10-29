package agents;

import launchers.EnergyMarketLauncher;
import java.awt.*;


public class Broker extends GenericAgent {


    public Broker(int x, int y, EnergyMarketLauncher model, Color c){
        super(x, y, model, c);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Broker " + this.getLocalName() + " was created.");
    }



}
