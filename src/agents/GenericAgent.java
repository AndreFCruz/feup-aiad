package agents;

import launchers.EnergyMarketLauncher;
import sajas.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import utils.GraphicSettings;

import java.util.ArrayList;

public class GenericAgent extends Agent implements Drawable {


    ArrayList<GenericAgent> contacts;

    private static int agentCount = 0;
    protected int id;
    protected EnergyMarketLauncher worldModel;
    protected GraphicSettings graphicSettings;


    GenericAgent(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super();

        this.worldModel = model;
        this.graphicSettings = graphicSettings;
        this.contacts = new ArrayList<>();
        this.id = agentCount++;
    }

    @Override
    protected void setup() {
        super.setup();
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

    public void addContact(GenericAgent s) {
        this.contacts.add(s);
    }

    @Override
    public void draw(SimGraphics simGraphics) {
        simGraphics.drawCircle(graphicSettings.color);
        for (GenericAgent s : contacts) {

            simGraphics.drawLink(graphicSettings.color,
                    4 * graphicSettings.x,
                    4 * (s.graphicSettings.x + graphicSettings.x) / 2,
                    graphicSettings.y,
                    (s.graphicSettings.y + graphicSettings.y) / 2);
        }
    }

    @Override
    public int getX() {
        return graphicSettings.x;
    }

    @Override
    public int getY() {
        return graphicSettings.y;
    }

}
