package launchers;

import agents.Producer;

public class MainLauncher implements Launcher {
    @Override
    public void launch() {
        Producer prod = new Producer();
    }
}
