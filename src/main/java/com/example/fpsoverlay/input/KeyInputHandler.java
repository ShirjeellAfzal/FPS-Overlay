package com.example.fpsoverlay.input;

import com.example.fpsoverlay.gui.ConfigScreenBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_FPS = "key.category.simple_fps_overlay";
    public static final String KEY_OPEN_CONFIG = "key.simple_fps_overlay.open_config";

    public static KeyBinding openConfigKey;

    public static void register() {
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_OPEN_CONFIG,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_U,
                KEY_CATEGORY_FPS
        ));

        registerKeyInputs();
    }

    private static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKey.wasPressed()) {
                client.setScreen(ConfigScreenBuilder.build(client.currentScreen));
            }
        });
    }
}
