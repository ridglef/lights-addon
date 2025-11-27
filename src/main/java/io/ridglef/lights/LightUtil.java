package io.ridglef.lights;

import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class LightUtil {
    public static final Color BozeColor = new Color(79, 70, 229);
    public static final Color BozeColorLighter = new Color(79, 70, 229).brighter().brighter();
    public static final Color BozeColorDarker = new Color(79, 70, 229).darker().darker();

    // Damn the java gods for not letting me finalize this
    private static String token;
    private static URL url;
    private static String light;

    static {
        File tokenFile = new File(FabricLoaderImpl.INSTANCE.getGameDirectory(), "boze/ha.token");
        try (BufferedReader reader = new BufferedReader(new FileReader(tokenFile))) {
            token = "Bearer " + reader.readLine();
            url = new URI(reader.readLine() + "/api/services/light/turn_on").toURL();
            light = reader.readLine();
        } catch (Exception e) {
            System.out.println("Noob.");
            System.exit(80085);
        }
    }

    private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture<?> currentResetTask;

    public static synchronized void setColor(Color color, int ms) {
        if (currentResetTask != null && !currentResetTask.isDone()) {
            currentResetTask.cancel(false);
        }

        setColor(color);

        currentResetTask = scheduler.schedule(() -> setColor(BozeColor), ms, TimeUnit.MILLISECONDS);
    }

    private static void setColor(Color color) {
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", token);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String payload = String.format(
                    "{ \"entity_id\": \"%s\", \"rgb_color\": [%d, %d, %d], \"brightness\": 255 }",
                    light,
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue()
            );

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = payload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            conn.getResponseCode();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
