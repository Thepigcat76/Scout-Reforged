package pm.c7.scout;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pm.c7.scout.content.items.BaseBagItem;
import pm.c7.scout.content.items.BaseBagItem.BagType;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;
import java.util.Optional;

public class ScoutUtil {
	public static final Logger LOGGER = LoggerFactory.getLogger("Scout");
	public static final ResourceLocation SLOT_TEXTURE = Scout.rl("textures/gui/slots.png");

	public static final TagKey<Item> TAG_ITEM_BLACKLIST = TagKey.create(Registries.ITEM, Scout.rl("blacklist"));

	public static final int MAX_SATCHEL_SLOTS = 18;
	public static final int MAX_POUCH_SLOTS = 6;
	public static final int TOTAL_SLOTS = MAX_SATCHEL_SLOTS + MAX_POUCH_SLOTS + MAX_POUCH_SLOTS;

	public static final int SATCHEL_SLOT_START = -1100;
	public static final int LEFT_POUCH_SLOT_START = SATCHEL_SLOT_START - MAX_SATCHEL_SLOTS;
	public static final int RIGHT_POUCH_SLOT_START = LEFT_POUCH_SLOT_START - MAX_POUCH_SLOTS;
	public static final int BAG_SLOTS_END = RIGHT_POUCH_SLOT_START - MAX_POUCH_SLOTS;

	public static ItemStack findBagItem(Player player, BaseBagItem.BagType type, boolean right) {
		ItemStack targetStack = ItemStack.EMPTY;

		boolean hasFirstPouch = false;
		Optional<ICuriosItemHandler> _component = CuriosApi.getCuriosInventory(player);
		if (_component.isPresent()) {
			ICuriosItemHandler component = _component.get();
			IItemHandlerModifiable equippedCurios = component.getEquippedCurios();
			for (int i = 0; i < equippedCurios.getSlots(); i++) {
				ItemStack slotStack = equippedCurios.getStackInSlot(i);

				if (slotStack.getItem() instanceof BaseBagItem bagItem) {
					if (bagItem.getType() == type) {
						if (type == BagType.POUCH) {
							if (right && !hasFirstPouch) {
								hasFirstPouch = true;
							} else {
								targetStack = slotStack;
								break;
							}
						} else {
							targetStack = slotStack;
							break;
						}
					}
				}
			}
		}

		return targetStack;
	}

	public static boolean isBagSlot(int slot) {
		return slot <= SATCHEL_SLOT_START && slot > BAG_SLOTS_END;
	}

	public static @Nullable Slot getBagSlot(int slot, InventoryMenu playerScreenHandler) {
		var scoutScreenHandler = (ScoutScreenHandler) playerScreenHandler;
		if (slot <= SATCHEL_SLOT_START && slot > LEFT_POUCH_SLOT_START) {
			int realSlot = Mth.abs(slot - SATCHEL_SLOT_START);
			var slots = scoutScreenHandler.scout$getSatchelSlots();

			return slots.get(realSlot);
		} else if (slot <= LEFT_POUCH_SLOT_START && slot > RIGHT_POUCH_SLOT_START) {
			int realSlot = Mth.abs(slot - LEFT_POUCH_SLOT_START);
			var slots = scoutScreenHandler.scout$getLeftPouchSlots();

			return slots.get(realSlot);
		} else if (slot <= RIGHT_POUCH_SLOT_START && slot > BAG_SLOTS_END) {
			int realSlot = Mth.abs(slot - RIGHT_POUCH_SLOT_START);
			var slots = scoutScreenHandler.scout$getRightPouchSlots();

			return slots.get(realSlot);
		} else {
			return null;
		}
	}

	public static NonNullList<Slot> getAllBagSlots(InventoryMenu playerScreenHandler) {
		ScoutScreenHandler scoutScreenHandler = (ScoutScreenHandler) playerScreenHandler;
		NonNullList<Slot> out = NonNullList.createWithCapacity(TOTAL_SLOTS);
		out.addAll(scoutScreenHandler.scout$getSatchelSlots());
		out.addAll(scoutScreenHandler.scout$getLeftPouchSlots());
		out.addAll(scoutScreenHandler.scout$getRightPouchSlots());
		return out;
	}
}
