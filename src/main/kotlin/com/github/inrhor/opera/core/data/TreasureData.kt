package com.github.inrhor.opera.core.data

import taboolib.expansion.ioc.annotation.Component
import java.util.*

/**
 * @param id 唯一编号
 * @param treasure 宝藏编号
 */
@Component(index = "id")
data class TreasureData(
    var id: String = "null",
    var npc: String = "null",
    var treasure: String = "",
    var model: String = "",
    var players: MutableSet<UUID> = mutableSetOf())