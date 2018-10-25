package agents;


import launchers.OurRepastLauncher;
import sajas.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;

public class Producer extends Agent implements Drawable {

    int cumulativeExpenses = 0;
    int cumulativeEarnings = 0;
    private int x;
    private int y;
    private OurRepastLauncher model;

    public Producer(int x, int y, OurRepastLauncher model){
        super();
        this.x = x;
        this.y = y;
        this.model = model;

    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Producer " + this.getLocalName() + " was created.");
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        simGraphics.drawCircle(Color.GREEN);
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
