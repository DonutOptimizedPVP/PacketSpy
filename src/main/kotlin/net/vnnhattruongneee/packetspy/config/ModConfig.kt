package net.vnnhattruongneee.packetspy.config

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.network.packet.Packet
import java.io.File
import java.util.concurrent.ConcurrentLinkedQueue

object ModConfig {
    var enablePacketSpy = true
    var enablePacketCancel = false
    var enablePacketGhosting = false
    var ghostingDelayMs = 1000L
    var searchQuery = ""
    var customMessageTemplate = "§7[§cPacketSpy§7] Player [playername] use [theloai] and move to X: x : Y: y Z: z"

    val ghostingQueue = ConcurrentLinkedQueue<Pair<Packet<*>, Long>>()
    val debugLogs = mutableListOf<String>()

    private val configFile: File = FabricLoader.getInstance().configDir.resolve("packetspy.json").toFile()
    private val gson: Gson = GsonBuilder().setPrettyPrinting().create()

    fun load() {
        if (configFile.exists()) {
            try {
                val data = gson.fromJson(configFile.readText(), ModConfig::class.java)
                enablePacketSpy = data?.enablePacketSpy ?: true
                enablePacketCancel = data?.enablePacketCancel ?: false
                enablePacketGhosting = data?.enablePacketGhosting ?: false
                ghostingDelayMs = data?.ghostingDelayMs ?: 1000L
                customMessageTemplate = data?.customMessageTemplate ?: "§7[§cPacketSpy§7] Player [playername] use [theloai] and move to X: x : Y: y Z: z"
            } catch (e: Exception) { e.printStackTrace() }
        } else { save() }
    }

    fun save() {
        try { configFile.writeText(gson.toJson(this)) } catch (e: Exception) { e.printStackTrace() }
    }

    fun addLog(message: String) {
        debugLogs.add(0, message)
        if (debugLogs.size > 100) debugLogs.removeAt(debugLogs.size - 1)
    }
}