package com.enderdevelopment.plugin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import java.util.*;

final class RaidSystems implements Listener {
    private final Main plugin;
    private final NamespacedKey kindKey, ownerKey, shieldKey;
    private final Random random = new Random();
    private final BukkitTask ticker;
    private long nightCheck;
    private static final String[] HUNTERS = {"valka", "varkhen", "azra", "hacker", "luciano"};
    private static final String[] WORKERS = {"Samantha", "Bruno", "Paula", "Mauricio", "Alisson", "Marina", "Kain", "Marcos CLT", "Billy", "Gabriel", "Janus", "Luminus", "Cesar"};

    RaidSystems(Main plugin) { this.plugin=plugin; kindKey=new NamespacedKey(plugin,"entity_kind"); ownerKey=new NamespacedKey(plugin,"pet_owner"); shieldKey=new NamespacedKey(plugin,"fran_shield"); ticker=Bukkit.getScheduler().runTaskTimer(plugin,this::tick,20L,20L); }
    void shutdown(){ticker.cancel();}

    boolean spawnNamed(Player player,String raw){String id=raw.toLowerCase(Locale.ROOT); Location at=player.getLocation().add(player.getLocation().getDirection().normalize().multiply(4)); switch(id){
        case "fran" -> spawnFran(at);
        case "denise" -> spawnHost(at,EntityType.BLAZE,"Denise - Forma Ancestral",48000,"denise");
        case "rene","renê" -> {LivingEntity e=create(at,EntityType.PILLAGER,"Renê - Forma Ancestral",48000,"rene"); if(e!=null){spawnWorker(at.clone().add(2,0,0),"White - Raposa Robô",EntityType.FOX,"white");spawnWorker(at.clone().add(-2,0,0),"Snow - Assassina",EntityType.STRAY,"snow");spawnWorker(at.clone().add(0,0,3),"Bonequinho Chibi",EntityType.VEX,"chibi");}}
        case "destino_corrompido","destino" -> spawnHost(at,EntityType.WITHER_SKELETON,"Destino Corrompido",69000,"corrupted_destiny");
        case "valka","varkhen","azra","hacker","luciano" -> spawnHunter(at,id);
        case "litoral" -> spawnWorker(at,"Litoral",EntityType.ALLAY,"litoral"); case "larissa" -> spawnWorker(at,"Larissa - Operária do Amor",EntityType.CAT,"larissa");
        case "ze_do_po","zé_do_pó" -> spawnWorker(at,"Zé do Pó",EntityType.VILLAGER,"dust_child"); case "idris" -> spawnWorker(at,"Idris - Operário do Fogo",EntityType.BLAZE,"idris");
        case "white" -> spawnWorker(at,"White",EntityType.FOX,"white"); case "snow" -> spawnWorker(at,"Snow",EntityType.STRAY,"snow"); case "nox" -> spawnWorker(at,"Nox - Dragão do Abismo",EntityType.PHANTOM,"nox");
        case "reparador" -> spawnPet(at,EntityType.SLIME,"Pequeno Reparador da Realidade","repairer"); case "jade" -> spawnPet(at,EntityType.PARROT,"Jade - Arara das Joias","jade");
        case "gatuno" -> spawnPet(at,EntityType.CAT,"Gatuno","gatuno"); case "dorminhoco" -> spawnPet(at,EntityType.WOLF,"Dorminhoco","dorminhoco"); case "acucarina","açucarina" -> spawnPet(at,EntityType.ALLAY,"Açucarina","acucarina");
        case "reflexo" -> spawnPet(at,EntityType.RABBIT,"Reflexo","reflexo"); case "joao_ninguem","joão_ninguém" -> spawnPet(at,EntityType.WOLF,"João Ninguém","joao_ninguem");
        case "operarios" -> {for(String n:WORKERS)spawnWorker(at.clone().add(random.nextInt(7)-3,0,random.nextInt(7)-3),n,EntityType.VILLAGER,"worker");}
        default -> {return false;}}
        player.sendMessage(Component.text("Entidade invocada: "+raw,NamedTextColor.GREEN)); return true; }

