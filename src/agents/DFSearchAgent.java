package agents;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import launchers.EnergyMarketLauncher;
import sajas.domain.DFService;
import utils.AgentType;
import utils.GraphicSettings;

public class DFSearchAgent extends GenericAgent {

    private AgentType type;

    private DFAgentDescription[] result = {};

    public DFSearchAgent(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);
    }

    protected void setSearchType(AgentType type) {
        this.type = type;
    }

    protected void search() {
        DFAgentDescription description = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();

        serviceDescription.setType(type.name());
        description.addServices(serviceDescription);

        try {
            result = DFService.search(this, description);
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }

    public DFAgentDescription[] searchAndGet() {
        search();
        return result;
    }
}
