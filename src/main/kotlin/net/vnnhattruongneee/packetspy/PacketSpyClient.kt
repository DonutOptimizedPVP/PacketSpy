package net.vnnhattruongneee.packetspy

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.vnnhattruongneee.packetspy.config.ModConfig
import net.minecraft.network.packet.Packet

class PacketSpyClient : ClientModInitializer {
    override fun onInitializeClient() {
        ModConfig.load()

        ClientTickEvents.END_CLIENT_TICK.register { client ->
            val currentTime = System.currentTimeMillis()
            val iterator = ModConfig.ghostingQueue.iterator()

            while (iterator.hasNext()) {
                val pair = iterator.next()
                if (currentTime >= pair.second) {
                    val packet = pair.first
                    val handler = client.networkHandler
                    
                    if (handler != null) {
                        (packet as? Packet<net.minecraft.network.listener.ClientPlayPacketListener>)?.apply {
                            this.apply(handler)
                        }
                    }
                    iterator.remove()
                }
            }
        }
    }
}