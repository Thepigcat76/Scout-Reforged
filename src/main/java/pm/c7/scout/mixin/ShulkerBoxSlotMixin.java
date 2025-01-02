package pm.c7.scout.mixin;

import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pm.c7.scout.content.items.BaseBagItem;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {
	@Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
	public void scout$noNBTOverflow(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
		if (stack.getItem() instanceof BaseBagItem) {
			cir.setReturnValue(false);
		}
	}
}
