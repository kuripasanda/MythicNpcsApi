package com.github.kuripasanda.mythicnpcsapi.mixin;

import com.github.kuripasanda.mythicnpcsapi.MinecraftServerBridge;
import com.github.kuripasanda.mythicnpcsapi.MythicNpcsApi;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = FabricLoaderImpl.class, remap = false )
public class FabricLoaderImplMixin implements MinecraftServerBridge {

    @Unique
    protected MythicNpcsApi mythicNpcsApi$api;

    @Override
    public @NotNull MythicNpcsApi mythicNpcsApi$getMythicNpcsApi() {
        return this.mythicNpcsApi$api;
    }

    @Override
    public void mythicNpcsApi$setMythicNpcsApi(@NotNull MythicNpcsApi api) {
        this.mythicNpcsApi$api = api;
    }

}
