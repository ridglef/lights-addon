package io.ridglef.lights;

import dev.boze.api.event.EventModuleToggle;
import meteordevelopment.orbit.EventHandler;

public class Controller {
    @EventHandler
    public void onModuleToggle(EventModuleToggle event) {
        LightUtil.setColor(event.getModule().getState() ? LightUtil.BozeColorLighter : LightUtil.BozeColorDarker, 125);
    }
}
