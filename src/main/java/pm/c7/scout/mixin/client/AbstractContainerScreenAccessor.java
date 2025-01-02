package pm.c7.scout.mixin.client;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractContainerScreen.class)
public interface AbstractContainerScreenAccessor<T extends AbstractContainerMenu> {
	@Accessor("leftPos")
	int getLeftPos();
	@Accessor("topPos")
	int getTopPos();
	@Accessor("imageWidth")
	int getBackgroundWidth();
	@Accessor("imageHeight")
	int getBackgroundHeight();
	@Accessor("menu")
	T getHandler();
}
