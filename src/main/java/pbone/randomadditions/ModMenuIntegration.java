package pbone.randomadditions;

import com.google.gson.stream.JsonWriter;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.io.FileWriter;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public String getModId() { // Deprecated, may not be needed, not sure
        return RandomAdditions.MOD_ID;
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> config();
    }

    public static Screen config() {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(MinecraftClient.getInstance().currentScreen) // Set the parent screen to the screen the client is on before opening our config screen, allowing the user to go back
                .setTitle(new TranslatableText("gui.randomadditions.randomadditionsoptionstitle")); // Create a new config with a translatable title
        builder.setDefaultBackgroundTexture(new Identifier("minecraft:textures/block/dirt.png")); // Set the background to dirt blocks
        builder.setGlobalized(true); // ??
        builder.setSavingRunnable(() -> { // Code that actually saves our stuff
            try (
                    final FileWriter fileWriter = new FileWriter(RandomAdditions.OptionsPath.toString());
                    final JsonWriter jsonWriter = new JsonWriter(fileWriter);
            ) {
                jsonWriter.setIndent("    ");
                jsonWriter.beginObject()
                        .name("showarmortoughnessbar").value(RandomAdditions.ShowArmorToughnessBar)
                        .endObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder(); // Create an entry builder
        ConfigCategory mainCategory = builder.getOrCreateCategory(new TranslatableText("gui.randomadditions.randomadditionsoptionscategorymain")); // Create our main category

        mainCategory.addEntry(entryBuilder.startBooleanToggle(new TranslatableText("gui.randomadditions.showarmortoughnessbar"), RandomAdditions.ShowArmorToughnessBar) // Create a new boolean toggle
                .setDefaultValue(true) // Set the default value to true
                .setSaveConsumer(x -> RandomAdditions.ShowArmorToughnessBar = x) // Actually change values
                .build()); // We are required to build it

        return builder.build(); // Building our builder creates a screen we use for getModConfigScreenFactory
    }
}
