package pm.c7.scout;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.CompoundContainer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.EventBus;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.wrapper.InvWrapper;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredRegister;
import pm.c7.scout.client.ScoutUtilClient;
import pm.c7.scout.content.items.BaseBagItem;
import pm.c7.scout.content.menus.BagSlot;
import pm.c7.scout.mixin.client.AbstractContainerScreenAccessor;
import pm.c7.scout.networking.EnableSlotsPayload;
import pm.c7.scout.registries.ScoutItems;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.type.capability.ICurio;

@Mod(Scout.MODID)
public class Scout {
	public static final String MODID = "scout";
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID);

	public Scout(IEventBus modEventBus, ModContainer modContainer) {
		TABS.register(modEventBus);
		ScoutItems.ITEMS.register(modEventBus);

		modEventBus.addListener(this::registerPayloads);
		modEventBus.addListener(this::registerCapabilities);
	}

	private void registerPayloads(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(MODID);
		registrar.playToClient(EnableSlotsPayload.TYPE, EnableSlotsPayload.STREAM_CODEC, EnableSlotsPayload::handle);
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		for (Item item : BuiltInRegistries.ITEM) {
			if (item instanceof BaseBagItem baseBagItem) {
				event.registerItem(Capabilities.ItemHandler.ITEM, (stack, ctx) -> new ComponentItemHandler(stack, DataComponents.CONTAINER, baseBagItem.getSlotCount()), item);
				event.registerItem(CuriosCapability.ITEM, (stack, ctx) -> new ICurio() {
					@Override
					public ItemStack getStack() {
						return stack;
					}
				}, baseBagItem);
			}
		}
	}

	public static ResourceLocation rl(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}

	static {
		TABS.register("scout", () -> CreativeModeTab.builder()
			.icon(ScoutItems.SATCHEL::toStack)
			.title(Component.translatable("itemGroup.scout.itemgroup"))
			.displayItems((context, entries) -> {
				entries.accept(ScoutItems.TANNED_LEATHER);
				entries.accept(ScoutItems.SATCHEL_STRAP);
				entries.accept(ScoutItems.SATCHEL);
				entries.accept(ScoutItems.UPGRADED_SATCHEL);
				entries.accept(ScoutItems.POUCH);
				entries.accept(ScoutItems.UPGRADED_POUCH);
			}).build());
	}
}
