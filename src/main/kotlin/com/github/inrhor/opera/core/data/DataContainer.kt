package com.github.inrhor.opera.core.data

import com.github.inrhor.opera.api.option.ModelOption
import com.github.inrhor.opera.api.option.TreasureOption
import taboolib.expansion.ioc.linker.linkedIOCList
import java.util.concurrent.ConcurrentHashMap

object DataContainer {

    val modelContainer = ConcurrentHashMap<String, ModelOption>()

    val treasureContainer = ConcurrentHashMap<String, TreasureOption>()

    var treasureData = linkedIOCList<TreasureData>()

}