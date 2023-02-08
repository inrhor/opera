package com.github.inrhor.opera

import taboolib.common.platform.Plugin
import taboolib.platform.BukkitIO
import taboolib.platform.BukkitPlugin

object Opera : Plugin() {

    val plugin by lazy {
        BukkitPlugin.getInstance()
    }

    val resource by lazy {
        BukkitIO()
    }

}