package pm.c7.scout.mixin.server;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import pm.c7.scout.ScoutUtil;
import pm.c7.scout.server.ScoutUtilServer;

@Mixin(NonNullList.class)
public class NonNullListMixin {
	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	public void scout$fixIndexingSlots(int index, CallbackInfoReturnable<Object> cir) {
		Player currentPlayer = ScoutUtilServer.getCurrentPlayer();
		if (ScoutUtil.isBagSlot(index)) {
			if (currentPlayer != null) {
				cir.setReturnValue(ScoutUtil.getBagSlot(index, currentPlayer.inventoryMenu));
			} else {
				cir.setReturnValue(null);
			}
		}
	}
}
