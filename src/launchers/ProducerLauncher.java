package launchers;

import agents.Producer;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.tools.rma.rma;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.util.ArrayList;

public class ProducerLauncher implements Launcher {

    public static void main(String[] args) throws StaleProxyException {

        // get a JADE runtime
        Runtime jadeRuntime = Runtime.instance();

        // create main container
        Profile defaultProfile = new ProfileImpl();
        // --optional-- p1.setParameter(...);
        ContainerController mainContainer = jadeRuntime.createMainContainer(defaultProfile);


        // launch agents
        for (int i = 0; i < 10; i++) {
            AgentController p = mainContainer.acceptNewAgent("p"+i, new Producer());
            p.start();
        }

        AgentController guiAgent = mainContainer.acceptNewAgent("rma", new rma()); // to launch GUI
        guiAgent.start();


//        Object[] agentArgs = new Object[0];
//        AgentController ac2 = container.createNewAgent("name2", "jade.core.Agent", agentArgs);
//        ac2.start();

//        System.out.println("Bye World");

    }

    @Override
    public void launch() {
        System.out.println("Launched producers.");
    }
}
