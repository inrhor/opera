
package com.github.inrhor.opera.core.script

import org.bukkit.entity.Player
import taboolib.common.platform.function.*
import taboolib.common5.Coerce
import taboolib.module.chat.colored
import taboolib.module.kether.KetherShell
import taboolib.module.kether.printKetherErrorMessage

object Script {

    fun Player.eval(script: String): Boolean {
        if (script.isEmpty()) return true
        return try {
            KetherShell.eval(script, sender = adaptPlayer(this), namespace = listOf("QuestEngine", "adyeshach")).thenApply {
                Coerce.toBoolean(it)
            }.getNow(true)
        } catch (ex: Throwable) {
            console().sendMessage("&cError Script: $script".colored())
            ex.printKetherErrorMessage()
            false
        }
    }
}