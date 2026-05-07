package com.example.fpsoverlay.gui;

import com.example.fpsoverlay.config.ModConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class EditPositionScreen extends Screen {
    private final Screen parent;
    private boolean dragging = false;
    private double dragOffsetX = 0;
    private double dragOffsetY = 0;

    public EditPositionScreen(Screen parent) {
        super(Text.literal("Edit Overlay Position"));
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0x88000000);
        
        ModConfig config = ModConfig.getInstance();
        String previewText = "FPS: 144";
        if (config.displayStyle == ModConfig.DisplayStyle.BRACKETS) {
            previewText = "[ " + previewText + " ]";
        }
        
        int color;
        if (config.dynamicFpsColor) {
            color = 0xFF55FF55;
        } else {
            color = ModConfig.parseColor(config.textColorHex, 0xFFFFFFFF);
        }

        int textWidth = (int) (client.textRenderer.getWidth(previewText) * config.textSize);
        int textHeight = (int) (client.textRenderer.fontHeight * config.textSize);
        int padding = 4;

        if (dragging) {
            config.xOffset = (int) (mouseX - dragOffsetX);
            config.yOffset = (int) (mouseY - dragOffsetY);
            config.xOffset = Math.max(0, Math.min(config.xOffset, this.width - textWidth));
            config.yOffset = Math.max(0, Math.min(config.yOffset, this.height - textHeight));
            if (config.xOffset < 10) config.xOffset = 5;
            if (config.yOffset < 10) config.yOffset = 5;
            if (config.xOffset > this.width - textWidth - 10) config.xOffset = this.width - textWidth - 5;
            if (config.yOffset > this.height - textHeight - 10) config.yOffset = this.height - textHeight - 5;
        }

        context.getMatrices().push();
        context.getMatrices().translate(config.xOffset, config.yOffset, 0);

        if (config.showBackground) {
            int bgColor = ModConfig.parseColor(config.backgroundColorHex, 0x90000000);
            int bgAlpha = (int) (config.backgroundOpacity * 255);
            bgColor = (bgColor & 0x00FFFFFF) | (bgAlpha << 24);
            context.fill(-padding, -padding, textWidth + padding, textHeight + padding, bgColor);
        }

        context.getMatrices().scale(config.textSize, config.textSize, 1.0f);
        context.drawText(client.textRenderer, previewText, 0, 0, color, config.textShadow);
        context.getMatrices().pop();

        context.drawCenteredTextWithShadow(client.textRenderer, "Drag the text to reposition. Press ESC to save.", this.width / 2, this.height / 2 + 20, 0xFFFFFFFF);
        
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        ModConfig config = ModConfig.getInstance();
        String previewText = "FPS: 144";
        if (config.displayStyle == ModConfig.DisplayStyle.BRACKETS) {
            previewText = "[ " + previewText + " ]";
        }
        int textWidth = (int) (client.textRenderer.getWidth(previewText) * config.textSize);
        int textHeight = (int) (client.textRenderer.fontHeight * config.textSize);

        if (mouseX >= config.xOffset && mouseX <= config.xOffset + textWidth &&
            mouseY >= config.yOffset && mouseY <= config.yOffset + textHeight) {
            dragging = true;
            dragOffsetX = mouseX - config.xOffset;
            dragOffsetY = mouseY - config.yOffset;
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void close() {
        ModConfig.save();
        client.setScreen(parent);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
