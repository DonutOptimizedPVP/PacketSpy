package net.vnnhattruongneee.packetspy.mixin

import net.minecraft.client.MinecraftClient
import net.minecraft.network.ClientConnection
import net.minecraft.network.packet.Packet
import net.minecraft.text.Text
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo
import net.vnnhattruongneee.packetspy.config.ModConfig
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Mixin(ClientConnection::class)
class ClientConnectionMixin {

    @Inject(method = ["send(Lnet/minecraft/network/packet/Packet;)V"], at = [At("HEAD")])
    private fun onPacketSend(packet: Packet<*>, ci: CallbackInfo) {
        val client = MinecraftClient.getInstance()
        val player = client.player ?: return
        val packetName = packet.javaClass.simpleName
        val time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        if (ModConfig.enablePacketSpy) {
            val log = "§7[$time] §aDirection Filter §f(C2S) $packetName"
            ModConfig.addLog(log)
            player.sendMessage(Text.literal(log), false)
        }
    }
}