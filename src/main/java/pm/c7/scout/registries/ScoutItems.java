package pm.c7.scout.registries;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import pm.c7.scout.Scout;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BaseBagItem;

public class ScoutItems {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Scout.MODID);

	public static final DeferredItem<Item> TANNED_LEATHER = ITEMS.register("tanned_leather", () -> new Item(new Item.Properties()));
	public static final DeferredItem<Item> SATCHEL_STRAP = ITEMS.register("satchel_strap", () -> new Item(new Item.Properties()));
	public static final DeferredItem<BaseBagItem> SATCHEL = ITEMS.register("satchel", () -> new BaseBagItem(new Item.Properties()
		.stacksTo(1), ScoutUtil.MAX_SATCHEL_SLOTS / 2, BaseBagItem.BagType.SATCHEL));
	public static final DeferredItem<BaseBagItem> UPGRADED_SATCHEL = ITEMS.register("upgraded_satchel", () -> new BaseBagItem(new Item.Properties()
		.stacksTo(1)
		.rarity(Rarity.RARE), ScoutUtil.MAX_SATCHEL_SLOTS, BaseBagItem.BagType.SATCHEL));
	public static final DeferredItem<BaseBagItem> POUCH = ITEMS.register("pouch", () -> new BaseBagItem(new Item.Properties()
		.stacksTo(1), ScoutUtil.MAX_POUCH_SLOTS / 2, BaseBagItem.BagType.POUCH));
	public static final DeferredItem<BaseBagItem> UPGRADED_POUCH = ITEMS.register("upgraded_pouch", () -> new BaseBagItem(new Item.Properties()
		.stacksTo(1)
		.rarity(Rarity.RARE), ScoutUtil.MAX_POUCH_SLOTS, BaseBagItem.BagType.POUCH));
}
