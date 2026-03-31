package net.vnnhattruongneee.packetspy.mixin

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.network.packet.Packet
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import net.vnnhattruongneee.packetspy.config.ModConfig
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Mixin(ClientPlayNetworkHandler::class)
class ClientPacketListenerMixin {

    @Inject(method = ["onEntityTeleport", "onEntityVelocity", "onEntityPosition"], at = [At("HEAD")], cancellable = true)
    private fun handleIncomingPackets(packet: Packet<*>, ci: CallbackInfo) {
        val client = MinecraftClient.getInstance()
        val player = client.player ?: return
        val packetName = packet.javaClass.simpleName
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        if (ModConfig.enablePacketCancel) {
            val log = "§7[$time] §4Canceled $packetName"
            ModConfig.addLog(log)
            player.sendMessage(Text.literal(log), false)
            ci.cancel()
            return
        }

        if (ModConfig.enablePacketGhosting) {
            val log = "§7[$time] §6Ghosting $packetName"
            ModConfig.addLog(log)
            player.sendMessage(Text.literal(log), false)
            
            val releaseTime = System.currentTimeMillis() + ModConfig.ghostingDelayMs
            ModConfig.ghostingQueue.add(Pair(packet, releaseTime))
            ci.cancel()
            return
        }

        if (ModConfig.enablePacketSpy) {
            val log = "§7[$time] §bDirection Filter §f(S2C) $packetName"
            ModConfig.addLog(log)
            player.sendMessage(Text.literal(log), false)
        }
    }
}