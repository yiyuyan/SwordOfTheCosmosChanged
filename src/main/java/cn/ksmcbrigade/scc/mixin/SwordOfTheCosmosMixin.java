package cn.ksmcbrigade.scc.mixin;

import cn.ksmcbrigade.eo.EntityProtection;
import cn.ksmcbrigade.scc.SwordOfTheCosmosChanged;
import net.bullfighter.avaritia.item.SwordoftheCosmosItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = SwordoftheCosmosItem.class,remap = false)
public abstract class SwordOfTheCosmosMixin extends SwordItem {

    public SwordOfTheCosmosMixin(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Inject(method = "<init>",at = @At("TAIL"))
    public void init(CallbackInfo ci){
        //overwrite tier
        this.tier = new Tier() {
            @Override
            public int getUses() {
                return Integer.MAX_VALUE;
            }

            @Override
            public float getSpeed() {
                return (float) Double.POSITIVE_INFINITY;
            }

            @Override
            public float getAttackDamageBonus() {
                return (float) Double.POSITIVE_INFINITY;
            }

            @Override
            public int getLevel() {
                return Integer.MAX_VALUE;
            }

            @Override
            public int getEnchantmentValue() {
                return Integer.MAX_VALUE;
            }

            @Override
            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of();
            }
        };
    }

    @Inject(method = "m_7579_",at = @At(value = "TAIL"))
    public void clickEntity(ItemStack itemstack, LivingEntity entity, LivingEntity sourceentity, CallbackInfoReturnable<Boolean> cir){
        this.swordOfTheCosmosChanged$kill(entity);
        entity.setHealth(0.0f);
    }

    @Unique
    public @NotNull InteractionResultHolder<ItemStack> use(Level p_41432_, Player p_41433_, @NotNull InteractionHand p_41434_) {
        p_41432_.getEntitiesOfClass(Entity.class,new AABB(p_41433_.getOnPos()).inflate(1000d)).forEach(e -> {
            if(e!=p_41433_){
                if(e instanceof LivingEntity livingEntity){
                    e.hurt(e.damageSources().sonicBoom(e),Float.MAX_VALUE);
                    livingEntity.setHealth(0.0f);
                    swordOfTheCosmosChanged$kill(livingEntity);
                    livingEntity.setHealth(0.0f);
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,p_41432_);
                    lightningBolt.setVisualOnly(true);
                    lightningBolt.setPos(livingEntity.getPosition(0));
                    p_41432_.addFreshEntity(lightningBolt);
                }
                else{
                    e.hurt(e.damageSources().sonicBoom(e),Float.MAX_VALUE);
                    swordOfTheCosmosChanged$kill2(e);
                    LightningBolt lightningBolt = new LightningBolt(EntityType.LIGHTNING_BOLT,p_41432_);
                    lightningBolt.setPos(e.getPosition(0));
                    lightningBolt.setVisualOnly(true);
                    p_41432_.addFreshEntity(lightningBolt);
                }
            }

            /*if(p_41433_ instanceof ServerPlayer serverPlayer){
                SwordOfTheCosmosChanged.CHANNEL.sendTo(new SwordOfTheCosmosChanged.Message("reload"),serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }*/
        });
        
        return InteractionResultHolder.success(p_41433_.getItemInHand(p_41434_));
    }

    @Override
    public void inventoryTick(ItemStack p_41404_, Level p_41405_, Entity p_41406_, int p_41407_, boolean p_41408_) {
        if(p_41406_ instanceof Player player){
            player.getAbilities().mayfly = true;
        }
        if(!EntityProtection.safes.contains(p_41406_.getUUID())){
            SwordOfTheCosmosChanged.uuids.add(p_41406_.getUUID());
            EntityProtection.safes.add(p_41406_.getUUID());
            p_41406_.sendSystemMessage(Component.literal("safe done!"));
        }
        super.inventoryTick(p_41404_, p_41405_, p_41406_, p_41407_, p_41408_);
    }

    @Unique
    protected void swordOfTheCosmosChanged$kill(LivingEntity entity){
        if(!entity.isAlive()) return;

        //make it no invulnerable
        entity.setInvulnerable(false);
        entity.setSecondsOnFire(Integer.MAX_VALUE);
        entity.setSharedFlagOnFire(true);

        if(!entity.isAlive()) return;

        entity.removeVehicle();
        entity.removeAllEffects();

        //make it error
        //entity.setId(-1);

        if(!entity.isAlive()) return;

        //remove it
        entity.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.DISCARDED);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.KILLED);
        if(!entity.isAlive()) return;
        entity.onRemovedFromWorld();
        if(!entity.isAlive() && !entity.isRemoved()) return;
        /*//clear it data
        try {
            entity.getEntityData().itemsById = new Int2ObjectOpenHashMap<>();
        }
        catch (Exception e){
            System.out.println("Can't to clear the data");
            e.printStackTrace();
        }*/

        //kill it
        entity.kill();
        if(!entity.isAlive()) return;
        entity.die(entity.damageSources().sonicBoom(entity));
        if(!entity.isAlive()) return;
        entity.dead = true;
        if(!entity.isAlive()) return;
        entity.deathTime = 20;
        entity.hurtTime = 20;

        entity.dead = true;
        entity.noPhysics = true;
    }

    @Unique
    protected void swordOfTheCosmosChanged$kill2(Entity entity){

        if(!entity.isAlive()) return;

        //make it no invulnerable
        entity.setInvulnerable(false);
        entity.setSecondsOnFire(Integer.MAX_VALUE);
        entity.setSharedFlagOnFire(true);
        if(!entity.isAlive()) return;

        entity.removeVehicle();

        //make it error
        //entity.setId(-1);
        if(!entity.isAlive()) return;

        //remove it
        entity.remove(Entity.RemovalReason.UNLOADED_TO_CHUNK);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.KILLED);
        if(!entity.isAlive()) return;
        entity.remove(Entity.RemovalReason.DISCARDED);
        if(!entity.isAlive()) return;
        entity.onRemovedFromWorld();
        if(!entity.isAlive() && !entity.isRemoved()) return;
        /*//clear it data
        try {
            entity.getEntityData().itemsById = new Int2ObjectOpenHashMap<>();
        }
        catch (Exception e){
            System.out.println("Can't to clear the data");
            e.printStackTrace();
        }*/

        //kill it
        entity.kill();
        if(!entity.isAlive()) return;

        entity.noPhysics = true;
    }
}
