package net.vnnhattruongneee.packetspy.compat

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.text.Text
import net.vnnhattruongneee.packetspy.config.ModConfig

class ModMenuCompat : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent ->
            val builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("PacketSpy Terminal"))

            val controls = builder.getOrCreateCategory(Text.literal("Packet Controls"))
            val entryBuilder = builder.entryBuilder()

            controls.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Spy"), ModConfig.enablePacketSpy)
                .setDefaultValue(true).setSaveConsumer { ModConfig.enablePacketSpy = it }.build())

            controls.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Canceller"), ModConfig.enablePacketCancel)
                .setDefaultValue(false).setSaveConsumer { ModConfig.enablePacketCancel = it }.build())

            controls.addEntry(entryBuilder.startBooleanToggle(Text.literal("Enable Ghosting"), ModConfig.enablePacketGhosting)
                .setDefaultValue(false).setSaveConsumer { ModConfig.enablePacketGhosting = it }.build())

            controls.addEntry(entryBuilder.startLongField(Text.literal("Ghosting Delay (ms)"), ModConfig.ghostingDelayMs)
                .setDefaultValue(1000L).setSaveConsumer { ModConfig.ghostingDelayMs = it }.build())

            val logsCategory = builder.getOrCreateCategory(Text.literal("Debug Logs"))
            logsCategory.addEntry(entryBuilder.startStrField(Text.literal("Search Packet"), ModConfig.searchQuery)
                .setDefaultValue("").setSaveConsumer { ModConfig.searchQuery = it }.build())

            val filteredLogs = ModConfig.debugLogs.filter { it.contains(ModConfig.searchQuery, ignoreCase = true) }
            if (filteredLogs.isEmpty()) {
                logsCategory.addEntry(entryBuilder.startTextDescription(Text.literal("§7No matching packets found.")).build())
            } else {
                for (log in filteredLogs) logsCategory.addEntry(entryBuilder.startTextDescription(Text.literal(log)).build())
            }

            builder.setSavingRunnable { ModConfig.save() }
            builder.build()
        }
    }
}