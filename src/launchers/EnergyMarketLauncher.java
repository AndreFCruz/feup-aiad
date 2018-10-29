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
import uchicago.src.sim.gui.DisplayConstants;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;
import utils.GraphicSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;


public class EnergyMarketLauncher extends Repast3Launcher{

    // Render variables
    private static final Color PRODUCERCOLOR = Color.GREEN;
    private static final Color BROKERCOLOR = Color.YELLOW;
    private static final Color CONSUMERCOLOR = Color.RED;

    // Logic variables
    private static final int NUMPRODUCERS = 8;
    private static final int NUMBROKERS = 5;
    private static final int NUMCONSUMERS = 20;

    private ContainerController mainContainer;
    private Random rand;

    private DisplaySurface displaySurface;
    private Object2DGrid world;
    private int density = 10;
    private int worldWidth;
    private int worldHeight;

    private ArrayList<Producer> producers;
    private ArrayList<Broker> brokers;
    private ArrayList<Consumer> consumers;


    public EnergyMarketLauncher() {
        rand = new Random();

        worldWidth = 40 * density;
        worldHeight = 40 * density;
        DisplayConstants.CELL_WIDTH = 1;
        DisplayConstants.CELL_HEIGHT = 1;
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

    private void launchAgents() {
        try {
            for(int i = 0; i < NUMPRODUCERS; i++){
                int x = (worldWidth/(NUMPRODUCERS+1))*(i+1);
                int y = (int)((worldHeight)/4 + 20*(rand.nextFloat()-0.5f));
                GraphicSettings graphicSettings = new GraphicSettings(x, y, PRODUCERCOLOR);
                int energyUnits = 100 + rand.nextInt(100);
                Producer p = Producer.createProducer(this, graphicSettings, energyUnits);
                world.putObjectAt(p.getX(), p.getY(), p);
                producers.add(p);
                mainContainer.acceptNewAgent("producer-" + i, p).start();
            }

            for(int i = 0; i < NUMBROKERS; i++){
                int x = (worldWidth/(NUMBROKERS+1))*(i+1);
                int y = (int)((2*worldHeight)/4 + 20*(rand.nextFloat()-0.5f));
                GraphicSettings graphicSettings = new GraphicSettings(x, y, BROKERCOLOR);
                Broker b = new Broker(this, graphicSettings);
                world.putObjectAt(b.getX(), b.getY(), b);
                brokers.add(b);
                mainContainer.acceptNewAgent("broker-" + i, b).start();
            }

            for(int i = 0; i < NUMCONSUMERS; i++){
                int x = (worldWidth/(NUMCONSUMERS+1))*(i+1);
                int y = (int)((3*worldHeight)/4 + 20*(rand.nextFloat()-0.5f));
                GraphicSettings graphicSettings = new GraphicSettings(x, y, CONSUMERCOLOR);
                Consumer c = new Consumer(this, graphicSettings);
                world.putObjectAt(c.getX(), c.getY(), c);
                consumers.add(c);
                mainContainer.acceptNewAgent("consumer-" + i, c).start();
            }


            producers.get(0).addContact(brokers.get(1));
            brokers.get(1).addContact(producers.get(0));

            producers.get(0).addContact(brokers.get(0));
            brokers.get(0).addContact(producers.get(0));

            consumers.get(2).addContact(brokers.get(1));
            brokers.get(1).addContact(consumers.get(2));

            consumers.get(1).addContact(brokers.get(1));
            brokers.get(1).addContact(consumers.get(1));

        }
        catch (StaleProxyException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        boolean BATCH_MODE = false;

        SimInit init = new SimInit();
        init.setNumRuns(1); // works only in batch mode
        init.loadModel(new EnergyMarketLauncher(), null, BATCH_MODE);
    }


    public ArrayList<Producer> getProducers() {
        return producers;
    }

    public ArrayList<Broker> getBrokers() {
        return brokers;
    }

    public ArrayList<Consumer> getConsumers() {
        return consumers;
    }

}