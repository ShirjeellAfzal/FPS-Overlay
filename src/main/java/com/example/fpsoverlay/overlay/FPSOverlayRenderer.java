package com.example.fpsoverlay.overlay;

import com.example.fpsoverlay.config.ModConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class FPSOverlayRenderer {
    private static int minFps = Integer.MAX_VALUE;
    private static int maxFps = 0;
    private static long lastResetTime = System.currentTimeMillis();
    private static final List<Integer> fpsHistory = new ArrayList<>();

    public static void register() {
        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> {
            ModConfig config = ModConfig.getInstance();
            if (!config.enabled) return;

            MinecraftClient client = MinecraftClient.getInstance();
            if (client.options.hudHidden) return;

            int currentFps = client.getCurrentFps();
            
            if (System.currentTimeMillis() - lastResetTime > 1000) {
                if (currentFps < minFps && currentFps > 0) minFps = currentFps;
                if (currentFps > maxFps) maxFps = currentFps;
                fpsHistory.add(currentFps);
                if (fpsHistory.size() > 60) fpsHistory.remove(0);
                lastResetTime = System.currentTimeMillis();
            }

            String fpsText = "FPS: " + currentFps;
            if (config.displayStyle == ModConfig.DisplayStyle.BRACKETS) {
                fpsText = "[ " + fpsText + " ]";
            }

            if (config.showMinAvgMax) {
                int avg = (int) fpsHistory.stream().mapToInt(Integer::intValue).average().orElse(currentFps);
                String stats = String.format(" (Min: %d | Avg: %d | Max: %d)", 
                    minFps == Integer.MAX_VALUE ? 0 : minFps, 
                    avg, 
                    maxFps);
                fpsText += stats;
            }

            renderOverlay(drawContext, fpsText, currentFps, config);
        });
    }

    private static void renderOverlay(DrawContext context, String text, int currentFps, ModConfig config) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer textRenderer = client.textRenderer;
        
        int color;
        if (config.dynamicFpsColor) {
            if (currentFps >= 60) color = 0xFF55FF55;
            else if (currentFps >= 30) color = 0xFFFFFF55;
            else color = 0xFFFF5555;
        } else {
            color = ModConfig.parseColor(config.textColorHex, 0xFFFFFFFF);
        }

        int textWidth = (int) (textRenderer.getWidth(text) * config.textSize);
        int textHeight = (int) (textRenderer.fontHeight * config.textSize);
        int padding = 4;

        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(config.xOffset, config.yOffset, 0);
        
        if (config.showBackground) {
            int bgColor = ModConfig.parseColor(config.backgroundColorHex, 0x90000000);
            int bgAlpha = (int) (config.backgroundOpacity * 255);
            bgColor = (bgColor & 0x00FFFFFF) | (bgAlpha << 24);
            
            context.fill(-padding, -padding, textWidth + padding, textHeight + padding, bgColor);
        }

        matrices.scale(config.textSize, config.textSize, 1.0f);
        context.drawText(textRenderer, text, 0, 0, color, config.textShadow);
        
        matrices.pop();
    }
}
