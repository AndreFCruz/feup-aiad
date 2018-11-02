package behaviours.consumer;

import agents.Consumer;
import sajas.core.behaviours.SimpleBehaviour;

/**
 * Consume energy.
 */
public class ConsumeBehaviour extends SimpleBehaviour {

    private Consumer myConsumer;

    public ConsumeBehaviour(Consumer consumer) {
        super(consumer);
        this.myConsumer = consumer;
    }

    @Override
    public void action() {
        if (myConsumer.isBeingSupplied()) {
            myConsumer.getEnergyWallet().consume(myConsumer.getEnergyConsumptionPerMonth() / 30f);
        } else {
            // Has no contract yet.
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
