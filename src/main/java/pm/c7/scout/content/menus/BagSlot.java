package pm.c7.scout.content.menus;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.config.ScoutConfig;
import pm.c7.scout.content.items.BaseBagItem;

public class BagSlot extends Slot {
	public int index;
	private boolean enabled = true;
	private int realX;
	private int realY;
	private IItemHandlerModifiable itemHandler;

	public BagSlot(int index, int x, int y) {
		super(null, index, x, y);
		this.index = index;
		this.realX = x;
		this.realY = y;
	}

	public void setItemHandler(IItemHandlerModifiable itemHandler) {
		this.itemHandler = itemHandler;
	}

	public IItemHandlerModifiable getItemHandler() {
		return itemHandler;
	}

	public void setEnabled(boolean state) {
		enabled = state;
	}

	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		if (stack.getItem() instanceof BaseBagItem)
			return false;

		if (stack.is(ScoutUtil.TAG_ITEM_BLACKLIST)) {
			return false;
		}

		if (stack.getItem() instanceof BlockItem blockItem) {
			if (blockItem.getBlock() instanceof ShulkerBoxBlock)
				return enabled && itemHandler != null && ScoutConfig.allowShulkers;
		}

		return enabled && itemHandler != null;
	}

	@Override
	public boolean mayPickup(@NotNull Player player) {
		return enabled && itemHandler != null;
	}

	@Override
	public boolean isActive() {
		return enabled && itemHandler != null;
	}

	@Override
	public @NotNull ItemStack getItem() {
		return enabled && this.itemHandler != null ? this.itemHandler.getStackInSlot(this.index) : ItemStack.EMPTY;
	}

	@Override
	public void set(ItemStack stack) {
		if (enabled && this.itemHandler != null) {
			this.itemHandler.setStackInSlot(this.index, stack);
			this.setChanged();
		}
	}

	@Override
	public @NotNull ItemStack remove(int amount) {
		return enabled && this.itemHandler != null ? this.itemHandler.extractItem(this.index, amount, false) : ItemStack.EMPTY;
	}

	@Override
	public int getMaxStackSize() {
		return enabled && this.itemHandler != null ? this.itemHandler.getSlotLimit(0) : 0;
	}

	public int getX() {
		return this.realX;
	}
	public int getY() {
		return this.realY;
	}
	public void setX(int x) {
		this.realX = x;
	}
	public void setY(int y) {
		this.realY = y;
	}
}
