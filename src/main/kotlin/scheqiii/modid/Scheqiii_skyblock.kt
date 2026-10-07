package scheqiii.modid

import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper
import net.minecraft.client.KeyMapping
import com.mojang.blaze3d.platform.InputConstants

object Scheqiii_skyblock : ModInitializer {
	const val MOD_ID: String = "scheqiii_skyblock"
	private const val KEY_NUMPAD_DOWN: Int = 322 // GLFW_KEY_KP_2 (Numpad "nach unten")
	private val binds = linkedMapOf<KeyMapping, String>()


	override fun onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		bind(KEY_NUMPAD_DOWN, "/warp garden");

		ClientTickEvents.END_CLIENT_TICK.register { client ->
			val connection = client.player?.connection ?: return@register
			for ((key, text) in binds) {
                while (key.consumeClick()) {
                    if (text.startsWith("/")) connection.sendCommand(text.drop(1))
                    else connection.sendChat(text)
                }
            }
		}
	}

	private fun bind(key: Int, text: String) {
        val kb = KeyMapping("key.autotext.${binds.size}", InputConstants.Type.KEYBOARD, key, KeyMapping.Category.MISC)
        binds[KeyMappingHelper.registerKeyMapping(kb)] = text
    }

	fun id(path: String): Identifier
		= Identifier.fromNamespaceAndPath(MOD_ID, path)
}
