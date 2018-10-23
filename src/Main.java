import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.StaleProxyException;
import launchers.BrokerLauncher;
import launchers.GuiLauncher;
import launchers.Launcher;
import jade.core.Agent;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import launchers.ProducerLauncher;

public class Main {

    public static void main(String[] args) throws StaleProxyException {

        System.out.println("Starting GUI");
        GuiLauncher l = new GuiLauncher();
        l.launch();

        System.out.println("Starting Producers");
        ProducerLauncher p = new ProducerLauncher(5);
        p.launch();

        System.out.println("Starting Brokers");
        BrokerLauncher b = new BrokerLauncher(10);
        b.launch();

    }

}
