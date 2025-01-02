package pm.c7.scout.mixin.client;

import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BaseBagItem;
import pm.c7.scout.content.items.BaseBagItem.BagType;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> implements RecipeUpdateListener {
	private InventoryScreenMixin() {
		super(null, null, null);
	}

	@Inject(method = "hasClickedOutside", at = @At("TAIL"), cancellable = true)
	private void scout$adjustOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.minecraft != null && this.minecraft.player != null) {
			ItemStack backStack = ScoutUtil.findBagItem(this.minecraft.player, BagType.SATCHEL, false);
			ScoutUtil.LOGGER.debug("Satchel: {}", backStack);
			if (!backStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
				int slots = bagItem.getSlotCount();
				int rows = (int) (double) (slots / 9);
				ScoutUtil.LOGGER.debug("Found satchel");

				if (mouseY < (top + this.imageHeight) + 8 + (18 * rows) && mouseY >= (top + this.imageHeight) && mouseX >= left && mouseY < (left + this.imageWidth)) {
					ScoutUtil.LOGGER.debug("Returning false");
					callbackInfo.setReturnValue(false);
				}
			}

			ItemStack leftPouchStack = ScoutUtil.findBagItem(this.minecraft.player, BagType.POUCH, false);
			if (!leftPouchStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
				int slots = bagItem.getSlotCount();
				int columns = (int) (double) (slots / 3);

				if (mouseX >= left - (columns * 18) && mouseX < left && mouseY >= (top + this.imageHeight) - 90 && mouseY < (top + this.imageHeight) - 22) {
					callbackInfo.setReturnValue(false);
				}
			}

			ItemStack rightPouchStack = ScoutUtil.findBagItem(this.minecraft.player, BagType.POUCH, true);
			if (!rightPouchStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
				int slots = bagItem.getSlotCount();
				int columns = (int) (double) (slots / 3);

				if (mouseX >= (left + this.imageWidth) && mouseX < (left + this.imageWidth) + (columns * 18) && mouseY >= (top + this.imageHeight) - 90 && mouseY < (top + this.imageHeight) - 22) {
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}
