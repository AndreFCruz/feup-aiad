import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import launchers.Launcher;
import launchers.MainLauncher;
import jade.core.Agent;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.tools.rma.rma;

public class Main {

    public static void main(String[] args) throws StaleProxyException {
        Launcher launcher = new MainLauncher();
        launcher.launch();

        System.out.println("Hello World");

        // get a JADE runtime
        Runtime rt = Runtime.instance();

        // create main container
        Profile p1 = new ProfileImpl();
        // --optional-- p1.setParameter(...);
        ContainerController mainContainer = rt.createMainContainer(p1);

        // create another container
        Profile p2 = new ProfileImpl();
        ContainerController container = rt.createAgentContainer(p2);

        // launch agents
        AgentController ac1 = container.acceptNewAgent("name1", new Agent());
//         AgentController ac1 = container.acceptNewAgent("rma", new rma()); // to launch GUI

        ac1.start();

        Object[] agentArgs = new Object[0];
        AgentController ac2 = container.createNewAgent("name2", "jade.core.Agent", agentArgs);
        ac2.start();

        System.out.println("Bye World");

    }

}
