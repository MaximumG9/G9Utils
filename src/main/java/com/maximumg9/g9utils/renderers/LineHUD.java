package com.maximumg9.g9utils.renderers;

import com.maximumg9.g9utils.G9utils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.util.Colors;

public class LineHUD implements LayeredDrawer.Layer {

    @Override
    public void render(DrawContext context, RenderTickCounter tickCounter) {
        if(!G9utils.opt().useless.opt().seeSquareGrid) return;
        int minDim = Math.min(context.getScaledWindowHeight(),context.getScaledWindowWidth());

        int leftEdge = (context.getScaledWindowWidth() - minDim)/2;
        int topEdge = (context.getScaledWindowHeight() - minDim)/2;

        double spacing = (double) minDim / G9utils.opt().useless.opt().squareGridSize;

        for(int i = 1 ;i < G9utils.opt().useless.opt().squareGridSize; i++) {
            context.drawHorizontalLine(
                leftEdge,
                leftEdge + minDim,
                topEdge + (int)(spacing * i),
                Colors.WHITE
            );
        }

        for(int i = 1 ;i < G9utils.opt().useless.opt().squareGridSize; i++) {
            context.drawVerticalLine(
                leftEdge + (int)(spacing * i),
                topEdge + minDim,
                topEdge,
                Colors.WHITE
            );
        }
    }
}
