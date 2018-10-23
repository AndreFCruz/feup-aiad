package launchers;


import jade.core.ProfileImpl;
import sajas.core.Runtime;
import sajas.sim.repast3.Repast3Launcher;
import sajas.wrapper.ContainerController;

import uchicago.src.sim.engine.SimInit;


public class OurRepastLauncher extends Repast3Launcher{

    private ContainerController mainContainer;


    public OurRepastLauncher() {

    }

//    @SuppressWarnings("unchecked")
    public void setup() { // called after constructor
        super.setup();

    }

    public void begin() { // called when "Play" pressed on repast gui
        super.begin();
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

    }

    public static void main(String[] args) {
        boolean BATCH_MODE = false;

        SimInit init = new SimInit();
        init.setNumRuns(1); // works only in batch mode
        init.loadModel(new OurRepastLauncher(), null, BATCH_MODE);
    }

}