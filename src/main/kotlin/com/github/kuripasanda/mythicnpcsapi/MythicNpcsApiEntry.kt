package com.github.kuripasanda.mythicnpcsapi

import com.github.kuripasanda.mythicnpcsapi.mixin.FabricLoaderMixin
import net.fabricmc.api.ModInitializer
import net.fabricmc.loader.api.FabricLoader

object MythicNpcsApiEntry : ModInitializer, MythicNpcsApi {

    override fun onInitialize() {
        (FabricLoader.getInstance() as FabricLoaderMixin).`mythicNpcsApi$setMythicNpcsApi`(this)
    }
}
