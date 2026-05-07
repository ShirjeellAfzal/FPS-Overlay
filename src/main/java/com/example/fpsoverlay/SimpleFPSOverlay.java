package com.example.fpsoverlay;

import com.example.fpsoverlay.config.ModConfig;
import com.example.fpsoverlay.input.KeyInputHandler;
import com.example.fpsoverlay.overlay.FPSOverlayRenderer;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleFPSOverlay implements ClientModInitializer {
    public static final String MOD_ID = "simple_fps_overlay";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Simple FPS Overlay...");
        
        // Load configuration
        ModConfig.load();
        
        // Register keybindings
        KeyInputHandler.register();
        
        // Register HUD renderer
        FPSOverlayRenderer.register();
    }
}
