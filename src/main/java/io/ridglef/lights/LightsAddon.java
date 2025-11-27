package io.ridglef.lights;

import dev.boze.api.BozeInstance;
import dev.boze.api.addon.Addon;

public class LightsAddon extends Addon {
    public static final String ID = "lights-addon";
    public static final String NAME = "Lights Addon";
    public static final String DESCRIPTION = "Homeassistant Light Integration";
    public static final String VERSION = "1.0.0";

    public LightsAddon() {
        super(ID, NAME, DESCRIPTION, VERSION);
    }

    @Override
    public boolean initialize() {
        LightUtil.setColor(LightUtil.BozeColor,1);

        BozeInstance.INSTANCE.registerPackage("io.ridglef.lights");

        BozeInstance.INSTANCE.subscribe(new Controller());

        return true;
    }
}
