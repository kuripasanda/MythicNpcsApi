package com.github.kuripasanda.mythicnpcsapi

import com.github.kuripasanda.mythicnpcsapi.model.MythicNpc
import com.github.kuripasanda.mythicnpcsapi.model.MythicNpcType
import com.github.kuripasanda.mythicnpcsapi.model.NpcPosition
import com.github.kuripasanda.mythicnpcsapi.MythicNpcsApiEntry.MOD_ID
import com.github.kuripasanda.mythicnpcsapi.exception.AlreadyExistsNpcException
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.level.ServerPlayer

interface MythicNpcsApi {

    companion object {
        @JvmStatic
        fun getInstance(): MythicNpcsApi {
            val api = FabricLoader.getInstance().objectShare.get("${MOD_ID}:api") ?: throw RuntimeException("The API has not been initialized yet!")
            return api as? MythicNpcsApi ?: throw RuntimeException("The API instance is not of the correct type!")
        }

        @JvmStatic
        fun setInstance(api: MythicNpcsApi) {
            FabricLoader.getInstance().objectShare.put("${MOD_ID}:api", api)
        }
    }


    /**
     * 全てのNPCを取得します。
     */
    fun getAllNpcs(): List<MythicNpc>

    /**
     * IDに対応するNPCを取得します。
     */
    fun getNpc(id: String): MythicNpc?

    /**
     * NPCが存在するかどうかを確認します。
     */
    fun isExistsNpc(id: String): Boolean

    /**
     * NPCを作成します。
     * @throws AlreadyExistsNpcException 同じIDのNPCが既に存在する場合にスローされます。
     */
    @Throws(AlreadyExistsNpcException::class)
    fun createNpc(id: String, type: MythicNpcType, pos: NpcPosition): MythicNpc

    /**
     * NPCを削除します。
     */
    fun deleteNpc(id: String)


    /* NPCの全体的なデータ変更 */
    /**
     * サーバーに参加している全プレイヤーに対してNPCのデータ変更を反映させます。
     * NPCの共通データに対して変更を加えた後に呼び出してください。
     */
    fun updateNpcForEveryone(npc: MythicNpc)

    /**
     * 特定のプレイヤーに対してNPCのデータ変更を反映させます。
     * NPCのプレイヤー別のデータに対して変更を加えた後に呼び出してください。
     */
    fun updateNpcForPlayer(npc: MythicNpc, player: ServerPlayer)


    /* NPCの位置変更 */
    /**
     * サーバーに参加している全プレイヤーに対してNPCの位置変更をテレポートで反映させます。
     * NPCの共通の位置に対して変更を加えた後に呼び出してください。
     */
    fun teleportNpcForEveryone(npc: MythicNpc)

    /**
     * 特定のプレイヤーに対してNPCの位置変更をテレポートで反映させます。
     * NPCのプレイヤー別の位置に対して変更を加えた後に呼び出してください。
     */
    fun teleportNpcForPlayer(npc: MythicNpc, player: ServerPlayer)

    /**
     * サーバーに参加している全プレイヤーに対してNPCの位置変更を移動で反映させます。※移動は最大8ブロック以内の場合にのみ実行され、それ以上はテレポートされます。
     * NPCの共通の位置に対して変更を加えた後に呼び出してください。
     */
    fun moveNpcForEveryone(npc: MythicNpc)

    /**
     * 特定プレイヤーに対してNPCの位置変更を移動で反映させます。※移動は最大8ブロック以内の場合にのみ実行され、それ以上はテレポートされます。
     * NPCのプレイヤー別の位置に対して変更を加えた後に呼び出してください。
     */
    fun moveNpcForPlayer(npc: MythicNpc, player: ServerPlayer)

}