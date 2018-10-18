package launchers;

import agents.Broker;
import agents.Producer;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class BrokerLauncher implements Launcher {

    public static void main(String[] args) throws StaleProxyException {

        // get a JADE runtime
        Runtime jadeRuntime = Runtime.instance();

        // create main container
        Profile defaultProfile = new ProfileImpl();
        // --optional-- p1.setParameter(...);
        ContainerController mainContainer = jadeRuntime.createMainContainer(defaultProfile);

        // launch agents
        for (int i = 0; i < 10; i++) {
            AgentController p = mainContainer.acceptNewAgent("b" + i, new Broker());
            p.start();
        }

//        AgentController guiAgent = mainContainer.acceptNewAgent("rma", new rma()); // to launch GUI
//        guiAgent.start();

    }


    @Override
    public void launch() {

    }
}