    private LivingEntity create(Location loc,EntityType type,String name,double hp,String kind){Entity raw=loc.getWorld().spawnEntity(loc,type);if(!(raw instanceof LivingEntity mob)){raw.remove();return null;}mob.customName(Component.text(name,NamedTextColor.LIGHT_PURPLE));mob.setCustomNameVisible(true);mob.getPersistentDataContainer().set(kindKey,PersistentDataType.STRING,kind);Attribute a=mob.getAttribute(Attribute.MAX_HEALTH);if(a!=null){a.setBaseValue(Math.min(hp,1024));mob.setHealth(a.getValue());}return mob;}
    private void spawnFran(Location loc){LivingEntity e=create(loc,EntityType.RAVAGER,"Fran - Hospedeiro do Apocalipse",16000,"fran_base");if(e!=null){e.getPersistentDataContainer().set(shieldKey,PersistentDataType.INTEGER,3);spawnWorker(loc.clone().add(2,0,2),"Nox - Dragão do Abismo",EntityType.PHANTOM,"nox");}}
    private void spawnHost(Location loc,EntityType type,String name,double hp,String kind){LivingEntity e=create(loc,type,name,hp,kind);if(e!=null&&kind.equals("denise"))for(int i=0;i<2;i++)create(loc.clone().add(i==0?2:-2,1,0),EntityType.BLAZE,"Chama Carmesim",40,"denise_flame");}
    private void spawnHunter(Location loc,String kind){EntityType type=switch(kind){case "valka"->EntityType.VINDICATOR;case "varkhen"->EntityType.PILLAGER;case "azra"->EntityType.WITCH;case "hacker"->EntityType.ENDERMAN;default->EntityType.WITHER_SKELETON;};String name=switch(kind){case "valka"->"Valka - Operária do Sangue";case "varkhen"->"Varkhen - Tecno-Demônio";case "azra"->"Azra - Operária dos Demônios";case "hacker"->"Hacker - Operário dos Sistemas";default->"Luciano - Sombra de Lúcifer";};LivingEntity e=create(loc,type,name,180,kind);if(e!=null)e.setGlowing(true);}
    private void spawnWorker(Location l,String n,EntityType t,String k){LivingEntity e=create(l,t,n,120,k);if(e!=null){e.setPersistent(true);if(k.equals("litoral"))e.setInvulnerable(true);}}
    private void spawnPet(Location l,EntityType t,String n,String k){LivingEntity e=create(l,t,n,80,"pet_"+k);if(e!=null)e.setPersistent(true);}
    private String kind(Entity e){return e.getPersistentDataContainer().get(kindKey,PersistentDataType.STRING);}

