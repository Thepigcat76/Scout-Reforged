package pm.c7.scout.content.items;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

public class BagTooltipData implements TooltipComponent {
	private final NonNullList<ItemStack> inventory;
	private final int slotCount;

	public BagTooltipData(NonNullList<ItemStack> inventory, int slots) {
		this.inventory = inventory;
		this.slotCount = slots;
	}

	public NonNullList<ItemStack> getInventory() {
		return this.inventory;
	}

	public int getSlotCount() {
		return this.slotCount;
	}
}
