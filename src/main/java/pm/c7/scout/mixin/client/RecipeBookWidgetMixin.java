package pm.c7.scout.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BaseBagItem;
import pm.c7.scout.content.items.BaseBagItem.BagType;

// Lower priority to take priority over Better Recipe Book
@Mixin(value = RecipeBookComponent.class, priority = 950)
public class RecipeBookWidgetMixin {
	@Shadow
	protected Minecraft minecraft;
	@Shadow
	private int xOffset;

	@Inject(method = "updateScreenPosition", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	private void scout$modifyRecipeBookPosition(int width, int backgroundWidth, CallbackInfoReturnable<Integer> callbackInfo, int x) {
		if (this.minecraft != null && this.minecraft.player != null && this.isVisible()) {
			ItemStack leftPouchStack = ScoutUtil.findBagItem(this.minecraft.player, BagType.POUCH, false);
			if (!leftPouchStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
				int slots = bagItem.getSlotCount();

				int columns = (int) Math.ceil(slots / 3);

				// Realign as best we can when "Keep crafting screens centered" is enabled in Better Recipe Book
				if (this.xOffset != 86) {
					int diff = this.xOffset - 86;
					x -= diff;
				}

				x += 18 * columns;

				callbackInfo.setReturnValue(x);
			}
		}
	}

	@Shadow
	public boolean isVisible() {
		return false;
	}
}
