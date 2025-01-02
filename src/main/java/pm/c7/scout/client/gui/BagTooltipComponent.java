package pm.c7.scout.client.gui;

import com.google.common.math.IntMath;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BagTooltipData;

import java.math.RoundingMode;

public class BagTooltipComponent implements ClientTooltipComponent {
	private final NonNullList<ItemStack> inventory;
	private final int slotCount;

	public BagTooltipComponent(BagTooltipData data) {
		this.inventory = data.getInventory();
		this.slotCount = data.getSlotCount();
	}

	@Override
	public int getHeight() {
		return (18 * IntMath.divide(slotCount, 6, RoundingMode.UP)) + 2;
	}

	@Override
	public int getWidth(Font font) {
		return 18 * (Math.min(slotCount, 6));
	}

	@Override
	public void renderImage(Font font, int x, int y, GuiGraphics guiGraphics) {
		int originalX = x;

		for (int i = 0; i < slotCount; i++) {
			this.drawSlot(x, y, i, guiGraphics, font);

			x += 18;
			if ((i + 1) % 6 == 0) {
				y += 18;
				x = originalX;
			}
		}
	}

	private void drawSlot(int x, int y, int index, GuiGraphics graphics, Font font) {
		ItemStack itemStack = this.inventory.get(index);
		graphics.blit(ScoutUtil.SLOT_TEXTURE, x, y, 0, 46, 7, 18, 18, 256, 256);
		graphics.renderItem(itemStack, x + 1, y + 1, index);
		graphics.renderItemDecorations(font, itemStack, x + 1, y + 1);
	}
}
