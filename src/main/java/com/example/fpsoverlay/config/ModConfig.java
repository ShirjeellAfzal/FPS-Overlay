package com.example.fpsoverlay.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ModConfig {
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "simple_fps_overlay.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public enum DisplayStyle {
        NORMAL, BRACKETS
    }

    // FPS Settings
    public boolean enabled = true;
    public boolean showMinAvgMax = false;
    public boolean dynamicFpsColor = false;
    public DisplayStyle displayStyle = DisplayStyle.NORMAL;

    // Text Settings
    public float textSize = 1.0f;
    public String textColorHex = "#FFFFFF";
    public boolean textShadow = true;

    // Background Settings
    public boolean showBackground = false;
    public String backgroundColorHex = "#90000000";
    public float backgroundOpacity = 0.5f;

    // Position
    public int xOffset = 5;
    public int yOffset = 5;

    private static ModConfig instance = new ModConfig();

    public static ModConfig getInstance() {
        return instance;
    }

    public static int parseColor(String hex, int fallback) {
        try {
            if (hex.startsWith("#")) {
                hex = hex.substring(1);
            }
            if (hex.length() == 6) {
                return 0xFF000000 | Integer.parseInt(hex, 16);
            } else if (hex.length() == 8) {
                return (int) Long.parseLong(hex, 16);
            }
        } catch (NumberFormatException e) {
            return fallback;
        }
        return fallback;
    }

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ModConfig loaded = GSON.fromJson(reader, ModConfig.class);
                if (loaded != null) {
                    instance = loaded;
                }
            } catch (Exception e) {
                instance = new ModConfig();
                save();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(instance, writer);
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
}
