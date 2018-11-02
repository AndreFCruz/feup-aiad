package agents;

import behaviours.producer.ProduceBehaviour;
import behaviours.producer.ProducerListeningBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import launchers.EnergyMarketLauncher;
import resources.*;
import utils.AgentType;
import utils.EnergyContractProposal;
import utils.GraphicSettings;

import java.util.Random;

public class Producer extends DFRegisterAgent {

    private EnergySource energySource;
    private int sellPricePerUnit;

    private int energyProductionPerMonth;
//    private int unallocatedEnergyProductionPerMonth; // For when a producer may supply multiple brokers

    private EnergyContractProposal currentEnergyContractProposal = null;

    private Producer(EnergyMarketLauncher model, GraphicSettings graphicSettings, EnergySource energySource, int energyProductionPerMonth) {
        super(model, graphicSettings, AgentType.PRODUCER);
        this.energySource = energySource;
        this.energyProductionPerMonth = energyProductionPerMonth;
        Random rand = new Random();

        // profit margin of 5% to 20%
        this.sellPricePerUnit = (int) (this.energySource.getCostPerUnit() * (1f + (0.05f + 0.15f * rand.nextFloat())));

        // Start with a month's worth of energy, so it can trade with brokers immediately
        this.getEnergyWallet().inject(energyProductionPerMonth);

        addBehaviour(new ProducerListeningBehaviour(this));
        addBehaviour(new ProduceBehaviour(this));
    }

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

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Producer " + this.getLocalName() + " was created.");
    }

    public int getEnergyProductionPerMonth() {
        return energyProductionPerMonth;
    }

//    public int getUnallocatedEnergyProductionPerMonth() {
//        return unallocatedEnergyProductionPerMonth;
//    }

    public int getEnergyUnitSellPrice() {
        return sellPricePerUnit;
    }

    public boolean hasContract() {
        return currentEnergyContractProposal != null;
    }

    public void setContract(EnergyContractProposal ec) {
        this.currentEnergyContractProposal = ec;
    }
}
