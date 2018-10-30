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

    public DFSearchAgent(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super(model, graphicSettings);
    }

    protected void setType(AgentType type) {
        this.type = type;
    }

    protected void search() {
        // Adding to the DF Service
        DFAgentDescription description = new DFAgentDescription();
        ServiceDescription serviceDescription = new ServiceDescription();

        serviceDescription.setType(this.type.name());
        description.addServices(serviceDescription);

        try {
            DFAgentDescription[] results = DFService.search(this, description);
            for (DFAgentDescription result : results) {
                System.out.println("Found result: " + result.getName());
            }
        } catch (FIPAException e) {
            e.printStackTrace();
        }
    }
}
