package agents;

import launchers.OurRepastLauncher;
import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;

public class SimpleAgent extends Agent implements Drawable {


    class SimpleBehaviour extends Behaviour {

        private int count = 0;

        @Override
        public void action() {
            System.out.println(this.getAgent().getAID() + " : " + this.count++);
        }

        @Override
        public boolean done() {
            System.out.println(this.getAgent().getAID() + " : Bye cruel world ??");
            return false;
        }

        @Override
        public int onEnd() {
            System.out.println(this.getAgent().getAID() + " : Bye cruel world...");
            return super.onEnd();
        }

        @Override
        public void onStart() {
            super.onStart();
            System.out.println(this.getAgent().getAID() + " : Hello World");
        }
    }


    private static int agentCount = 0;
    protected int id;
    protected int x;
    protected int y;
    protected Color color;
    protected OurRepastLauncher worldModel;

    int cumulativeExpenses = 0;
    int cumulativeEarnings = 0;

    SimpleAgent(int x, int y, OurRepastLauncher model, Color c) {
        super();
        this.x = x;
        this.y = y;
        this.color = c;
        this.worldModel = model;
        this.id = agentCount++;
    }

    @Override
    protected void setup() {
        super.setup();
        this.addBehaviour(new SimpleBehaviour());
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    /*
    @Override
    public void step() {
        System.out.println(this.id + " Hello World!");
    }
    */

    @Override
    public void draw(SimGraphics simGraphics) {
        simGraphics.drawCircle(color);
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
