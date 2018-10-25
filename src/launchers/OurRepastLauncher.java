package launchers;


import agents.Broker;
import agents.Consumer;
import agents.Producer;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;

import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class OurRepastLauncher extends Repast3Launcher{

    private static final int NUMPRODUCERS = 10;
    private static final int NUMBROKERS = 5;
    private static final int NUMCONSUMERS = 100;

    private ContainerController mainContainer;
    private Random rand;

    private DisplaySurface displaySurface;
    private Object2DGrid world;
    private int worldWidth = 500;
    private int worldHeight = 300;

    private ArrayList<Producer> producers;
    private ArrayList<Broker> brokers;
    private ArrayList<Consumer> consumers;


    public OurRepastLauncher() {
        rand = new Random();
    }

//    @SuppressWarnings("unchecked")
    public void setup() { // called after constructor
        super.setup();

        if (displaySurface != null)
            displaySurface.dispose();

        String surfaceName = "Energy Market - World";

        displaySurface = new DisplaySurface(this, surfaceName);
        registerDisplaySurface(surfaceName, displaySurface);

    }

    public void begin() { // called when "Play" pressed on repast gui

        modelConstructor();
        displayConstructor();

        super.begin();
    }

    private void modelConstructor() {
        world = new Object2DGrid(worldWidth, worldHeight);

        producers = new ArrayList<>();
        brokers = new ArrayList<>();
        consumers = new ArrayList<>();

    }

    private void displayConstructor() {
        Object2DDisplay displayProducers = new Object2DDisplay(world);
        displayProducers.setObjectList(producers);
        displaySurface.addDisplayableProbeable(displayProducers, "show producers");

        Object2DDisplay displayBrokers = new Object2DDisplay(world);
        displayBrokers.setObjectList(brokers);
        displaySurface.addDisplayableProbeable(displayBrokers, "show brokers");

        Object2DDisplay displayConsumers = new Object2DDisplay(world);
        displayConsumers.setObjectList(consumers);
        displaySurface.addDisplayableProbeable(displayConsumers, "show consumers");

        displaySurface.display();

    }

    @Override
    public String getName() {
        return "AIAD - Electric Market";
    }

    @Override
    public String[] getInitParam() {
        return new String[] { "brokers", "producers", "consumers", "resources" };
    }

    @Override
    protected void launchJADE() {
        mainContainer = Runtime.instance().createMainContainer(new ProfileImpl());

        launchAgents();

    }

    public void launchAgents() {
        try {
            for(int i = 0; i < NUMPRODUCERS; i++){
                Producer p = new Producer((worldWidth/(NUMPRODUCERS+1))*(i+1), 50, this, Color.GREEN);
                world.putObjectAt(p.getX(), p.getY(), p);
                producers.add(p);
                mainContainer.acceptNewAgent("Producer:" + i, p).start();
            }

            for(int i = 0; i < NUMBROKERS; i++){
                Broker b = new Broker((worldWidth/(NUMBROKERS+1))*(i+1), 150, this, Color.YELLOW);
                world.putObjectAt(b.getX(), b.getY(), b);
                brokers.add(b);
                mainContainer.acceptNewAgent("Broker:" + i, b).start();
            }

            for(int i = 0; i < NUMCONSUMERS; i++){
                Consumer c = new Consumer((worldWidth/(NUMCONSUMERS+1))*(i+1), 250, this, Color.RED);
                world.putObjectAt(c.getX(), c.getY(), c);
                consumers.add(c);
                mainContainer.acceptNewAgent("Consumer:" + i, c).start();
            }

        }
        catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        boolean BATCH_MODE = false;

        SimInit init = new SimInit();
        init.setNumRuns(1); // works only in batch mode
        init.loadModel(new OurRepastLauncher(), null, BATCH_MODE);
    }

}