package com.maximumg9.g9utils;

import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public abstract class AttributeSwap {

    public abstract Text getText();

    public static class FailedAttributeSwap extends AttributeSwap {
        private final Text previousStackText;
        private final Text currentStackText;
        private final int time;
        public FailedAttributeSwap(ItemStack previousStack, ItemStack currentStack, int time) {
            this.previousStackText = previousStack.toHoverableText();
            this.currentStackText = currentStack.toHoverableText();
            this.time = time;
        }
        @Override
        public Text getText() {
            return
                Text.literal("FAILED")
                    .formatted(Formatting.DARK_RED)
                    .append(
                        Text.literal("[" + time + "t]: ")
                    ).append(
                        previousStackText
                    ).append(
                        Text.literal(" to ")
                            .formatted(Formatting.RESET)
                    ).append(
                        currentStackText
                    );
        }
    }

    public static class SuccessfullAttributeSwap extends AttributeSwap {
        private final Text previousStackText;
        private final Text currentStackText;
        public SuccessfullAttributeSwap(ItemStack previousStack, ItemStack currentStack) {
            this.previousStackText = previousStack.toHoverableText();
            this.currentStackText = currentStack.toHoverableText();
        }
        @Override
        public Text getText() {
            return Text.literal("SUCCESS: ")
                .formatted(Formatting.GREEN)
                .append(
                    previousStackText
                ).append(
                    Text.literal(" to ")
                        .formatted(Formatting.RESET)
                ).append(
                    currentStackText
                );
        }
    }
}
