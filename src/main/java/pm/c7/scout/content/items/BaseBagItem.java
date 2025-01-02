package pm.c7.scout.content.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import pm.c7.scout.ScoutScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.menus.BagSlot;
import pm.c7.scout.networking.EnableSlotsPayload;
import top.theillusivec4.curios.common.inventory.CurioSlot;

import java.util.List;
import java.util.Optional;

public class BaseBagItem extends Item {
	private static final String ITEMS_KEY = "Items";

	private final int slots;
	private final BagType type;

	public BaseBagItem(Properties properties, int slots, BagType type) {
		super(properties.component(DataComponents.CONTAINER, ItemContainerContents.EMPTY));

		if (type == BagType.SATCHEL && slots > ScoutUtil.MAX_SATCHEL_SLOTS) {
			throw new IllegalArgumentException("Satchel has too many slots.");
		}
		if (type == BagType.POUCH && slots > ScoutUtil.MAX_POUCH_SLOTS) {
			throw new IllegalArgumentException("Pouch has too many slots.");
		}

		this.slots = slots;
		this.type = type;
	}

	public int getSlotCount() {
		return this.slots;
	}

	public BagType getType() {
		return this.type;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable TooltipContext context, List<Component> tooltip, TooltipFlag tooltipFlag) {
		super.appendHoverText(stack, context, tooltip, tooltipFlag);
		tooltip.add(Component.translatable("tooltip.scout.slots", Component.literal(String.valueOf(this.slots)).withStyle(ChatFormatting.BLUE)).withStyle(ChatFormatting.GRAY));
	}

	public IItemHandlerModifiable getItemHandler(ItemStack stack) {
		return (IItemHandlerModifiable) stack.getCapability(Capabilities.ItemHandler.ITEM);
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack stack) {
		NonNullList<ItemStack> stacks = NonNullList.create();
		IItemHandlerModifiable inventory = getItemHandler(stack);

		for (int i = 0; i < slots; i++) {
			stacks.add(inventory.getStackInSlot(i));
		}

		if (stacks.stream().allMatch(ItemStack::isEmpty)) return Optional.empty();

		return Optional.of(new BagTooltipData(stacks, slots));
	}

	public void onEquip(ItemStack stack, EquipmentSlot slotRef, LivingEntity entity) {
		if (entity instanceof Player player)
			updateSlots(player);
	}

	public void onUnequip(ItemStack stack, EquipmentSlot slotRef, LivingEntity entity) {
		if (entity instanceof Player player)
			updateSlots(player);
	}

	private void updateSlots(Player player) {
		ScoutScreenHandler handler = (ScoutScreenHandler) player.containerMenu;

		ItemStack satchelStack = ScoutUtil.findBagItem(player, BagType.SATCHEL, false);
		NonNullList<BagSlot> satchelSlots = handler.scout$getSatchelSlots();

		for (int i = 0; i < ScoutUtil.MAX_SATCHEL_SLOTS; i++) {
			BagSlot slot = satchelSlots.get(i);
			slot.setItemHandler(null);
			slot.setEnabled(false);
		}
		if (!satchelStack.isEmpty()) {
			BaseBagItem satchelItem = (BaseBagItem) satchelStack.getItem();
			IItemHandlerModifiable satchelInv = satchelItem.getItemHandler(satchelStack);

			for (int i = 0; i < satchelItem.getSlotCount(); i++) {
				BagSlot slot = satchelSlots.get(i);
				slot.setItemHandler(satchelInv);
				slot.setEnabled(true);
			}
		}

		ItemStack leftPouchStack = ScoutUtil.findBagItem(player, BagType.POUCH, false);
		NonNullList<BagSlot> leftPouchSlots = handler.scout$getLeftPouchSlots();

		for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
			BagSlot slot = leftPouchSlots.get(i);
			slot.setItemHandler(null);
			slot.setEnabled(false);
		}
		if (!leftPouchStack.isEmpty()) {
			BaseBagItem leftPouchItem = (BaseBagItem) leftPouchStack.getItem();
			IItemHandlerModifiable leftPouchInv = leftPouchItem.getItemHandler(leftPouchStack);

			for (int i = 0; i < leftPouchItem.getSlotCount(); i++) {
				BagSlot slot = leftPouchSlots.get(i);
				slot.setItemHandler(leftPouchInv);
				slot.setEnabled(true);
			}
		}

		ItemStack rightPouchStack = ScoutUtil.findBagItem(player, BagType.POUCH, true);
		NonNullList<BagSlot> rightPouchSlots = handler.scout$getRightPouchSlots();

		for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
			BagSlot slot = rightPouchSlots.get(i);
			slot.setItemHandler(null);
			slot.setEnabled(false);
		}
		if (!rightPouchStack.isEmpty()) {
			BaseBagItem rightPouchItem = (BaseBagItem) rightPouchStack.getItem();
			IItemHandlerModifiable rightPouchInv = rightPouchItem.getItemHandler(rightPouchStack);

			for (int i = 0; i < rightPouchItem.getSlotCount(); i++) {
				BagSlot slot = rightPouchSlots.get(i);
				slot.setItemHandler(rightPouchInv);
				slot.setEnabled(true);
			}
		}

		if (player instanceof ServerPlayer serverPlayer) {
			PacketDistributor.sendToPlayer(serverPlayer, EnableSlotsPayload.INSTANCE);
		}
	}

	public boolean canEquip(ItemStack stack, CurioSlot slot, LivingEntity entity) {
		Item item = stack.getItem();

		ItemStack slotStack = slot.getItem();
		Item slotItem = slotStack.getItem();

		if (slotItem instanceof BaseBagItem) {
			if (((BaseBagItem) item).getType() == BagType.SATCHEL) {
				if (((BaseBagItem) slotItem).getType() == BagType.SATCHEL) {
					return true;
				} else {
					return ScoutUtil.findBagItem((Player) entity, BagType.SATCHEL, false).isEmpty();
				}
			} else if (((BaseBagItem) item).getType() == BagType.POUCH) {
				if (((BaseBagItem) slotItem).getType() == BagType.POUCH) {
					return true;
				} else {
					return ScoutUtil.findBagItem((Player) entity, BagType.POUCH, true).isEmpty();
				}
			}
		} else {
			if (((BaseBagItem) item).getType() == BagType.SATCHEL) {
				return ScoutUtil.findBagItem((Player) entity, BagType.SATCHEL, false).isEmpty();
			} else if (((BaseBagItem) item).getType() == BagType.POUCH) {
				return ScoutUtil.findBagItem((Player) entity, BagType.POUCH, true).isEmpty();
			}
		}

		return false;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		var inv = getItemHandler(stack);

		for (int i = 0; i < inv.getSlots(); i++) {
			var invStack = inv.getStackInSlot(i);
			invStack.inventoryTick(level, entity, i, false);
		}
	}


	public void tick(ItemStack stack, CurioSlot slot, LivingEntity entity) {
		var inv = getItemHandler(stack);

		for (int i = 0; i < inv.getSlots(); i++) {
			var invStack = inv.getStackInSlot(i);
			invStack.inventoryTick(entity.level(), entity, i, false);
		}
	}

	public enum BagType {
		SATCHEL,
		POUCH
	}
}
