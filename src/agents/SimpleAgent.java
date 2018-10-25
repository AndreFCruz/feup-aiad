package agents;

import sajas.core.AID;
import sajas.core.Agent;
import sajas.core.behaviours.Behaviour;

public class SimpleAgent extends Agent {

    class SimpleBehaviour extends Behaviour {

        private int count = 0;

        @Override
        public void action() {
            System.out.println(this.getAgent().getAID() + " : " + this.count++);
        }

        @Override
        public boolean done() {
            System.out.println(this.getAgent().getAID() + " : Bye cruel world ??");
            return false;
        }

        @Override
        public int onEnd() {
            System.out.println(this.getAgent().getAID() + " : Bye cruel world...");
            return super.onEnd();
        }

        @Override
        public void onStart() {
            super.onStart();
            System.out.println(this.getAgent().getAID() + " : Hello World");
        }
    }

    private static int agentCount = 0;
    private int id;

    public SimpleAgent() {
        this.id = agentCount++;
    }

    @Override
    protected void setup() {
        super.setup();
        this.addBehaviour(new SimpleBehaviour());
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    /*
    @Override
    public void step() {
        System.out.println(this.id + " Hello World!");
    }
    */
}
