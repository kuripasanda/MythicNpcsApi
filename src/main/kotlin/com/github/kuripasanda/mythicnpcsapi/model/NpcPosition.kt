package com.github.kuripasanda.mythicnpcsapi.model

import com.github.kuripasanda.mythicnpcsapi.MythicNpcsApiEntry
import kotlinx.serialization.Serializable
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3

data class NpcPosition(
    var level: Level,
    var pos: Vec3,
    var yHeadRot: Float,
    var yaw: Float,
    var pitch: Float
) {

    companion object {
        fun NpcPosition.forSerializable(): ForSerializable {
            return ForSerializable(
                level.dimension().registry().toString(),
                pos.x,
                pos.y,
                pos.z,
                yHeadRot,
                yaw,
                pitch
            )
        }

        /**
         * [ForSerializable]を[NpcPosition]に変換します。
         * @throws IllegalStateException レベルの取得に失敗した場合にスローされます。
         */
        @Throws(IllegalStateException::class)
        fun ForSerializable.toNpcPosition(): NpcPosition {
            val level = MythicNpcsApiEntry.server?.allLevels?.firstOrNull { it.dimension().registry().toString() == level }
                ?: throw IllegalStateException("Failed to get level for dimension: $level")
            return NpcPosition(
                level,
                Vec3(x, y, z),
                headYaw,
                yaw,
                pitch
            )
        }
    }

    @Serializable
    data class ForSerializable(
        var level: String,
        var x: Double,
        var y: Double,
        var z: Double,
        var headYaw: Float,
        var yaw: Float,
        var pitch: Float
    )

}