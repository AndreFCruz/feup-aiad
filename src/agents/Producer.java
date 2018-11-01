package agents;

import behaviours.producer.ProducerListeningBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import launchers.EnergyMarketLauncher;
import resources.*;
import utils.AgentType;
import utils.EnergyContract;
import utils.GraphicSettings;

import java.util.Random;

public class Producer extends DFRegisterAgent {

    private EnergySource energySource;
    private int sellPricePerUnit;

    private int energyProductionPerMonth;
    private int unallocatedEnergyProductionPerMonth;

    private EnergyContract currentEnergyContract = null;

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

    public Producer(EnergyMarketLauncher model, GraphicSettings graphicSettings, EnergySource energySource, int energyProductionPerMonth) {
        super(model, graphicSettings);
        this.energySource = energySource;
        this.energyProductionPerMonth = energyProductionPerMonth;
        Random rand = new Random();

        // profit margin of 5% to 20%
        this.sellPricePerUnit = (int) (this.energySource.getCostPerUnit() * (1f + (0.05f + 0.15f * rand.nextFloat())));

        //For DFService
        this.setType(AgentType.PRODUCER);
        addBehaviour(new ProducerListeningBehaviour(this, MessageTemplate.MatchPerformative(ACLMessage.CFP)));
    }

    @Override
    protected void setup() {
        super.setup();
        this.register();
        System.out.println("Producer " + this.getLocalName() + " was created.");
    }

    public int getEnergyProductionPerMonth() {
        return energyProductionPerMonth;
    }

    public int getUnallocatedEnergyProductionPerMonth() {
        return unallocatedEnergyProductionPerMonth;
    }

    public int getEnergyUnitSellPrice() {
        return sellPricePerUnit;
    }

    public boolean hasContract() {
        return currentEnergyContract == null;
    }

    public void setContract(EnergyContract ec) {
        this.currentEnergyContract = ec;
    }
}
