package recorders;

import launchers.EnergyMarketLauncher;

abstract class GenericRecorder {

    EnergyMarketLauncher worldModel;

    GenericRecorder(EnergyMarketLauncher em) {
        this.worldModel = em;
    }
}
