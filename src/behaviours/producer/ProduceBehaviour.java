package behaviours.producer;

import agents.Producer;
import sajas.core.behaviours.SimpleBehaviour;

/**
 * Produce energy.
 */
public class ProduceBehaviour extends SimpleBehaviour {
    private Producer myProducer;

    public ProduceBehaviour(Producer producer) {
        super(producer);
        this.myProducer = producer;
    }

    @Override
    public void action() {
        myProducer.getEnergyWallet().inject(myProducer.getEnergyProductionPerMonth() / 30f);
    }

    @Override
    public boolean done() {
        return false;
    }
}
