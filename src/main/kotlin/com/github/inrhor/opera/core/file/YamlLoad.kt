package com.github.inrhor.opera.core.file

import com.github.inrhor.opera.api.option.ModelOption
import com.github.inrhor.opera.api.option.TreasureOption
import com.github.inrhor.opera.core.data.DataContainer
import taboolib.module.configuration.Configuration
import taboolib.module.configuration.Configuration.Companion.getObject

object YamlLoad {

    fun loadModel() {
        val folder = getFile("model", true)
        getFileList(folder).forEach {
            val yaml = Configuration.loadFromFile(it)
            yaml.getConfigurationSection("model")?.getKeys(false)?.forEach { e ->
                val option = yaml.getObject<ModelOption>("model.$e", false)
                option.id = e
                DataContainer.modelContainer[e] = option
            }
        }
    }

    fun loadTreasure() {
        val folder = getFile("treasure", true)
        getFileList(folder).forEach {
            val yaml = Configuration.loadFromFile(it)
            yaml.getConfigurationSection("treasure")?.getKeys(false)?.forEach { e ->
                val option = yaml.getObject<TreasureOption>("treasure.$e", false)
                option.id = e
                DataContainer.treasureContainer[e] = option
            }
        }
    }

}