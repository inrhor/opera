package com.github.inrhor.opera.server

import com.github.inrhor.opera.core.data.DataContainer
import com.github.inrhor.opera.core.data.TreasureData
import com.github.inrhor.opera.core.file.YamlLoad.loadModel
import com.github.inrhor.opera.core.file.YamlLoad.loadTreasure
import taboolib.common.LifeCycle
import taboolib.common.platform.Awake
import taboolib.common.platform.function.console
import taboolib.common.platform.function.warning
import taboolib.expansion.ioc.IOCReader

/**
 * 插件管理
 */
object PluginLoader {

    @Awake(LifeCycle.INIT)
    fun init() {
        val classes = listOf(TreasureData::class.java)
        IOCReader.readRegister(classes)
    }

    @Awake(LifeCycle.ENABLE)
    fun load() {
        logo("a")
        loadTask()
    }

    @Awake(LifeCycle.DISABLE)
    fun disable() {
        logo("c")
        unloadTask()
    }

    private fun yamlLoad(yaml: String, a: () -> Unit) {
        try {
            a()
        } catch (e: Exception) {
            warning("加载配置文件出错，请检查${yaml}配置")
        }
    }

    fun loadTask() {
        yamlLoad("模型") { loadModel() }
        yamlLoad("宝藏") { loadTreasure() }
    }

    fun unloadTask() {
        DataContainer.modelContainer.clear()
        DataContainer.treasureContainer.clear()
    }

    fun logo(color: String) {
        console().sendMessage("§$color\n" +
            " .---.  ,---.  ,---.  ,---.    .--.   \n" +
                "/ .-. ) | .-.\\ | .-'  | .-.\\  / /\\ \\  \n" +
                "| | |(_)| |-' )| `-.  | `-'/ / /__\\ \\ \n" +
                "| | | | | |--' | .-'  |   (  |  __  | \n" +
                "\\ `-' / | |    |  `--.| |\\ \\ | |  |)| \n" +
                " )---'  /(     /( __.'|_| \\)\\|_|  (_) \n" +
                "(_)    (__)   (__)        (__)        \n")
    }

}