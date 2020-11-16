package net.axay.blueutils.miscellaneous

import java.util.*

object UUIDGenerator {

    fun generateWhile(condition: (UUID) -> Boolean): UUID {
        var currentUUID = UUID.randomUUID()
        while (condition.invoke(currentUUID))
            currentUUID = UUID.randomUUID()
        return currentUUID
    }

}