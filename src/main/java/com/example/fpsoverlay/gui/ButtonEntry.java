package com.example.fpsoverlay.gui;

import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ButtonEntry extends AbstractConfigListEntry<Void> {
    private final ButtonWidget button;
    private final int width;

    public ButtonEntry(Text text, Text buttonText, int width, Runnable onClick) {
        super(text, false);
        this.width = width;
        this.button = ButtonWidget.builder(buttonText, b -> onClick.run())
                .dimensions(0, 0, width, 20)
                .build();
    }

    @Override
    public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isHovered, float delta) {
        super.render(context, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isHovered, delta);
        this.button.setX(x + entryWidth - width - 20); // Align to the right, matching reset button position
        this.button.setY(y);
        this.button.render(context, mouseX, mouseY, delta);
        
        context.drawText(MinecraftClient.getInstance().textRenderer, getFieldName(), x, y + 6, 0xFFFFFFFF, true);
    }

    @Override
    public List<? extends Element> children() {
        return Collections.singletonList(button);
    }

    @Override
    public List<? extends Selectable> narratables() {
        return Collections.singletonList(button);
    }

    @Override
    public Void getValue() {
        return null;
    }

    @Override
    public Optional<Void> getDefaultValue() {
        return Optional.empty();
    }

    @Override
    public void save() {
    }
}
