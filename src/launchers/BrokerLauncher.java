package launchers;

import agents.Broker;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class BrokerLauncher extends Launcher {

    public BrokerLauncher(int i){
        super(i);
    }

    @Override
    public void launch() throws StaleProxyException {
        ContainerController mainContainer = Launcher.getMainContainer();

        // launch brokers
        for (int i = 1; i < numberOfAgents; i++) {
//            AgentController p = mainContainer.acceptNewAgent("broker" + i, new Broker());
//            p.start();
        }
    }
}
