package com.github.kuripasanda.mythicnpcsapi.util

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.NbtAccounter
import net.minecraft.nbt.NbtIo
import net.minecraft.nbt.NbtOps
import net.minecraft.world.item.ItemStack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.Base64

/**
 * MythicNpcsApi 内で使用するアイテム関連のユーティリティクラスです。
 */
object MythicNpcsItemUtil {

    /**
     * ItemStack と文字列の相互変換ユーティリティです。
     *
     * 対応フォーマット:
     * - `v2:<base64(gzip-nbt-bytes)>`
     * - `v2:empty` (`ItemStack.EMPTY` / `AIR` の正規化表現)
     *
     * Fabric 1.21.1 では、ItemStack の保存・復元に [net.minecraft.world.item.ItemStack.CODEC] と
     * [net.minecraft.core.HolderLookup.Provider] (registry access) が必要です。
     */
    object ItemStackCodec {
        private const val PREFIX_V2 = "v2:"
        private val BASE64_ENCODER = Base64.getUrlEncoder().withoutPadding()
        private val BASE64_DECODER = Base64.getUrlDecoder()
        private const val EMPTY_TOKEN = "empty"

        /**
         * ItemStack をコンパクトな文字列へ変換します。
         *
         * `NbtIo.writeCompressed` の出力を
         * Base64(URL-safe, no padding) 化します。
         *
         * @param stack 変換対象の ItemStack
         * @param registries レジストリ参照 (例: `server.registryAccess()`)
         */
        fun encode(stack: ItemStack, registries: HolderLookup.Provider): String {
            if (stack.isEmpty) return PREFIX_V2 + EMPTY_TOKEN

            val tag = encodeItemStackToTag(stack, registries)
            val gzipNbt = writeNbtToBytes(tag)
            val b64 = BASE64_ENCODER.encodeToString(gzipNbt)
            return PREFIX_V2 + b64
        }

        /**
         * 文字列から ItemStack を復元します。
         *
         * @throws IllegalArgumentException 形式不正・ペイロード破損時
         */
        fun decode(encoded: String, registries: HolderLookup.Provider): ItemStack {
            require(encoded.startsWith(PREFIX_V2)) { "Unsupported ItemStack encoding format" }
            val payload = encoded.removePrefix(PREFIX_V2)
            if (payload.equals(EMPTY_TOKEN, ignoreCase = true)) return ItemStack.EMPTY

            val bytes = try {
                BASE64_DECODER.decode(payload)
            } catch (e: IllegalArgumentException) {
                throw IllegalArgumentException("Invalid Base64 payload", e)
            }

            val tag = try {
                readNbtFromBytes(bytes)
            } catch (e: Exception) {
                throw IllegalArgumentException("Invalid NBT payload", e)
            }

            val decoded = decodeItemStackFromTag(tag, registries)
            return if (decoded.isEmpty) ItemStack.EMPTY else decoded
        }

        private fun encodeItemStackToTag(stack: ItemStack, registries: HolderLookup.Provider): CompoundTag {
            val encoded = ItemStack.CODEC.encodeStart(
                registries.createSerializationContext(NbtOps.INSTANCE),
                stack
            ).result().orElseThrow {
                IllegalStateException("Failed to encode ItemStack to NBT")
            }

            return encoded as? CompoundTag ?: throw IllegalStateException(
                "Unexpected ItemStack CODEC output: expected CompoundTag"
            )
        }

        private fun decodeItemStackFromTag(tag: CompoundTag, registries: HolderLookup.Provider): ItemStack {
            return ItemStack.CODEC.parse(
                registries.createSerializationContext(NbtOps.INSTANCE),
                tag
            ).result().orElseThrow {
                IllegalArgumentException("Failed to parse ItemStack from NBT")
            }
        }

        private fun writeNbtToBytes(tag: CompoundTag): ByteArray {
            val out = ByteArrayOutputStream(128)
            NbtIo.writeCompressed(tag, out)
            return out.toByteArray()
        }

        private fun readNbtFromBytes(bytes: ByteArray): CompoundTag {
            return NbtIo.readCompressed(ByteArrayInputStream(bytes), NbtAccounter.unlimitedHeap())
        }
    }

}