package agents;

import launchers.EnergyMarketLauncher;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;

import java.awt.*;
import java.util.ArrayList;

public class GenericAgent extends Agent implements Drawable {

    ArrayList<GenericAgent> contacts;

    class SimpleBehaviour extends Behaviour {

        private int count = 0;

        @Override
        public void action() {
//            System.out.println(this.getAgent().getAID() + " : " + this.count++);
        }

        @Override
        public boolean done() {
//            System.out.println(this.getAgent().getAID() + " : Bye cruel world ??");
            return false;
        }

        @Override
        public int onEnd() {
//            System.out.println(this.getAgent().getAID() + " : Bye cruel world...");
            return super.onEnd();
        }

        @Override
        public void onStart() {
            super.onStart();
//            System.out.println(this.getAgent().getAID() + " : Hello World");
        }
    }


    private static int agentCount = 0;
    protected int id;
    protected int x;
    protected int y;
    protected Color color;
    protected EnergyMarketLauncher worldModel;

    int cumulativeExpenses = 0;
    int cumulativeEarnings = 0;

    GenericAgent(int x, int y, EnergyMarketLauncher model, Color c) {
        super();
        this.contacts = new ArrayList<>();
        this.x = x;
        this.y = y;
        System.out.println("(x,y) = " + x + "," + y);
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

    public void addContact(GenericAgent s){
        this.contacts.add(s);
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        simGraphics.drawCircle(color);
        for (GenericAgent s: contacts) {
            // TODO: figure out why this 4 is necessary here...
            simGraphics.drawLink(color, 4*this.x, 4*(s.x+this.x)/2, this.y, (s.y+this.y)/2);
        }
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
