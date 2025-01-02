package pm.c7.scout.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.config.ScoutConfig;
import pm.c7.scout.content.items.BaseBagItem;

import java.util.function.Predicate;

@Mixin(Player.class)
public class PlayerMixin {
	/*
	@Inject(method = "getProjectile", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/item/ProjectileWeaponItem;getAllSupportedProjectiles()Ljava/util/function/Predicate;"), cancellable = true, locals = LocalCapture.CAPTURE_FAILHARD)
	public void scout$arrowsFromBags(ItemStack stack, CallbackInfoReturnable<ItemStack> cir, @Local Predicate<ItemStack> predicate) {
		if (ScoutConfig.useArrows) {
			var self = (Player) (Object) this;
			var leftPouch = ScoutUtil.findBagItem(self, BaseBagItem.BagType.POUCH, false);
			var rightPouch = ScoutUtil.findBagItem(self, BaseBagItem.BagType.POUCH, true);
			var satchel = ScoutUtil.findBagItem(self, BaseBagItem.BagType.SATCHEL, false);

			if (!leftPouch.isEmpty()) {
				BaseBagItem item = (BaseBagItem) leftPouch.getItem();
				var inv = item.getItemHandler(leftPouch);

				for(int i = 0; i < inv.getSlots(); ++i) {
					ItemStack invStack = inv.getStackInSlot(i);
					if (predicate.test(invStack)) {
						cir.setReturnValue(invStack);
					}
				}
			}
			if (!rightPouch.isEmpty()) {
				BaseBagItem item = (BaseBagItem) rightPouch.getItem();
				var inv = item.getItemHandler(rightPouch);

				for(int i = 0; i < inv.getSlots(); ++i) {
					ItemStack invStack = inv.getStackInSlot(i);
					if (predicate.test(invStack)) {
						cir.setReturnValue(invStack);
					}
				}
			}
			if (!satchel.isEmpty()) {
				BaseBagItem item = (BaseBagItem) satchel.getItem();
				var inv = item.getItemHandler(satchel);

				for(int i = 0; i < inv.getSlots(); ++i) {
					ItemStack invStack = inv.getStackInSlot(i);
					if (predicate.test(invStack)) {
						cir.setReturnValue(invStack);
					}
				}
			}
		}
	}

	 */
}
