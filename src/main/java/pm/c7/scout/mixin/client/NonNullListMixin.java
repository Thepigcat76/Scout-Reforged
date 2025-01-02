package pm.c7.scout.mixin.client;

import net.minecraft.core.NonNullList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import pm.c7.scout.ScoutUtil;
import pm.c7.scout.client.ScoutUtilClient;

@Mixin(NonNullList.class)
public class NonNullListMixin {
	@Inject(method = "get", at = @At("HEAD"), cancellable = true)
	public void scout$fixIndexingSlots(int index, CallbackInfoReturnable<Object> cir) {
		var playerScreenHandler = ScoutUtilClient.getPlayerScreenHandler();
		if (ScoutUtil.isBagSlot(index)) {
			if (playerScreenHandler != null) {
				cir.setReturnValue(ScoutUtil.getBagSlot(index, playerScreenHandler));
			} else {
				cir.setReturnValue(null);
			}
		}
	}
}
