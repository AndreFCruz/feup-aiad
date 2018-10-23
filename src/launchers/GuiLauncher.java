package launchers;

import jade.tools.rma.rma;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class GuiLauncher extends Launcher {

    @Override
    public void launch() throws StaleProxyException {
        ContainerController mainContainer = Launcher.getMainContainer();

        AgentController guiAgent = mainContainer.acceptNewAgent("rma", new rma());
        guiAgent.start();
    }

}
