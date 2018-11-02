package agents;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import launchers.EnergyMarketLauncher;
import sajas.domain.DFService;
import utils.AgentType;
import utils.GraphicSettings;

public class DFRegisterAgent extends GenericAgent {

    private AgentType type;

    public DFRegisterAgent(EnergyMarketLauncher model, GraphicSettings graphicSettings, AgentType type) {
        super(model, graphicSettings);
        this.type = type;
    }

    @Override
    protected void setup() {
        super.setup();

        // Adding to the DF Service
        DFAgentDescription description = new DFAgentDescription();
        description.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();
        serviceDescription.setType(this.type.name());
        serviceDescription.setName(this.getLocalName());
        description.addServices(serviceDescription);

        try {
            DFService.register(this, description);
        } catch (FIPAException e) {
            System.err.println("Couldn't register DFService.");
            e.printStackTrace();
        }
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
        super.takeDown();
    }
}