    @EventHandler public void onDamage(EntityDamageByEntityEvent event){if(!(event.getEntity() instanceof LivingEntity target))return;String k=kind(target);if(k==null)return;
        if(k.startsWith("fran_")){if(event.getDamager() instanceof Projectile shot){event.setCancelled(true);int shield=target.getPersistentDataContainer().getOrDefault(shieldKey,PersistentDataType.INTEGER,0);if(shield>0){target.getPersistentDataContainer().set(shieldKey,PersistentDataType.INTEGER,shield-1);shot.setVelocity(shot.getVelocity().multiply(-1.4));target.getWorld().spawnParticle(Particle.CRIT,target.getLocation().add(0,2,0),20,1,1,1,.1);}return;}if(target.getPersistentDataContainer().getOrDefault(shieldKey,PersistentDataType.INTEGER,0)>0){event.setCancelled(true);return;}}
        if(event.getDamager() instanceof Player p){if(k.equals("valka")&&p.getHealth()<p.getAttribute(Attribute.MAX_HEALTH).getValue()/2)target.setVelocity(target.getVelocity().multiply(2));if(k.equals("denise")){p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,50,8));p.sendActionBar(Component.text("Corte temporal!",NamedTextColor.RED));}if(k.equals("azra")){p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,80,2));p.setFreezeTicks(Math.min(p.getMaxFreezeTicks(),100));}if(k.equals("hacker")){p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,100,0));p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,80,0));}if(k.equals("larissa")){p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,100,0));p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,100,1));}if(k.startsWith("pet_")){String owner=target.getPersistentDataContainer().get(ownerKey,PersistentDataType.STRING);if(owner!=null&&!owner.equals(p.getUniqueId().toString()))target.setTarget(p);}}
    }
    @EventHandler public void onDamageAny(EntityDamageEvent event){if(!(event.getEntity() instanceof LivingEntity e))return;String k=kind(e);if(k==null)return;if(k.equals("fran_base")&&e.getHealth()-event.getFinalDamage()<=0){event.setCancelled(true);Location l=e.getLocation();e.remove();Bukkit.getScheduler().runTask(plugin,()->{LivingEntity ancient=create(l,EntityType.RAVAGER,"Fran - Forma Ancestral",48000,"fran_ancestral");if(ancient!=null){ancient.getPersistentDataContainer().set(shieldKey,PersistentDataType.INTEGER,3);spawnWorker(l.clone().add(2,0,2),"Nox - Dragão do Abismo",EntityType.PHANTOM,"nox");}});}if(k.equals("denise_flame")&&e.getHealth()-event.getFinalDamage()<=0){for(Entity n:e.getNearbyEntities(12,12,12))if(n instanceof LivingEntity boss&&"denise".equals(kind(boss))){Attribute max=boss.getAttribute(Attribute.MAX_HEALTH);if(max!=null)boss.setHealth(Math.min(max.getValue(),boss.getHealth()+max.getValue()*.15));}}}
    @EventHandler public void onDeath(EntityDeathEvent event){if("litoral".equals(kind(event.getEntity())))return;event.getDrops().clear();}
    @EventHandler public void onTame(PlayerInteractEntityEvent event){if(!(event.getRightClicked() instanceof LivingEntity pet))return;String k=kind(pet);if(k==null||!k.startsWith("pet_"))return;Player p=event.getPlayer();Material required=k.equals("pet_jade")?Material.DIAMOND:Material.SWEET_BERRIES;ItemStack hand=p.getInventory().getItem(event.getHand());if(hand.getType()!=required&&!(k.equals("pet_jade")&&hand.getType()==Material.EMERALD))return;if(hand.getAmount()>1)hand.setAmount(hand.getAmount()-1);else p.getInventory().setItem(event.getHand(),new ItemStack(Material.AIR));pet.getPersistentDataContainer().set(ownerKey,PersistentDataType.STRING,p.getUniqueId().toString());pet.setGlowing(true);event.setCancelled(true);p.sendMessage(Component.text("Pet vinculado!",NamedTextColor.GREEN));}
    @EventHandler public void onMove(PlayerMoveEvent event){if(event.getTo()==null||event.getFrom().getBlockX()==event.getTo().getBlockX()&&event.getFrom().getBlockZ()==event.getTo().getBlockZ())return;Player p=event.getPlayer();for(Entity entity:p.getNearbyEntities(3,2,3))if(entity instanceof LivingEntity mob){String k=kind(mob);if("dust_child".equals(k)){p.getWorld().spawnParticle(Particle.ASH,mob.getLocation(),8,.3,.2,.3,0);if(p.getLocation().distanceSquared(mob.getLocation())<2)for(int x=-1;x<=1;x++)for(int z=-1;z<=1;z++){Location b=mob.getLocation().getBlock().getRelative(x,-1,z).getLocation();if(b.getBlock().getType()==Material.DIRT||b.getBlock().getType()==Material.STONE)b.getBlock().setType(Material.SAND);}}if("idris".equals(k)&&random.nextInt(12)==0){Location f=mob.getLocation().add(random.nextInt(3)-1,0,random.nextInt(3)-1);if(f.getBlock().isAir()&&f.clone().subtract(0,1,0).getBlock().getType().isSolid())f.getBlock().setType(Material.FIRE);}}}
    private void tick(){long now=System.currentTimeMillis();for(World w:Bukkit.getWorlds()){if(now>=nightCheck&&w.getTime()>13000&&w.getTime()<23000&&random.nextInt(90)==0&&!w.getPlayers().isEmpty()){Player p=w.getPlayers().get(random.nextInt(w.getPlayers().size()));Location l=p.getLocation().add(random.nextInt(15)-7,0,random.nextInt(15)-7);if(l.getBlock().isPassable())spawnHunter(l,HUNTERS[random.nextInt(HUNTERS.length)]);}}
        nightCheck=now+1200;for(World w:Bukkit.getWorlds())for(Entity e:w.getEntities())if(e instanceof LivingEntity mob){String k=kind(mob);if(k==null)continue;if(k.equals("denise")){w.spawnParticle(Particle.FLAME,mob.getLocation().add(0,1.5,0),7,.7,.6,.7,.02);for(Entity n:mob.getNearbyEntities(4,3,4))if(n instanceof Player p&&random.nextInt(4)==0)p.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,30,3));}if(k.equals("hacker")||k.equals("luciano"))for(Entity n:mob.getNearbyEntities(5,3,5))if(n instanceof Player p&&random.nextInt(12)==0){p.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,60,0));if(k.equals("hacker"))p.addPotionEffect(new PotionEffect(PotionEffectType.NAUSEA,100,0));else p.damage(5,mob);}if(k.equals("litoral"))for(Entity n:mob.getNearbyEntities(10,8,10))if(n instanceof ExperienceOrb orb){w.spawnParticle(Particle.SOUL,orb.getLocation(),5,.2,.2,.2,0);orb.remove();}if(k.equals("pet_jade"))for(Entity n:mob.getNearbyEntities(10,5,10))if(n instanceof Player p&&p.getUniqueId().toString().equals(mob.getPersistentDataContainer().get(ownerKey,PersistentDataType.STRING)))p.spawnParticle(Particle.HAPPY_VILLAGER,p.getLocation().add(0,1,0),4,1,.5,1,0);}}
}
