package launchers;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public abstract class Launcher {

    private static Runtime jadeRuntime = null;
    private static ContainerController mainContainer = null;
    Integer numberOfAgents = 10;

    Launcher(){}

    Launcher(int i){
        numberOfAgents = i;
    }

    static ContainerController getMainContainer(){
        if(jadeRuntime == null){
            Profile defaultProfile = new ProfileImpl();
            jadeRuntime = Runtime.instance();
            mainContainer = jadeRuntime.createMainContainer(defaultProfile);
        }
        return mainContainer;
    }


    public abstract void launch() throws StaleProxyException;

}
