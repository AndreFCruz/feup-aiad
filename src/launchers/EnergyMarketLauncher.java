package launchers;


import agents.Broker;
import agents.Consumer;
import agents.GenericAgent;
import agents.Producer;
import jade.core.AID;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;
import uchicago.src.sim.analysis.OpenSequenceGraph;
import uchicago.src.sim.analysis.Sequence;
import uchicago.src.sim.engine.Schedule;
import uchicago.src.sim.engine.SimInit;
import uchicago.src.sim.gui.DisplayConstants;
import uchicago.src.sim.gui.DisplaySurface;
import uchicago.src.sim.gui.Object2DDisplay;
import uchicago.src.sim.space.Object2DGrid;
import utils.EnergyContract;
import utils.EnergyContractProposal;
import utils.GraphicSettings;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Stream;

/**
 * This class represents the world model and is the starting point of the simulation.
 */
public class EnergyMarketLauncher extends Repast3Launcher {

    // Render variables
    private static final Color PRODUCER_COLOR = Color.GREEN;
    private static final Color BROKER_COLOR = Color.YELLOW;
    private static final Color CONSUMER_COLOR = Color.RED;
    private OpenSequenceGraph energyGraph = null;

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

    private List<Producer> producers;
    private List<Broker> brokers;
    private List<Consumer> consumers;

    private List<EnergyContract> energyContracts;

    private Map<AID, GenericAgent> agents;

    public EnergyMarketLauncher() {
        rand = new Random();

        worldWidth = 40 * density;
        worldHeight = 40 * density;
        DisplayConstants.CELL_WIDTH = 1;
        DisplayConstants.CELL_HEIGHT = 1;
    }

    public static void main(String[] args) {
        boolean BATCH_MODE = false;

        SimInit init = new SimInit();
        init.setNumRuns(1); // works only in batch mode
        init.loadModel(new EnergyMarketLauncher(), null, BATCH_MODE);
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

        energyPlotBuild();

    }

    private void energyPlotBuild() {
        if (energyGraph != null)
            energyGraph.dispose();

        energyGraph = new OpenSequenceGraph("Sum of energies", this);
        energyGraph.setAxisTitles("time", "energy");

        energyGraph.addSequence("Energy Traded Brokers-Producers", new Sequence() {

            @Override
            public double getSValue() {
                double energy = 0f;

                for (EnergyContract ec : energyContracts){
                    energy += ec.getEnergyAmountPerCycle();

                }

                return energy;
            }

        });

        energyGraph.display();
    }

    private void scheduleConstructor() {
        getSchedule().scheduleActionAtInterval(1, this, "simulationStep");
//        getSchedule().scheduleActionAtInterval(1, this, "simulationDelay", ScheduleBase.LAST);
        getSchedule().scheduleActionAtInterval(1, energyGraph, "step", Schedule.LAST);

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
        launchConsumers();

        setUpAgentsAIDMap();
    }

    private void setUpAgentsAIDMap() {
        agents = new HashMap<>();

        Stream<GenericAgent> stream = Stream.concat(producers.stream(), brokers.stream());
        stream = Stream.concat(stream, consumers.stream());

        stream.forEach(
                (GenericAgent a) -> agents.put(a.getAID(), a)
        );
    }

    private void launchProducers() throws StaleProxyException {
        for (int i = 0; i < NUM_PRODUCERS; ++i) {
            GraphicSettings gs = makeGraphicsSettings(NUM_PRODUCERS, i, 1, PRODUCER_COLOR);

            int energyLeft = TOTAL_ENERGY_PRODUCED_PER_MONTH - actualTotalEnergyProducedPerMonth;
            int monthlyEnergyQuota = (int) ((energyLeft / (NUM_PRODUCERS - i))
                    * (0.5 + rand.nextFloat()));
            if (monthlyEnergyQuota > (TOTAL_ENERGY_PRODUCED_PER_MONTH - actualTotalEnergyProducedPerMonth))
                monthlyEnergyQuota = TOTAL_ENERGY_PRODUCED_PER_MONTH - actualTotalEnergyProducedPerMonth;
            actualTotalEnergyProducedPerMonth += monthlyEnergyQuota;

            Producer p = Producer.createProducer(this, gs, monthlyEnergyQuota);
            world.putObjectAt(p.getX(), p.getY(), p);
            producers.add(p);
            mainContainer.acceptNewAgent("producer-" + i, p).start();

            System.out.println(monthlyEnergyQuota);
        }
    }

    private void launchBrokers() throws StaleProxyException {
        int totalEneryCostPerMonth = producers.parallelStream().mapToInt(
                (Producer p) -> p.getEnergyProductionPerMonth() * p.getEnergyUnitSellPrice()
        ).sum();

        for (int i = 0; i < NUM_BROKERS; ++i) {
            GraphicSettings gs = makeGraphicsSettings(NUM_BROKERS, i, 2, BROKER_COLOR);

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
            GraphicSettings gs = makeGraphicsSettings(NUM_CONSUMERS, i, 3, CONSUMER_COLOR);

            int agentConsumption = (int) ((totalEnergyConsumed / (NUM_CONSUMERS - i)) * (0.5 + rand.nextFloat()));
            if (agentConsumption > totalEnergyConsumed)
                agentConsumption = totalEnergyConsumed;
            totalEnergyConsumed -= agentConsumption;

            Consumer c = new Consumer(this, gs, agentConsumption);
            world.putObjectAt(c.getX(), c.getY(), c);
            consumers.add(c);
            mainContainer.acceptNewAgent("consumer-" + i, c).start();
        }
    }

    private GraphicSettings makeGraphicsSettings(int total, int idx, int yCoords, Color color) {
        int x = (worldWidth / (total + 1)) * (idx + 1);
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
        System.out.println("Currently with " + energyContracts.size() + " contracts.");
//        for (ListIterator<EnergyContractProposal> iter = energyContractProposals.listIterator(); iter.hasNext(); ) {
//            EnergyContractProposal contract = iter.next();
//            if (contract.hasEnded()) {
//                iter.remove();
//                // TODO inform Broker and Producer that contract ended
//            } else {
//                contract.step();
//            }
//        }
    }

    private GenericAgent getAgentByAID(AID agentAID) {
        return agents.get(agentAID);
    }

    public void addContract(EnergyContractProposal contractProposal) {
        GenericAgent supplier = getAgentByAID(contractProposal.getEnergySupplierAID());
        GenericAgent client = getAgentByAID(contractProposal.getEnergyClientAID());

        EnergyContract contract = new EnergyContract(contractProposal, supplier, client);
        energyContracts.add(contract);
        contract.step(); // first step, so first month's trades are promptly withdrawn
    }

    public List<Producer> getProducers() {
        return producers;
    }

    public List<Broker> getBrokers() {
        return brokers;
    }

    public List<Consumer> getConsumers() {
        return consumers;
    }

}