package com.github.kuripasanda.mythicnpcsapi

import java.util.Optional

interface MinecraftServerBridge {

    fun `mythicNpcsApi$getMythicNpcsApi`(): Optional<MythicNpcsApi> {
        return Optional.empty()
    }

    fun `mythicNpcsApi$setMythicNpcsApi`(api: MythicNpcsApi) {}

}