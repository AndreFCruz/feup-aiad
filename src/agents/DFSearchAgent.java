package agents;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import launchers.EnergyMarketLauncher;
import sajas.domain.DFService;
import utils.AgentType;
import utils.GraphicSettings;

public abstract class DFSearchAgent extends GenericAgent {

    private AgentType type;

    public DFSearchAgent(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);
    }

    protected void setType(AgentType type) {
        this.type = type;
    }

    protected void register() {
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
    }
}
