package launchers;


import agents.Broker;
import agents.Consumer;
import agents.Producer;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;

import uchicago.src.sim.engine.ScheduleBase;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplayConstants;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;
import utils.EnergyContract;
import utils.GraphicSettings;

import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

/**
 *  This class represents the world model and is the starting point of the simulation.
 */
public class EnergyMarketLauncher extends Repast3Launcher {

    // Render variables
    private static final Color PRODUCER_COLOR = Color.GREEN;
    private static final Color BROKER_COLOR = Color.YELLOW;
    private static final Color CONSUMER_COLOR = Color.RED;

    // Logic variables
    private static final int DELAY_SIMULATION = 1000;
    private static final int NUM_PRODUCERS = 9;
    private static final int NUM_BROKERS = 3;
    private static final int NUM_CONSUMERS = 20;


    // Energy Variables
    /**
     * Approximate value of the total energy produced per month in this energy market.
     */
    private static final int TOTAL_ENERGY_PRODUCED_PER_MONTH = (int) Math.pow(10, 6); // 1 MWh
    private int actualTotalEnergyProducedPerMonth = 0;

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

    private ArrayList<EnergyContract> energyContracts;

    public static void main(String[] args) {
        boolean BATCH_MODE = false;

        SimInit init = new SimInit();
        init.setNumRuns(1); // works only in batch mode
        init.loadModel(new EnergyMarketLauncher(), null, BATCH_MODE);
    }

    public EnergyMarketLauncher() {
        rand = new Random();

        worldWidth = 40 * density;
        worldHeight = 40 * density;
        DisplayConstants.CELL_WIDTH = 1;
        DisplayConstants.CELL_HEIGHT = 1;
    }

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
        scheduleConstructor();

        super.begin();
    }

    private void modelConstructor() {
        world = new Object2DGrid(worldWidth, worldHeight);

        producers = new ArrayList<>();
        brokers = new ArrayList<>();
        consumers = new ArrayList<>();
        energyContracts = new ArrayList<>();
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

    private void scheduleConstructor() {
        getSchedule().scheduleActionAtInterval(1, this, "simulationStep");
//        getSchedule().scheduleActionAtInterval(1, this, "simulationDelay", ScheduleBase.LAST);

    }

    @Override
    public String getName() {
        return "AIAD - Electric Market";
    }

    @Override
    public String[] getInitParam() {
        return new String[]{"brokers", "producers", "consumers", "resources"};
    }

    @Override
    protected void launchJADE() {
        mainContainer = Runtime.instance().createMainContainer(new ProfileImpl());

        try {
            launchAgents();
        } catch (StaleProxyException e) {
            System.err.println("Failed launchAgents in main container.");
            e.printStackTrace();
        }
    }

    private void launchAgents() throws StaleProxyException {
        launchProducers();
        launchBrokers();
//        launchConsumers();
    }

    private void launchProducers() throws StaleProxyException {
        for (int i = 0; i < NUM_PRODUCERS; ++i) {
            GraphicSettings gs = makeGraphicsSettings(i, 1, PRODUCER_COLOR);

            int monthlyEnergyQuota = (int) ((TOTAL_ENERGY_PRODUCED_PER_MONTH / NUM_PRODUCERS) * (1. + rand.nextGaussian()));
            actualTotalEnergyProducedPerMonth += monthlyEnergyQuota;

            Producer p = Producer.createProducer(this, gs, monthlyEnergyQuota);
            world.putObjectAt(p.getX(), p.getY(), p);
            producers.add(p);
            mainContainer.acceptNewAgent("producer-" + i, p).start();

        }
    }

    private void launchBrokers() throws StaleProxyException {
        int totalEneryCostPerMonth = producers.parallelStream().mapToInt(
                (Producer p) -> p.getEnergyProductionPerMonth() * p.getEnergyUnitSellPrice()
        ).sum();

        for (int i = 0; i < NUM_BROKERS; ++i) {
            GraphicSettings gs = makeGraphicsSettings(i, 2, BROKER_COLOR);

            int initialInvestment = (int) ((totalEneryCostPerMonth / NUM_BROKERS)
                    * (1. + (1. / NUM_BROKERS) * rand.nextFloat()));

            Broker b = new Broker(this, gs, initialInvestment);
            world.putObjectAt(b.getX(), b.getY(), b);
            brokers.add(b);
            mainContainer.acceptNewAgent("broker-" + i, b).start();

        }
    }

    private void launchConsumers() throws StaleProxyException {
        int totalEnergyConsumed = (int) (actualTotalEnergyProducedPerMonth * 0.90);

        for (int i = 0; i < NUM_CONSUMERS; i++) {
            GraphicSettings gs = makeGraphicsSettings(i, 3, CONSUMER_COLOR);

            int agentConsumption = (int) ((totalEnergyConsumed / (NUM_CONSUMERS - i)) * (1 + rand.nextGaussian()));
            if (agentConsumption > totalEnergyConsumed)
                agentConsumption = totalEnergyConsumed;
            totalEnergyConsumed -= agentConsumption;

            Consumer c = new Consumer(this, gs, agentConsumption);
            world.putObjectAt(c.getX(), c.getY(), c);
            consumers.add(c);
            mainContainer.acceptNewAgent("consumer-" + i, c).start();
        }
    }

    private GraphicSettings makeGraphicsSettings(int idx, int yCoords, Color color) {
        int x = (worldWidth / (NUM_PRODUCERS + 1)) * (idx + 1);
        int y = (int) ((yCoords * worldHeight) / 4 + 20 * (rand.nextFloat() - 0.5f));
        return new GraphicSettings(x, y, color);
    }

    public void simulationDelay() {
        try {
            Thread.sleep(DELAY_SIMULATION);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void simulationStep() {
        updateEnergyContracts();
    }

    private void updateEnergyContracts() {
        for (ListIterator<EnergyContract> iter = energyContracts.listIterator(); iter.hasNext(); ) {
            EnergyContract contract = iter.next();
            if (contract.hasEnded()) {
                iter.remove();
                // TODO inform Broker and Producer that contract ended
            } else {
                contract.step();
            }
        }
    }

    public void addContract(EnergyContract ec) {
        energyContracts.add(ec);
    }

    public ArrayList<EnergyContract> getEnergyContracts() {
        return energyContracts;
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