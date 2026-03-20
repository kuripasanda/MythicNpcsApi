package com.github.kuripasanda.mythicnpcsapi

interface MinecraftServerBridge {

    fun `mythicNpcsApi$getMythicNpcsApi`(): MythicNpcsApi

    fun `mythicNpcsApi$setMythicNpcsApi`(api: MythicNpcsApi)

}