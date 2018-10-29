package agents;

import launchers.EnergyMarketLauncher;
import java.awt.*;

public class Producer extends GenericAgent {


    public Producer(int x, int y, EnergyMarketLauncher model, Color c){
        super(x, y, model, c);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Producer " + this.getLocalName() + " was created.");
    }

}
