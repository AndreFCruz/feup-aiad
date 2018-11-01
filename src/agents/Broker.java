package agents;

import behaviours.broker.BusinessStarter;
import sajas.core.AID;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import launchers.EnergyMarketLauncher;
import utils.AgentType;
import utils.GraphicSettings;

import java.util.ArrayList;
import java.util.HashMap;

public class Broker extends DFRegisterAgent {

    private HashMap<AID, Producer> producersInContractWith = new HashMap<>();

    private static final int TIMEOUT = 2000;

    private DFSearchAgent search;

    public Broker(EnergyMarketLauncher model, GraphicSettings graphicSettings, int initialBudget) {
        super(model, graphicSettings);
        this.search = new DFSearchAgent(model, graphicSettings);

        this.setType(AgentType.BROKER);
        this.search.setType(AgentType.PRODUCER);

        this.moneyWallet.inject(initialBudget);
    }

    @Override
    protected void setup() {
        super.setup();
        this.register();
        this.addBehaviour(new BusinessStarter(this, TIMEOUT));
    }

    public ArrayList<String> getProducers() {
        ArrayList<String> producersNames = new ArrayList<>();

        for (DFAgentDescription p : this.search.searchAndGet()) {
            producersNames.add(p.getName().getLocalName());
        }
        return producersNames;
    }

    public EnergyMarketLauncher getWorldModel() {
        return worldModel;
    }

}
