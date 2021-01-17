package pbone.randomadditions.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pbone.randomadditions.RandomAdditions;

@Mixin(InGameHud.class)
public abstract class ArmorToughnessMixin extends DrawableHelper {
    private static final Identifier ICONS = new Identifier(RandomAdditions.MOD_ID, "textures/gui/icons.png");

    @Shadow private int scaledWidth;

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow private int scaledHeight;

    @Shadow @Final private MinecraftClient client;

    @Inject(method="renderStatusBars", at = @At("TAIL"))
    private void renderStatusBars(MatrixStack matrices, CallbackInfo ci) {
        if (!RandomAdditions.ShowArmorToughnessBar)
            return;

        client.getTextureManager().bindTexture(ICONS);

        PlayerEntity playerEntity = getCameraPlayer();
        int scaledScaledHeight = this.scaledHeight - 39;
        int scaledScaledWidth = scaledWidth / 2 + 90;
        this.client.getProfiler().swap("air");
        int maxAir = playerEntity.getMaxAir();
        int minAir = Math.min(playerEntity.getAir(), maxAir);
        int yModifier = playerEntity.isSubmergedIn(FluidTags.WATER) || minAir < maxAir ? 10 : 0;
        int origY = scaledScaledHeight - yModifier - 10;
        int armorToughness = (int)playerEntity.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS);

        for(int i = 10; i >0; i--) {
            if (armorToughness > 0) {
                int origX = scaledScaledWidth - i * 8;

                if (i * 2 + 1 < armorToughness) {
                    this.drawTexture(matrices, origX, origY, 18, 0, 9, 9);
                }

                if (i * 2 + 1 == armorToughness) {
                    this.drawTexture(matrices, origX, origY, 9, 0, 9, 9);
                }

                if (i * 2 + 1 > armorToughness) {
                    this.drawTexture(matrices, origX, origY, 0, 0, 9, 9);
                }
            }
        }

        client.getProfiler().pop();
    }
}
