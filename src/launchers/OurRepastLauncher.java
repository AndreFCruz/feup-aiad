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

    private static final int NUMPRODUCERS = 8;
    private static final int NUMBROKERS = 5;
    private static final int NUMCONSUMERS = 20;

    private ContainerController mainContainer;
    private Random rand;

    private DisplaySurface displaySurface;
    private Object2DGrid world;
    private int worldWidth = 400;
    private int worldHeight = 400;

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
                int x = (worldWidth/(NUMPRODUCERS+1))*(i+1);
                int y = (int)((worldHeight)/4 + 20*(rand.nextFloat()-0.5f));
                Producer p = new Producer(x, y, this, Color.GREEN);
                world.putObjectAt(p.getX(), p.getY(), p);
                producers.add(p);
                mainContainer.acceptNewAgent("Producer:" + i, p).start();
            }

            for(int i = 0; i < NUMBROKERS; i++){
                int x = (worldWidth/(NUMBROKERS+1))*(i+1);
                int y = (int)((2*worldHeight)/4 + 20*(rand.nextFloat()-0.5f));
                Broker b = new Broker(x, y, this, Color.YELLOW);
                world.putObjectAt(b.getX(), b.getY(), b);
                brokers.add(b);
                mainContainer.acceptNewAgent("Broker:" + i, b).start();
            }

            for(int i = 0; i < NUMCONSUMERS; i++){
                int x = (worldWidth/(NUMCONSUMERS+1))*(i+1);
                int y = (int)((3*worldHeight)/4 + 20*(rand.nextFloat()-0.5f));
                Consumer c = new Consumer(x, y, this, Color.RED);
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