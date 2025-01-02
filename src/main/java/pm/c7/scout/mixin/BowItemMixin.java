package pm.c7.scout.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.config.ScoutConfig;
import pm.c7.scout.content.items.BaseBagItem;

import java.util.List;

@Mixin(BowItem.class)
public class BowItemMixin {
	@Inject(method = "releaseUsing", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
	))
	public void scout$arrowsFromBags(ItemStack itemStack, Level level, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
		if (ScoutConfig.useArrows && user instanceof Player player) {
			boolean bl = true;
			boolean infinity = bl && itemStack.is(Items.ARROW);
			boolean hasRan = false;

			if (!infinity && !player.hasInfiniteMaterials()) {
				var leftPouch = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, false);
				var rightPouch = ScoutUtil.findBagItem(player, BaseBagItem.BagType.POUCH, true);
				var satchel = ScoutUtil.findBagItem(player, BaseBagItem.BagType.SATCHEL, false);

				if (!leftPouch.isEmpty()) {
					BaseBagItem item = (BaseBagItem) leftPouch.getItem();
					IItemHandlerModifiable itemHandler = item.getItemHandler(leftPouch);

					for(int i = 0; i < itemHandler.getSlots(); ++i) {
						ItemStack invStack = itemHandler.getStackInSlot(i);
						if (ItemStack.isSameItemSameComponents(invStack, itemStack)) {
							invStack.shrink(1);
							if (invStack.isEmpty()) {
								itemHandler.setStackInSlot(i, ItemStack.EMPTY);
							}
							hasRan = true;
							break;
						}
					}
				}
				if (!rightPouch.isEmpty() && !hasRan) {
					BaseBagItem item = (BaseBagItem) rightPouch.getItem();
					var inv = item.getItemHandler(rightPouch);

					for(int i = 0; i < inv.getSlots(); ++i) {
						ItemStack invStack = inv.getStackInSlot(i);
						if (ItemStack.isSameItem(invStack, itemStack)) {
							invStack.shrink(1);
							if (invStack.isEmpty()) {
								inv.setStackInSlot(i, ItemStack.EMPTY);
							}
							//inv.markDirty();
							hasRan = true;
							break;
						}
					}
				}
				if (!satchel.isEmpty() && !hasRan) {
					BaseBagItem item = (BaseBagItem) satchel.getItem();
					var inv = item.getItemHandler(satchel);

					for(int i = 0; i < inv.getSlots(); ++i) {
						ItemStack invStack = inv.getStackInSlot(i);
						if (ItemStack.isSameItem(invStack, itemStack)) {
							invStack.shrink(1);
							if (invStack.isEmpty()) {
								inv.setStackInSlot(i, ItemStack.EMPTY);
							}
							//inv.markDirty();
							hasRan = true;
							break;
						}
					}
				}
			}
		}
	}
}
