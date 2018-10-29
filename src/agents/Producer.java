package agents;

import launchers.OurRepastLauncher;
import java.awt.*;

public class Producer extends GenericAgent {


    public Producer(int x, int y, OurRepastLauncher model, Color c){
        super(x, y, model, c);
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Producer " + this.getLocalName() + " was created.");
    }

}
