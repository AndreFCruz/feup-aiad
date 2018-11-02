package behaviours.consumer;

import agents.Consumer;
import sajas.core.behaviours.SimpleBehaviour;

public class IncomeBehaviour extends SimpleBehaviour {

    private Consumer myConsumer;

    /**
     * Consumer monthly income
     */
    private int income;

    private int monthDay = 0;

    public IncomeBehaviour(Consumer consumer, int income) {
        super(consumer);
        this.myConsumer = consumer;
        this.income = income;
    }

    @Override
    public void action() {
        if (++monthDay == 30) {
            myConsumer.getEnergyWallet().inject(this.income);
            monthDay = 0;
        }
    }

    @Override
    public boolean done() {
        return false;
    }
}
