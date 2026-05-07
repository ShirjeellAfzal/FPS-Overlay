package com.example.fpsoverlay.gui;

import com.example.fpsoverlay.config.ModConfig;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ConfigScreenBuilder {
    public static Screen build(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Simple FPS Overlay Settings"))
                .setSavingRunnable(ModConfig::save);

        ConfigCategory general = builder.getOrCreateCategory(Text.literal("General"));
        ConfigCategory background = builder.getOrCreateCategory(Text.literal("Background"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ModConfig config = ModConfig.getInstance();

        // --- General Settings ---
        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable FPS"), config.enabled)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.enabled = val)
                .build());

        general.addEntry(entryBuilder.startEnumSelector(Text.literal("Display Style"), ModConfig.DisplayStyle.class, config.displayStyle)
                .setDefaultValue(ModConfig.DisplayStyle.NORMAL)
                .setEnumNameProvider(en -> Text.literal(en.name().substring(0, 1) + en.name().substring(1).toLowerCase()))
                .setSaveConsumer(val -> config.displayStyle = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show Min/Avg/Max"), config.showMinAvgMax)
                .setDefaultValue(false)
                .setSaveConsumer(val -> config.showMinAvgMax = val)
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Dynamic FPS Color"), config.dynamicFpsColor)
                .setDefaultValue(false)
                .setSaveConsumer(val -> config.dynamicFpsColor = val)
                .build());

        general.addEntry(entryBuilder.startIntSlider(Text.literal("Text Size (%)"), (int)(config.textSize * 100), 50, 500)
                .setDefaultValue(100)
                .setSaveConsumer(val -> config.textSize = val / 100f)
                .build());

        // Hex Color Input for Text
        general.addEntry(entryBuilder.startStrField(Text.literal("Text Color (Hex)"), config.textColorHex)
                .setDefaultValue("#FFFFFF")
                .setSaveConsumer(val -> config.textColorHex = val)
                .build());

        // Preset Color Selection for Text
        general.addEntry(entryBuilder.startSelector(Text.literal("Text Color Presets"), new String[]{"White", "Red", "Green", "Blue", "Yellow", "Cyan", "Magenta"}, "White")
                .setDefaultValue("White")
                .setSaveConsumer(val -> {
                    switch (val) {
                        case "White" -> config.textColorHex = "#FFFFFF";
                        case "Red" -> config.textColorHex = "#FF5555";
                        case "Green" -> config.textColorHex = "#55FF55";
                        case "Blue" -> config.textColorHex = "#5555FF";
                        case "Yellow" -> config.textColorHex = "#FFFF55";
                        case "Cyan" -> config.textColorHex = "#55FFFF";
                        case "Magenta" -> config.textColorHex = "#FF55FF";
                    }
                })
                .build());

        general.addEntry(entryBuilder.startBooleanToggle(Text.literal("Text Shadow"), config.textShadow)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.textShadow = val)
                .build());

        general.addEntry(new ButtonEntry(
                Text.literal("Overlay Position"),
                Text.literal("Edit Position"),
                100,
                () -> {
                    ModConfig.save();
                    MinecraftClient.getInstance().setScreen(new EditPositionScreen(builder.build()));
                }
        ));

        // --- Background Settings ---
        background.addEntry(entryBuilder.startBooleanToggle(Text.literal("Show Background"), config.showBackground)
                .setDefaultValue(false)
                .setSaveConsumer(val -> config.showBackground = val)
                .build());

        // Hex Color Input for Background
        background.addEntry(entryBuilder.startStrField(Text.literal("Background Color (Hex)"), config.backgroundColorHex)
                .setDefaultValue("#90000000")
                .setSaveConsumer(val -> config.backgroundColorHex = val)
                .build());

        background.addEntry(entryBuilder.startIntSlider(Text.literal("Background Opacity (%)"), (int)(config.backgroundOpacity * 100), 0, 100)
                .setDefaultValue(50)
                .setSaveConsumer(val -> config.backgroundOpacity = val / 100f)
                .build());

        return builder.build();
    }
}
