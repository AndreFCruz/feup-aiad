package agents;

import launchers.EnergyMarketLauncher;
import resources.*;
import utils.GraphicSettings;

import java.awt.*;
import java.util.Random;

public class Producer extends GenericAgent {

    EnergySource energySource;

    public static Producer createProducer(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        Random random = new Random();
        float probability = random.nextFloat();
        EnergySource energySource;

        if(probability < 0.20f)
            energySource = new Eolic();
        else if(probability < 0.40f)
            energySource = new Hydro();
        else if(probability < 0.50f)
            energySource = new Solar();
        else
            energySource = new FossilFuels();

        return new Producer(model, graphicSettings, energySource);
    }

    public Producer(EnergyMarketLauncher model, GraphicSettings graphicSettings, EnergySource energySource){
        super(model, graphicSettings);
        this.energySource = energySource;
    }

    @Override
    protected void setup() {
        super.setup();
        System.out.println("Producer " + this.getLocalName() + " was created.");
    }

}
