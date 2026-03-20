package com.github.kuripasanda.mythicnpcsapi

import com.github.kuripasanda.mythicnpcsapi.mixin.FabricLoaderMixin
import net.fabricmc.loader.api.FabricLoader

interface MythicNpcsApi {

    companion object {
        @JvmStatic
        fun getInstance(): MythicNpcsApi {
            return (FabricLoader.getInstance() as FabricLoaderMixin).`mythicNpcsApi$getMythicNpcsApi`()
        }
    }

}