package pm.c7.scout.server;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import pm.c7.scout.ScoutUtil;

public class ScoutUtilServer {
	private static Player currentPlayer = null;

	public static void setCurrentPlayer(Player player) {
		if (currentPlayer != null) {
			ScoutUtil.LOGGER.warn("[Scout] New player set during existing quick move, expect players getting wrong items!");
		}
		currentPlayer = player;
	}

	public static void clearCurrentPlayer() {
		currentPlayer = null;
	}

	public static @Nullable Player getCurrentPlayer() {
		return currentPlayer;
	}
}
