package agents;

import launchers.OurRepastLauncher;
import java.awt.*;

/**
 * Only subscribes ONE Broker;
 * Some Consumers have higher inertia to changing Brokers;
 * Some Consumers prefer green energy;
 */
public class Consumer extends SimpleAgent{


    public Consumer(int x, int y, OurRepastLauncher model, Color c){
        super(x, y, model, c);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Consumer " + this.getLocalName() + " was created.");
    }

}
