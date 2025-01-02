package pm.c7.scout.mixin.client;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BaseBagItem;

@Mixin(AbstractFurnaceScreen.class)
public abstract class AbstractFurnaceScreenMixin<T extends AbstractFurnaceMenu> extends AbstractContainerScreen<T> implements RecipeUpdateListener {
	public AbstractFurnaceScreenMixin() {
		super(null, null, null);
	}

	@Inject(method = "hasClickedOutside", at = @At("TAIL"), cancellable = true)
	private void scout$adjustOutsideBounds(double mouseX, double mouseY, int left, int top, int button, CallbackInfoReturnable<Boolean> callbackInfo) {
		if (this.minecraft != null && this.minecraft.player != null) {
			ItemStack backStack = ScoutUtil.findBagItem(this.minecraft.player, BaseBagItem.BagType.SATCHEL, false);
			if (!backStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
				int slots = bagItem.getSlotCount();
				int rows = (int) Math.ceil(slots / 9);

				if (mouseY < (top + this.imageHeight) + 8 + (18 * rows) && mouseY >= (top + this.imageHeight) && mouseX >= left && mouseY < (left + this.imageWidth)) {
					callbackInfo.setReturnValue(false);
				}
			}

			ItemStack leftPouchStack = ScoutUtil.findBagItem(this.minecraft.player, BaseBagItem.BagType.POUCH, false);
			if (!leftPouchStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
				int slots = bagItem.getSlotCount();
				int columns = (int) Math.ceil(slots / 3);

				if (mouseX >= left - (columns * 18) && mouseX < left && mouseY >= (top + this.imageHeight) - 90 && mouseY < (top + this.imageHeight) - 22) {
					callbackInfo.setReturnValue(false);
				}
			}

			ItemStack rightPouchStack = ScoutUtil.findBagItem(this.minecraft.player, BaseBagItem.BagType.POUCH, true);
			if (!rightPouchStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
				int slots = bagItem.getSlotCount();
				int columns = (int) Math.ceil(slots / 3);

				if (mouseX >= (left + this.imageWidth) && mouseX < (left + this.imageWidth) + (columns * 18) && mouseY >= (top + this.imageHeight) - 90 && mouseY < (top + this.imageHeight) - 22) {
					callbackInfo.setReturnValue(false);
				}
			}
		}
	}
}
