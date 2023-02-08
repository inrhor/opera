package com.github.inrhor.opera.core.listener

import com.github.inrhor.opera.api.manager.TreasureManager.createAnimation
import com.github.inrhor.opera.api.manager.TreasureManager.getTreasure
import com.github.inrhor.opera.api.manager.TreasureManager.getTreasureDataByNPC
import com.github.inrhor.opera.api.manager.TreasureManager.npcIsTreasure
import com.github.inrhor.opera.core.script.Script.eval
import ink.ptms.adyeshach.core.event.AdyeshachEntityInteractEvent
import ink.ptms.adyeshach.core.event.AdyeshachEntityVisibleEvent
import taboolib.common.platform.event.SubscribeEvent

object NpcHandle {

    @SubscribeEvent
    fun view(ev: AdyeshachEntityVisibleEvent) {
        val npcId = ev.entity.id
        if (!npcId.npcIsTreasure()) return
        val data = npcId.getTreasureDataByNPC()?: return
        val p = ev.viewer
        val npc = ev.entity
        if (ev.visible) {
            if (!data.players.contains(p.uniqueId)) {
                npc.removeViewer(p)
            }
        }else {
            val option = data.treasure.getTreasure()?: return
            val condition = option.condition
            if (!p.eval(condition)) {
                npc.addViewer(p)
            }
        }
    }

    @SubscribeEvent
    fun click(ev: AdyeshachEntityInteractEvent) {
        val npc = ev.entity
        val npcId = npc.id
        if (!npcId.npcIsTreasure()) return
        val data = npcId.getTreasureDataByNPC()?: return
        val p = ev.player
        val uuid = p.uniqueId
        if (data.players.contains(uuid)) return
        val option = data.treasure.getTreasure()?: return
        p.eval(option.reward)
        npc.removeViewer(p)
        data.players.add(uuid)
        createAnimation(npc.getLocation(), p, data.model)
    }

}