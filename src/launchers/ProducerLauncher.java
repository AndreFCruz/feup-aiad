package launchers;

import agents.Broker;
import agents.Producer;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class ProducerLauncher extends Launcher {

    public ProducerLauncher(int i){
        super(i);
    }

    @Override
    public void launch() throws StaleProxyException {
        ContainerController mainContainer = Launcher.getMainContainer();

        // launch producers
        for (int i = 1; i < numberOfAgents; i++) {
            AgentController p = mainContainer.acceptNewAgent("producer"+i, new Producer());
            p.start();
        }

    }
}
