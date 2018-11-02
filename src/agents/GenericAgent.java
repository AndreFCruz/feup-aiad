package agents;

import launchers.EnergyMarketLauncher;
import sajas.core.Agent;
import uchicago.src.sim.gui.Drawable;
import uchicago.src.sim.gui.SimGraphics;
import utils.GraphicSettings;
import utils.Wallet;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class GenericAgent extends Agent implements Drawable, Serializable {

    protected ArrayList<GenericAgent> contacts;

    protected EnergyMarketLauncher worldModel;
    protected GraphicSettings graphicSettings;

    /**
     * Wallet representing the finances of the agent.
     */
    protected Wallet moneyWallet;

    /**
     * Energy wallet (keeps track of this agent's energy trades).
     */
    protected Wallet energyWallet;


    GenericAgent(EnergyMarketLauncher model, GraphicSettings graphicSettings) {
        super();

        this.worldModel = model;
        this.graphicSettings = graphicSettings;
        this.contacts = new ArrayList<>();

        this.moneyWallet = new Wallet(this, Wallet.WalletType.CURRENCY);
        this.energyWallet = new Wallet(this, Wallet.WalletType.ENERGY);
    }

    @Override
    protected void setup() {
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }

    public void addContact(GenericAgent s) {
        this.contacts.add(s);
    }

    public Wallet getMoneyWallet() {
        return this.moneyWallet;
    }

    public Wallet getEnergyWallet() {
        return this.energyWallet;
    }

    /**
     * Draw Methods
     **/

    @Override
    public void draw(SimGraphics simGraphics) {
        simGraphics.drawCircle(graphicSettings.color);
        for (GenericAgent s : contacts) {

            simGraphics.drawLink(graphicSettings.color,
                    4 * graphicSettings.x,
                    4 * (s.graphicSettings.x + graphicSettings.x) / 2,
                    graphicSettings.y,
                    (s.graphicSettings.y + graphicSettings.y) / 2);
        }
    }

    @Override
    public int getX() {
        return graphicSettings.x;
    }

    @Override
    public int getY() {
        return graphicSettings.y;
    }

}
