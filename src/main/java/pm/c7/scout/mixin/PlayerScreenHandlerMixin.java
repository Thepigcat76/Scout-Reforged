package pm.c7.scout.mixin;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pm.c7.scout.ScoutScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.ScoutMixin.Transformer;
import pm.c7.scout.content.menus.BagSlot;

@Mixin(value = InventoryMenu.class, priority = 950)
@Transformer(PlayerScreenHandlerTransformer.class)
public abstract class PlayerScreenHandlerMixin extends AbstractContainerMenu implements ScoutScreenHandler {
	protected PlayerScreenHandlerMixin() {
		super(null, 0);
	}

	@Unique
	public final NonNullList<BagSlot> scout$satchelSlots = NonNullList.createWithCapacity(ScoutUtil.MAX_SATCHEL_SLOTS);
	@Unique
	public final NonNullList<BagSlot> scout$leftPouchSlots = NonNullList.createWithCapacity(ScoutUtil.MAX_POUCH_SLOTS);
	@Unique
	public final NonNullList<BagSlot> scout$rightPouchSlots = NonNullList.createWithCapacity(ScoutUtil.MAX_POUCH_SLOTS);

	@Inject(method = "<init>", at = @At("RETURN"))
	private void scout$addSlots(Inventory inventory, boolean onServer, Player owner, CallbackInfo callbackInfo) {
		// satchel
		int x = 8;
		int y = 168;

		for (int i = 0; i < ScoutUtil.MAX_SATCHEL_SLOTS; i++) {
			if (i % 9 == 0) {
				x = 8;
			}

			BagSlot slot = new BagSlot(i, x, y);
			slot.index = ScoutUtil.SATCHEL_SLOT_START - i;
			scout$satchelSlots.add(slot);

			x += 18;

			if ((i + 1) % 9 == 0) {
				y += 18;
			}
		}

		// left pouch
		x = 8;
		y = 66;

		for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
			if (i % 3 == 0) {
				x -= 18;
				y += 54;
			}

			BagSlot slot = new BagSlot(i, x, y);
			slot.index = ScoutUtil.LEFT_POUCH_SLOT_START - i;
			scout$leftPouchSlots.add(slot);

			y -= 18;
		}

		// right pouch
		x = 152;
		y = 66;

		for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
			if (i % 3 == 0) {
				x += 18;
				y += 54;
			}

			BagSlot slot = new BagSlot(i, x, y);
			slot.index = ScoutUtil.RIGHT_POUCH_SLOT_START - i;
			scout$rightPouchSlots.add(slot);

			y -= 18;
		}
	}

	@Override
	public final NonNullList<BagSlot> scout$getSatchelSlots() {
		return scout$satchelSlots;
	}

	@Override
	public final NonNullList<BagSlot> scout$getLeftPouchSlots() {
		return scout$leftPouchSlots;
	}

	@Override
	public final NonNullList<BagSlot> scout$getRightPouchSlots() {
		return scout$rightPouchSlots;
	}
}
