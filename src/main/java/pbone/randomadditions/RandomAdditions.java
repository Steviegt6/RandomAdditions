package pbone.randomadditions;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import pbone.randomadditions.loaders.CuriosLoader;
import pbone.randomadditions.loaders.EnchantmentLoader;
import pbone.randomadditions.loaders.ItemLoader;

import java.io.FileReader;
import java.nio.file.Path;

public class RandomAdditions implements ModInitializer {
    public static final String MOD_ID = "randomadditions";
    public static ModContainer RandomAdditionsMod;
    public static String OptionsVersion = "0.1.0";
    public static Path OptionsPath = FabricLoader.getInstance().getConfigDir().resolve("randomadditions.json");
    public static boolean ShowArmorToughnessBar;

    @Override
    public void onInitialize() {
        // Initialize our loaders.
        ItemLoader.onInitialize();
        EnchantmentLoader.onInitialize();
        CuriosLoader.onInitialize();

        readConfig();

        for (ModContainer mod : FabricLoader.getInstance().getAllMods())
            if (mod.getMetadata().getName().equals("Random Additions"))
                RandomAdditionsMod = mod;
    }

    private void readConfig() {
        if (OptionsPath.toFile().exists()) {
            try (
                    final FileReader fr = new FileReader(OptionsPath.toString())
            ) {
                final JsonElement je = new JsonParser().parse(fr);

                if (!je.isJsonObject())
                    ShowArmorToughnessBar = true;

                final JsonObject object = je.getAsJsonObject();
                ShowArmorToughnessBar = readBoolean(object, "REMOVE_SPLASH", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            ShowArmorToughnessBar = true;
        }
    }

    private boolean readBoolean(JsonObject json, String key, boolean defaultValue) {
        final JsonElement jsonElement = json.get(key);

        if (jsonElement == null || !jsonElement.isJsonPrimitive())
            return defaultValue;

        try {
            return jsonElement.getAsBoolean();
        } catch (Exception ignore) {
            return defaultValue;
        }
    }
}
