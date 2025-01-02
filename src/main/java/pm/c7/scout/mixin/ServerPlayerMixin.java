package pm.c7.scout.mixin;

import com.mojang.authlib.GameProfile;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pm.c7.scout.ScoutScreenHandler;
import pm.c7.scout.ScoutUtil;
import pm.c7.scout.content.items.BaseBagItem;
import pm.c7.scout.content.items.BaseBagItem.BagType;
import pm.c7.scout.content.menus.BagSlot;
import pm.c7.scout.networking.EnableSlotsPayload;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
	// Never called
	private ServerPlayerMixin() {
		super(null, null, 0, null);
	}

	@Inject(method = "die", at = @At("HEAD"))
	private void scout$attemptFixGraveMods(DamageSource source, CallbackInfo callbackInfo) {
		ServerPlayer player = (ServerPlayer) (Object) this;
		ScoutScreenHandler handler = (ScoutScreenHandler) player.inventoryMenu;

		if (!player.level().getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY)) {
			ItemStack backStack = ScoutUtil.findBagItem(player, BagType.SATCHEL, false);
			if (!backStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) backStack.getItem();
				int slots = bagItem.getSlotCount();

				NonNullList<BagSlot> bagSlots = handler.scout$getSatchelSlots();

				for (int i = 0; i < slots; i++) {
					BagSlot slot = bagSlots.get(i);
					slot.setItemHandler(null);
					slot.setEnabled(false);
				}
			}

			ItemStack leftPouchStack = ScoutUtil.findBagItem(player, BagType.POUCH, false);
			if (!leftPouchStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) leftPouchStack.getItem();
				int slots = bagItem.getSlotCount();

				NonNullList<BagSlot> bagSlots = handler.scout$getLeftPouchSlots();

				for (int i = 0; i < slots; i++) {
					BagSlot slot = bagSlots.get(i);
					slot.setItemHandler(null);
					slot.setEnabled(false);
				}
			}

			ItemStack rightPouchStack = ScoutUtil.findBagItem(player, BagType.POUCH, true);
			if (!rightPouchStack.isEmpty()) {
				BaseBagItem bagItem = (BaseBagItem) rightPouchStack.getItem();
				int slots = bagItem.getSlotCount();

				NonNullList<BagSlot> bagSlots = handler.scout$getRightPouchSlots();

				for (int i = 0; i < slots; i++) {
					BagSlot slot = bagSlots.get(i);
					slot.setItemHandler(null);
					slot.setEnabled(false);
				}
			}

			PacketDistributor.sendToPlayer(player, EnableSlotsPayload.INSTANCE);
		}
	}
}
