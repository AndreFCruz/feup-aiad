package agents;

import launchers.OurRepastLauncher;
import sajas.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;


/**
 * Only subscribes ONE Broker;
 * Some Consumers have higher inertia to changing Brokers;
 * Some Consumers prefer green energy;
 */
public class Consumer extends Agent implements Drawable {

    int cumulativeExpenses = 0;
    private int x;
    private int y;
    private OurRepastLauncher model;

    public Consumer(int x, int y, OurRepastLauncher model){
        super();
        this.x = x;
        this.y = y;
        this.model = model;

    }

    @Override
    protected void setup() {
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        simGraphics.drawCircle(Color.RED);
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }
}
