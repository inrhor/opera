package com.github.inrhor.opera.api.manager

import com.github.inrhor.oemodel.manager.ModelManager.updateModel
import com.github.inrhor.opera.api.option.TreasureOption
import com.github.inrhor.opera.core.data.DataContainer.modelContainer
import com.github.inrhor.opera.core.data.DataContainer.treasureContainer
import com.github.inrhor.opera.core.data.DataContainer.treasureData
import com.github.inrhor.opera.core.data.TreasureData
import ink.ptms.adyeshach.core.Adyeshach
import ink.ptms.adyeshach.core.entity.EntityInstance
import ink.ptms.adyeshach.core.entity.EntityTypes
import ink.ptms.adyeshach.core.entity.manager.ManagerType
import ltd.icecold.orangeengine.api.OrangeEngineAPI
import org.bukkit.Location
import org.bukkit.entity.Player
import taboolib.common.platform.function.submit

object TreasureManager {

    /**
     * 宝藏数据
     */
    fun String.getTreasureData(): TreasureData? {
        val data = treasureData.get(this)?: return null
        return data as TreasureData
    }

    /**
     * @return 宝藏配置
     */
    fun String.getTreasure(): TreasureOption? {
        return treasureContainer[this]
    }

    /**
     * 宝藏数据移除玩家
     */
    fun Player.removeTreasure(id: String) {
        id.getTreasureData()?.players?.remove(uniqueId)
    }

    /**
     * 创建宝藏
     */
    fun Player.createTreasure(id: String, model: String, treasure: String): Boolean {
        if (hasTreasureData(id)) {
            sendMessage("§c已经有相同的唯一编号了")
            return false
        }
        val manager = Adyeshach.api().getPublicEntityManager(ManagerType.PERSISTENT)
        val entity: EntityInstance = manager.create(EntityTypes.PLAYER, location) {
            it.id = "__Opera__$id"
            // 在单位生成前把它设置成傻子
            it.isNitwit = true
            it.isNameTagVisible = false
        }
        val i = entity.id
        saveTreasure(id, i, treasure)
        sendMessage("§6成功创建宝藏 唯一编号: §f$id (Adyeshach ID: $i)")
        sendMessage("§6宝藏模型编号: §f$model §6奖励编号: §f$treasure")
        entity.normalizeUniqueId.updateModel(model)
        return true
    }

    /**
     * @return NPC是否是宝藏
     */
    fun String.npcIsTreasure(): Boolean {
        treasureData.iteratorIOC().forEach {
            val data = it as TreasureData
            if (data.npc == this) return true
        }
        return false
    }

    /**
     * @return 通过NPC ID获取宝藏数据
     */
    fun String.getTreasureDataByNPC(): TreasureData? {
        treasureData.iteratorIOC().forEach {
            val data = it as TreasureData
            if (data.npc == this) return data
        }
        return null
    }

    private fun saveTreasure(id: String, npc: String, treasure: String) {
        treasureData.add(TreasureData(id, npc, treasure))
    }

    private fun hasTreasureData(id: String): Boolean {
        return treasureData.get(id) != null
    }

    /**
     * 播放开箱动画
     */
    fun createAnimation(location: Location, player: Player, model: String) {
        val modelOption = modelContainer[model]?: return
        val manager = Adyeshach.api().getPrivateEntityManager(player)
        val entity: EntityInstance = manager.create(EntityTypes.PLAYER, location) {
            it.id = "__OperaAnimation__${player.uniqueId}"
            it.isNitwit = true
            it.isNameTagVisible = false
        }
        val enUid = entity.normalizeUniqueId
        enUid.updateModel(model)
        OrangeEngineAPI.getModelManager()?.getModelEntity(enUid)?.playAnimation(modelOption.open)
        submit(delay = modelOption.time.toLong(), async = true) {
            entity.remove()
        }
    }

}