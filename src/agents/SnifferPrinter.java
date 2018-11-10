package agents;

import jade.lang.acl.ACLMessage;
import jade.tools.sniffer.Sniffer;
import jade.util.leap.List;

public class SnifferPrinter extends Sniffer {

    @Override
    public ACLMessage getSniffMsg(List agents, boolean onFlag) {
        ACLMessage msg = super.getSniffMsg(agents, onFlag);

        System.out.println("\nMessage Sniffed:");
        System.out.println(msg);
        return msg;
    }
}
