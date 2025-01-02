package pm.c7.scout.networking;

import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import pm.c7.scout.Scout;
import pm.c7.scout.ScoutScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BaseBagItem;
import pm.c7.scout.content.menus.BagSlot;

public class EnableSlotsPayload implements CustomPacketPayload {
	public static final EnableSlotsPayload INSTANCE = new EnableSlotsPayload();
	public static final StreamCodec<RegistryFriendlyByteBuf, EnableSlotsPayload> STREAM_CODEC = StreamCodec.unit(INSTANCE);
	public static final Type<EnableSlotsPayload> TYPE = new Type<>(Scout.rl("enable_slots"));
	public static final ResourceLocation ENABLE_SLOTS = Scout.rl("enable_slots");

	private EnableSlotsPayload() {
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public void handle(IPayloadContext context) {
		context.enqueueWork(() -> {
			Minecraft mc = Minecraft.getInstance();
			ScoutScreenHandler screenHandler = (ScoutScreenHandler) mc.player.inventoryMenu;

			ItemStack satchelStack = ScoutUtil.findBagItem(mc.player, BaseBagItem.BagType.SATCHEL, false);
			NonNullList<BagSlot> satchelSlots = screenHandler.scout$getSatchelSlots();

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

			ItemStack leftPouchStack = ScoutUtil.findBagItem(mc.player, BaseBagItem.BagType.POUCH, false);
			NonNullList<BagSlot> leftPouchSlots = screenHandler.scout$getLeftPouchSlots();

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

			ItemStack rightPouchStack = ScoutUtil.findBagItem(mc.player, BaseBagItem.BagType.POUCH, true);
			NonNullList<BagSlot> rightPouchSlots = screenHandler.scout$getRightPouchSlots();

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
		});
	}
}
