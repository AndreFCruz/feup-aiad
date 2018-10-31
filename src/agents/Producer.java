package agents;

import behaviours.producer.ListeningBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import launchers.EnergyMarketLauncher;
import resources.*;
import utils.AgentType;
import utils.GraphicSettings;

import java.util.Random;

public class Producer extends DFRegisterAgent {

    Consumer consumerServing = null;
    EnergySource energySource;
    float currentSellPricePerUnit;
    int energyUnits;

    public static Producer createProducer(EnergyMarketLauncher model, GraphicSettings graphicSettings, int energyUnits) {
        Random random = new Random();
        float probability = random.nextFloat();
        EnergySource energySource;

        if (probability < 0.20f)
            energySource = new Eolic();
        else if (probability < 0.40f)
            energySource = new Hydro();
        else if (probability < 0.50f)
            energySource = new Solar();
        else
            energySource = new FossilFuels();

        return new Producer(model, graphicSettings, energySource, energyUnits);
    }

    public Producer(EnergyMarketLauncher model, GraphicSettings graphicSettings, EnergySource energySource, int energyUnits) {
        super(model, graphicSettings);
        this.energySource = energySource;
        this.energyUnits = energyUnits;
        Random rand = new Random();
        // increase in 10-20%
        this.currentSellPricePerUnit = this.energySource.getCostPerUnit() * (1f + (0.1f + 0.1f * rand.nextFloat()));

        //For DFService
        this.setType(AgentType.PRODUCER);
        addBehaviour(new ListeningBehaviour(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
    }

    @Override
    protected void setup() {
        super.setup();
        this.register();
        System.out.println("Producer " + this.getLocalName() + " was created.");
    }

}
