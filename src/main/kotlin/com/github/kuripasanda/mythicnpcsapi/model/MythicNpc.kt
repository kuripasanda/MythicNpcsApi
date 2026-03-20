package com.github.kuripasanda.mythicnpcsapi.model

import com.github.kuripasanda.mythicnpcsapi.MythicNpcsApiEntry
import com.github.kuripasanda.mythicnpcsapi.model.NpcPosition.Companion.forSerializable
import com.github.kuripasanda.mythicnpcsapi.model.NpcPosition.Companion.toNpcPosition
import com.github.kuripasanda.mythicnpcsapi.util.MythicNpcsItemUtil
import kotlinx.serialization.Serializable
import net.minecraft.world.entity.EquipmentSlot
import net.minecraft.world.item.ItemStack
import java.util.UUID

/**
 * MythicNpcsのNPCを表すクラス。
 *
 * @param id NPCのID
 * @param type NPCのタイプ
 * @param entityId NPCのエンティティID
 * @param commonData 全プレイヤー共通のNPCデータ
 * @param dataPerPlayer プレイヤーごとのNPCデータのマップ（キーはプレイヤーのUUID）
 */
class MythicNpc(
    var id: String,
    var type: MythicNpcType,
    var entityId: Int,
    var commonData: Data,
    var dataPerPlayer: MutableMap<UUID, Data>
) {

    fun getSkinForPlayer(playerUUID: UUID): String {
        return dataPerPlayer[playerUUID]?.skin ?: commonData.skin
    }

    fun getNameForPlayer(playerUUID: UUID): String {
        return dataPerPlayer[playerUUID]?.name ?: commonData.name
    }

    fun getNameTagVisibilityForPlayer(playerUUID: UUID): Boolean {
        return dataPerPlayer[playerUUID]?.nameTagVisibility ?: commonData.nameTagVisibility
    }

    fun getPosForPlayer(playerUUID: UUID): NpcPosition {
        return dataPerPlayer[playerUUID]?.pos ?: commonData.pos
    }

    fun getSittingForPlayer(playerUUID: UUID): Boolean {
        return dataPerPlayer[playerUUID]?.sitting ?: commonData.sitting
    }

    fun getEquipment(playerUUID: UUID, slot: EquipmentSlot): ItemStack {
        return dataPerPlayer[playerUUID]?.equipments[slot] ?: commonData.equipments[slot] ?: ItemStack.EMPTY
    }


    companion object {
        fun MythicNpc.forSerializable(): ForSerializable {
            return ForSerializable(
                id,
                type,
                entityId,
                commonData.forSerializable(),
                dataPerPlayer.mapKeys { it.key.toString() }.mapValues { it.value.forSerializable() }
            )
        }

        fun Data.forSerializable(): DataForSerializable {
            val encodedEquipments = equipments.mapValues { MythicNpcsItemUtil.ItemStackCodec.encode(it.value, MythicNpcsApiEntry.server!!.registryAccess()) }
            return DataForSerializable(name, nameTagVisibility, skin, pos.forSerializable(), sitting, HashMap(encodedEquipments))
        }
        fun DataForSerializable.toData(): Data {
            val decodedEquipments = equipments.mapValues { MythicNpcsItemUtil.ItemStackCodec.decode(it.value, MythicNpcsApiEntry.server!!.registryAccess()) }
            return Data(name, nameTagVisibility, skin, pos.toNpcPosition(), sitting, HashMap(decodedEquipments))
        }
    }

    /**
     * NPCのデータクラス。
     * @param name NPCの名前
     * @param nameTagVisibility ネームタグの表示/非表示
     * @param skin NPCのスキン
     * @param pos NPCの位置情報
     */
    data class Data(
        var name: String,
        var nameTagVisibility: Boolean,
        var skin: String,
        var pos: NpcPosition,
        var sitting: Boolean = false,
        var equipments: HashMap<EquipmentSlot, ItemStack> = hashMapOf(),
    ) {

        companion object {
            fun createDefault(id: String, pos: NpcPosition) = Data(
                name = id,
                nameTagVisibility = true,
                skin = id,
                pos = pos
            )
        }

    }

    @Serializable
    data class DataForSerializable(
        var name: String,
        var nameTagVisibility: Boolean,
        var skin: String,
        var pos: NpcPosition.ForSerializable,
        var sitting: Boolean,
        var equipments: HashMap<EquipmentSlot, String> = hashMapOf(),
    )

    @Serializable
    data class ForSerializable(
        var id: String,
        var type: MythicNpcType,
        var entityId: Int,
        var commonData: DataForSerializable,
        var dataPerPlayer: Map<String, DataForSerializable>
    )

}