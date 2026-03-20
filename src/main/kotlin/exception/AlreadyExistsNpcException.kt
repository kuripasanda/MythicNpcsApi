package exception

/**
 * NPCのIDが既に存在する場合にスローされる例外クラス。
 */
class AlreadyExistsNpcException(
    override val message: String = "A NPC with the same ID already exists."
): Exception(message)