package com.github.kuripasanda.mythicnpcsapi

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.server.MinecraftServer

object MythicNpcsApiEntry : ModInitializer {

    var server: MinecraftServer? = null
        private set

    override fun onInitialize() {
        ServerLifecycleEvents.SERVER_STARTING.register { server ->
            this.server = server
        }
    }
}
