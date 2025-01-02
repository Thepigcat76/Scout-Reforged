package pm.c7.scout.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.Nullable;
import pm.c7.scout.Scout;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.client.gui.BagTooltipComponent;
import pm.c7.scout.client.render.SatchelRenderLayer;
import pm.c7.scout.content.items.BagTooltipData;
import pm.c7.scout.client.render.PouchRenderLayer;
import pm.c7.scout.mixin.client.AbstractContainerScreenAccessor;
import pm.c7.scout.content.menus.BagSlot;

@Mod(Scout.MODID)
public class ScoutClient {
	public ScoutClient(IEventBus modEventBus, ModContainer modContainer) {
		modEventBus.addListener(this::registerTooltipComponent);
		modEventBus.addListener(this::registerLayerDefinitions);
		modEventBus.addListener(this::registerRenderLayers);

		NeoForge.EVENT_BUS.addListener(this::screenInit);
	}

	private void screenInit(ScreenEvent.Init.Post event) {
		Minecraft mc = Minecraft.getInstance();
		Screen screen = event.getScreen();
		if (screen instanceof AbstractContainerScreen<?> handledScreen && mc.player != null) {
			if (ScoutUtilClient.isScreenBlacklisted(screen)) {
				// realistically no one is going to have a screen bigger than 2147483647 pixels
				for (Slot slot : ScoutUtil.getAllBagSlots(mc.player.inventoryMenu)) {
					BagSlot bagSlot = (BagSlot) slot;
					bagSlot.setX(Integer.MAX_VALUE);
					bagSlot.setY(Integer.MAX_VALUE);
				}
				return;
			}

			var handledScreenAccessor = (AbstractContainerScreenAccessor<?>) handledScreen;
			AbstractContainerMenu handler = handledScreenAccessor.getHandler();

			var playerInventory = mc.player.getInventory();

			int x;
			int y;

			// satchel
			var _hotbarSlot1 = handler.slots.stream().filter(slot-> slot.container.equals(playerInventory) && slot.index == 0).findFirst();
			Slot hotbarSlot1 = _hotbarSlot1.orElse(null);
			if (hotbarSlot1 != null) {
				if (hotbarSlot1.isFake()) {
					for (int i = 0; i < ScoutUtil.MAX_SATCHEL_SLOTS; i++) {
						BagSlot slot = (BagSlot) ScoutUtil.getBagSlot(ScoutUtil.SATCHEL_SLOT_START - i, mc.player.inventoryMenu);
						if (slot != null) {
							slot.setX(Integer.MAX_VALUE);
							slot.setY(Integer.MAX_VALUE);
						}
					}
				} else {
					x = hotbarSlot1.x;
					y = hotbarSlot1.y + 27;

					for (int i = 0; i < ScoutUtil.MAX_SATCHEL_SLOTS; i++) {
						if (i % 9 == 0) {
							x = hotbarSlot1.x;
						}

						BagSlot slot = (BagSlot) ScoutUtil.getBagSlot(ScoutUtil.SATCHEL_SLOT_START - i, mc.player.inventoryMenu);
						if (slot != null) {
							slot.setX(x);
							slot.setY(y);
						}

						x += 18;

						if ((i + 1) % 9 == 0) {
							y += 18;
						}
					}
				}
			}

			// left pouch
			var _topLeftSlot = handler.slots.stream().filter(slot->slot.container.equals(playerInventory) && slot.getSlotIndex() == 9).findFirst();
			Slot topLeftSlot = _topLeftSlot.orElse(null);
			if (topLeftSlot != null) {
				if (!topLeftSlot.isActive()) {
					for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
						BagSlot slot = (BagSlot) ScoutUtil.getBagSlot(ScoutUtil.LEFT_POUCH_SLOT_START - i, mc.player.inventoryMenu);
						if (slot != null) {
							slot.setX(Integer.MAX_VALUE);
							slot.setY(Integer.MAX_VALUE);
						}
					}
				} else {
					x = topLeftSlot.x;
					y = topLeftSlot.y - 18;

					for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
						if (i % 3 == 0) {
							x -= 18;
							y += 54;
						}

						BagSlot slot = (BagSlot) ScoutUtil.getBagSlot(ScoutUtil.LEFT_POUCH_SLOT_START - i, mc.player.inventoryMenu);
						if (slot != null) {
							slot.setX(x);
							slot.setY(y);
						}

						y -= 18;
					}
				}
			}

			// right pouch
			var _topRightSlot = handler.slots.stream().filter(slot->slot.container.equals(playerInventory) && slot.getSlotIndex() == 17).findFirst();
			Slot topRightSlot = _topRightSlot.orElse(null);
			if (topRightSlot != null) {
				if (topLeftSlot.isFake()) {
					for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
						BagSlot slot = (BagSlot) ScoutUtil.getBagSlot(ScoutUtil.RIGHT_POUCH_SLOT_START - i, mc.player.inventoryMenu);
						if (slot != null) {
							slot.setX(Integer.MAX_VALUE);
							slot.setY(Integer.MAX_VALUE);
						}
					}
				} else {
					x = topRightSlot.x;
					y = topRightSlot.y - 18;

					for (int i = 0; i < ScoutUtil.MAX_POUCH_SLOTS; i++) {
						if (i % 3 == 0) {
							x += 18;
							y += 54;
						}

						BagSlot slot = (BagSlot) ScoutUtil.getBagSlot(ScoutUtil.RIGHT_POUCH_SLOT_START - i, mc.player.inventoryMenu);
						if (slot != null) {
							slot.setX(x);
							slot.setY(y);
						}

						y -= 18;
					}
				}
			}
		}
	}

	private void registerTooltipComponent(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(BagTooltipData.class, BagTooltipComponent::new);
	}

	private void registerRenderLayers(EntityRenderersEvent.AddLayers event) {
//		@Nullable PlayerRenderer renderer = (PlayerRenderer) (Object) event.getRenderer(EntityType.PLAYER);
//		renderer.addLayer(new PouchRenderLayer<>(renderer, event.getContext().getItemInHandRenderer()));
//		renderer.addLayer(new SatchelRenderLayer<>(renderer));
	}

	private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

	}
}
