package com.github.inrhor.opera.command

import com.github.inrhor.opera.api.manager.TreasureManager.createTreasure
import com.github.inrhor.opera.api.manager.TreasureManager.getTreasureData
import com.github.inrhor.opera.core.data.DataContainer
import com.github.inrhor.opera.core.data.DataContainer.treasureData
import com.github.inrhor.opera.core.data.TreasureData
import com.github.inrhor.opera.server.PluginLoader.loadTask
import com.github.inrhor.opera.server.PluginLoader.unloadTask
import ink.ptms.adyeshach.core.Adyeshach
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import taboolib.common.platform.command.CommandBody
import taboolib.common.platform.command.CommandHeader
import taboolib.common.platform.command.mainCommand
import taboolib.common.platform.command.subCommand
import taboolib.expansion.createHelper

@CommandHeader(name = "opera", permission = "opera.command")
object Command {

    @CommandBody
    val main = mainCommand {
        createHelper()
    }

    @CommandBody(permission = "opera.admin.add")
    val add = subCommand {
        dynamic("唯一编号") {
            dynamic("模型编号") {
                suggestion<Player> { _, _ ->
                    DataContainer.modelContainer.keys.map { it }
                }
                dynamic("奖励编号") {
                    suggestion<Player> { _, _ ->
                        DataContainer.treasureContainer.keys.map { it }
                    }
                    execute<Player> { sender, context, argument ->
                        val id = context["唯一编号"]
                        val model = context["模型编号"]
                        val treasure = argument.split(" ")[0]
                        sender.createTreasure(id, model, treasure)
                    }
                }
            }
        }
    }

    @CommandBody(permission = "opera.admin.remove")
    val remove = subCommand {
        dynamic("唯一编号") {
            suggestion<CommandSender> { _, _ ->
                val list = mutableListOf<String>()
                treasureData.iteratorIOC().forEach {
                    list.add((it as TreasureData).id)
                }
                list
            }
            execute<CommandSender> { sender, context, _ ->
                val id = context["唯一编号"]
                val data = id.getTreasureData()?: return@execute
                val npcId = data.npc
                val api = Adyeshach.api()
                api.getEntityFinder().getEntitiesFromId(npcId).forEach {
                    it.remove()
                }
                treasureData.remove(data)
                sender.sendMessage("§6成功移除宝藏 唯一编号: §f$id (Adyeshach ID: $npcId)")
            }
        }
    }

    @CommandBody(permission = "opera.admin.reload")
    val reload = subCommand {
        unloadTask()
        loadTask()
    }

}