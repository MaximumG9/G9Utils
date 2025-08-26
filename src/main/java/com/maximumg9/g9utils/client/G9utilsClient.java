package com.maximumg9.g9utils.client;

import com.maximumg9.g9utils.G9utils;
import com.maximumg9.g9utils.config.ConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.network.ClientPlayerEntity;

import static com.mojang.brigadier.arguments.FloatArgumentType.floatArg;
import static com.mojang.brigadier.arguments.FloatArgumentType.getFloat;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class G9utilsClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(
                literal("g9utils")
                    .then(
                        literal("config")
                            .executes((context) -> {
                                var client = context.getSource().getClient();
                                client.send(() ->
                                    client.setScreen(
                                        new ConfigScreen<>(
                                            client.currentScreen,
                                            G9utils.getConfig()
                                        )
                                    )
                                );
                                return 0;
                            })
                    )
                    .then(
                        literal("setRot")
                            .then(
                                argument("rotation",floatArg())
                                    .executes((ctx) -> {
                                        ClientPlayerEntity player = ctx.getSource().getPlayer();
                                        player.setYaw(getFloat(ctx,"rotation"));
                                        return 1;
                                    })
                            )
                            .then(
                                argument("base",floatArg())
                                .then(
                                    argument("exponent",floatArg())
                                        .executes((ctx) -> {
                                            ClientPlayerEntity player = ctx.getSource().getPlayer();
                                            float base = getFloat(ctx,"base");
                                            float exponent = getFloat(ctx,"exponent");
                                            player.setYaw((float) Math.pow(base,exponent));
                                            return 1;
                                        })
                                )
                            )
                    )
            );
        });
    }
}
