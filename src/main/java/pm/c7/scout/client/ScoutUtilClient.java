package pm.c7.scout.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;

public class ScoutUtilClient {
	public static InventoryMenu getPlayerScreenHandler() {
		Minecraft client = Minecraft.getInstance();
		if (client.player != null) {
			return client.player.inventoryMenu;
		}

		return null;
	}

	// FIXME: registry system for mods to register their own blacklisted screens
	public static boolean isScreenBlacklisted(Screen screen) {
		return screen instanceof CreativeModeInventoryScreen;
	}
}
