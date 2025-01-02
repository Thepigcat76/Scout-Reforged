package pm.c7.scout.mixin;


import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.c7.scout.ScoutMixin.Transformer;
import pm.c7.scout.ScoutUtil;

@Mixin(value = AbstractContainerMenu.class, priority = 950)
@Transformer(ScreenHandlerTransformer.class)
public abstract class AbstractContainerMenuMixin {
	@Inject(method = "doClick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/inventory/AbstractContainerMenu;getCarried()Lnet/minecraft/world/item/ItemStack;", ordinal = 11), locals = LocalCapture.CAPTURE_FAILEXCEPTION)
	public void scout$fixDoubleClick(int slotIndex, int button, ClickType actionType, Player player, CallbackInfo ci, Inventory playerInventory, Slot slot3) {
		ItemStack cursorStack = this.getCarried();
		if (!cursorStack.isEmpty() && (!slot3.hasItem() || !slot3.mayPickup(player))) {
			var slots = ScoutUtil.getAllBagSlots(player.inventoryMenu);
			var k = button == 0 ? 0 : ScoutUtil.TOTAL_SLOTS - 1;
			var o = button == 0 ? 1 : -1;

			for (int n = 0; n < 2; ++n) {
				for (int p = k; p >= 0 && p < slots.size() && cursorStack.getCount() < cursorStack.getMaxStackSize(); p += o) {
					Slot slot4 = slots.get(p);
					if (slot4.hasItem() && canItemQuickReplace(slot4, cursorStack, true) && slot4.mayPickup(player) && this.canTakeItemForPickAll(cursorStack, slot4)) {
						ItemStack itemStack6 = slot4.getItem();
						if (n != 0 || itemStack6.getCount() != itemStack6.getMaxStackSize()) {
							ItemStack itemStack7 = slot4.safeTake(itemStack6.getCount(), cursorStack.getMaxStackSize() - cursorStack.getCount(), player);
							cursorStack.grow(itemStack7.getCount());
						}
					}
				}
			}
		}
	}

	@Shadow
	public static boolean canItemQuickReplace(@Nullable Slot slot, ItemStack stack, boolean allowOverflow) {
		return false;
	}

	@Shadow
	public boolean canTakeItemForPickAll(ItemStack stack, Slot slot) {
		return true;
	}

	@Shadow
	public abstract ItemStack getCarried();
}
