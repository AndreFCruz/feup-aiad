package agents;

import launchers.OurRepastLauncher;
import java.awt.*;


public class Broker extends SimpleAgent {


    public Broker(int x, int y, OurRepastLauncher model, Color c){
        super(x, y, model, c);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Broker " + this.getLocalName() + " was created.");
    }



}
