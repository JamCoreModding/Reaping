package io.github.jamalam360.reaping.mixin;

import io.github.jamalam360.reaping.ReapingExpectPlatform;
import io.github.jamalam360.reaping.ReapingMod;
import io.github.jamalam360.reaping.logic.CustomReapableEntityDuck;
import io.github.jamalam360.reaping.logic.ReapingHelper;
import io.github.jamalam360.reaping.registry.ReapingItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * @author Jamalam360
 */

@Mixin(VillagerEntity.class)
public abstract class VillagerEntityMixin extends MerchantEntity implements CustomReapableEntityDuck {
    private boolean reaping$remainSmall = false;
    private int reaping$remainingSmallTicks = 0;

    public VillagerEntityMixin(EntityType<? extends MerchantEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(
            method = "writeCustomDataToNbt",
            at = @At("TAIL")
    )
    public void reaping$serializeRemainSmall(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("reaping$remainSmall", this.reaping$remainSmall);
        nbt.putInt("reaping$remainSmallTicks", this.reaping$remainingSmallTicks);
    }

    @Inject(
            method = "readCustomDataFromNbt",
            at = @At("TAIL")
    )
    public void reaping$deserializeRemainSmall(NbtCompound nbt, CallbackInfo ci) {
        this.reaping$remainSmall = nbt.getBoolean("reaping$remainSmall");
        this.reaping$remainingSmallTicks = nbt.getInt("reaping$remainSmallTicks");
    }

    @Inject(
            method = "tick",
            at = @At("TAIL")
    )
    public void reaping$tick(CallbackInfo ci) {
        if (this.reaping$remainingSmallTicks != 0 && this.reaping$remainSmall) {
            this.reaping$remainingSmallTicks--;
        } else if (this.reaping$remainingSmallTicks <= 0 && this.reaping$remainSmall) {
            this.reaping$remainSmall = false;
            ReapingExpectPlatform.setScale(this, 1f);
        }
    }

    @Inject(
            method = "interactMob",
            at = @At("HEAD"),
            cancellable = true
    )
    public void reaping$reapVillager(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (ReapingHelper.tryReap(player, this, player.getStackInHand(hand)) == ActionResult.SUCCESS) {
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Override
    public ActionResult reaping$onReaped(@Nullable PlayerEntity user, ItemStack toolStack) {
        int lootingLvl = EnchantmentHelper.getLevel(Enchantments.LOOTING, toolStack);

        if (!this.reaping$remainSmall) {
            this.reaping$remainSmall = true;
            this.reaping$remainingSmallTicks = ReapingMod.RANDOM.nextInt(50 * 20, 120 * 20);

            this.dropStack(new ItemStack(ReapingItems.HUMAN_MEAT.get(), lootingLvl == 0 ? 1 : this.world.random.nextInt(lootingLvl) + 1));
            ReapingExpectPlatform.setScale(this, 0.45f);
            this.playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0f, 1.0f);

            if (user != null) {
                this.damage(DamageSource.player(user), 1.0f);
            } else {
                this.damage(DamageSource.GENERIC, 1.0f);
            }

            return ActionResult.SUCCESS;
        } else if (!this.isDead()) {
            this.kill();
            this.playSound(SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
            this.dropStack(new ItemStack(Items.BONE, lootingLvl == 0 ? 1 : this.world.random.nextInt(lootingLvl) + 1));

            return ActionResult.SUCCESS;
        } else {
            return ActionResult.PASS;
        }
    }
}
