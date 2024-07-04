package cn.ksmcbrigade.scc.mixin;

import cn.ksmcbrigade.eo.EntityProtection;
import cn.ksmcbrigade.scc.SwordOfTheCosmosChanged;
import net.bullfighter.avaritia.init.AvaritiaModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> p_20966_, Level p_20967_) {
        super(p_20966_, p_20967_);
    }

    @Shadow public abstract boolean isCreative();

    @Shadow public abstract boolean isSpectator();

    @Shadow public abstract Abilities getAbilities();

    @Shadow public abstract Inventory getInventory();

    @Shadow public abstract boolean canBeHitByProjectile();

    @Inject(method = "tick",at = @At("TAIL"))
    public void tick(CallbackInfo ci){
        try{
            if(!this.isCreative() && !this.isSpectator() && this.getAbilities().mayfly && this.getInventory().countItem(AvaritiaModItems.SWORDOFTHE_COSMOS.get())<=0){
                this.getAbilities().mayfly = false;
                this.getAbilities().flying = false;
                if(SwordOfTheCosmosChanged.uuids.contains(this.getUUID())){
                    EntityProtection.safes.remove(EntityProtection.safes.indexOf(this.getUUID()));
                    SwordOfTheCosmosChanged.uuids.remove(this.getUUID());
                }
            }
        }
        catch (Exception e){
            System.out.println("An error in player tick: "+e.getMessage());
            e.printStackTrace();
        }
    }
}
