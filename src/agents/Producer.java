package agents;


import jade.core.Agent;

public class Producer extends Agent {

    int cumulativeExpenses = 0;
    int cumulativeEarnings = 0;

    @Override
    protected void setup() {
        super.setup();
        System.out.println("# Hello world");
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

}
