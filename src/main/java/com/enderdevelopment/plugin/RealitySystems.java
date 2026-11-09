package com.enderdevelopment.plugin;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import java.util.*;

final class RealitySystems implements Listener {
    private final Main plugin;
    private final Map<UUID, BossBar> bars = new HashMap<>();
    private final Map<UUID, Long> attacks = new HashMap<>();
    private final Random random = new Random();
    private final BukkitTask ticker;
    private static final String TITLE = "Cosmic Beings Encyclopedia";
    private static final String[] KINGS = {"Destino", "Sofia", "Chemite", "Apocalipse", "Samara"};

    RealitySystems(Main plugin) { this.plugin = plugin; ticker = Bukkit.getScheduler().runTaskTimer(plugin, this::tickBosses, 20, 20); }
    void shutdown() { ticker.cancel(); bars.values().forEach(BossBar::removeViewer); bars.clear(); }

    ItemStack item(String id) {
        Material material = switch (id) {
            case "reality_book", "destiny_book", "oracle" -> Material.ENCHANTED_BOOK;
            case "life_scepter" -> Material.BLAZE_ROD;
            case "paradox_sword" -> Material.NETHERITE_SWORD;
            case "chaos_scythe" -> Material.NETHERITE_HOE;
            default -> Material.PAPER;
        };
        ItemStack stack = new ItemStack(material); ItemMeta meta = stack.getItemMeta();
        String name = switch (id) { case "reality_book" -> "Book of Reality"; case "destiny_book" -> "Book of Destiny"; case "life_scepter" -> "Scepter of Life"; case "paradox_sword" -> "Sword of Paradox"; case "chaos_scythe" -> "Scythe of Chaos"; case "oracle" -> "Oracle of Wisdom"; default -> id; };
        meta.displayName(Component.text(name, NamedTextColor.LIGHT_PURPLE)); meta.getPersistentDataContainer().set(plugin.artifactKey, PersistentDataType.STRING, id); stack.setItemMeta(meta); return stack;
    }

    void openBook(Player player) {
        Inventory menu = Bukkit.createInventory(null, 9, Component.text(TITLE, NamedTextColor.DARK_PURPLE));
        for (int i = 0; i < KINGS.length; i++) { ItemStack icon = new ItemStack(Material.NETHER_STAR); ItemMeta meta = icon.getItemMeta(); meta.displayName(Component.text("Blessing of " + KINGS[i], NamedTextColor.GOLD)); meta.getPersistentDataContainer().set(plugin.blessingKey, PersistentDataType.STRING, KINGS[i]); icon.setItemMeta(meta); menu.setItem(i + 2, icon); }
        player.openInventory(menu);
    }

    @EventHandler public void onUse(PlayerInteractEvent event) {
        ItemStack stack = event.getItem(); if (stack == null || !stack.hasItemMeta()) return;
        String id = stack.getItemMeta().getPersistentDataContainer().get(plugin.artifactKey, PersistentDataType.STRING); if (id == null) return;
        event.setCancelled(true); Player player = event.getPlayer();
        if (id.equals("reality_book")) openBook(player);
        if (id.equals("life_scepter")) { player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 160, 2)); player.getWorld().spawnParticle(Particle.ROOTS, player.getLocation(), 30, 1, 0, 1); }
        if (id.equals("oracle")) reveal(player);
        if (id.equals("chaos_scythe")) { for (Player target : nearby(player, 8)) target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 80, 1)); player.getWorld().spawnParticle(Particle.SQUID_INK, player.getLocation(), 40, 3, 1, 3); }
    }

    @EventHandler public void onMenu(InventoryClickEvent event) {
        if (!event.getView().title().equals(Component.text(TITLE, NamedTextColor.DARK_PURPLE))) return; event.setCancelled(true); if (!(event.getWhoClicked() instanceof Player player)) return;
        ItemStack clicked = event.getCurrentItem(); if (clicked == null || !clicked.hasItemMeta()) return; String blessing = clicked.getItemMeta().getPersistentDataContainer().get(plugin.blessingKey, PersistentDataType.STRING); if (blessing == null) return;
        if (player.getPersistentDataContainer().has(plugin.blessingKey, PersistentDataType.STRING)) { player.sendMessage(Component.text("Your first blessing is already chosen.", NamedTextColor.RED)); return; }
        player.getPersistentDataContainer().set(plugin.blessingKey, PersistentDataType.STRING, blessing); player.closeInventory(); player.sendMessage(Component.text("The King " + blessing + " has chosen you.", NamedTextColor.GOLD)); player.addPotionEffect(new PotionEffect(PotionEffectType.RESISTANCE, 20 * 60 * 60, 0));
    }

    @EventHandler public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player player) || random.nextDouble() > 0.03) return;
        String id = switch (random.nextInt(5)) { case 0 -> "destiny_book"; case 1 -> "life_scepter"; case 2 -> "paradox_sword"; case 3 -> "chaos_scythe"; default -> "oracle"; };
        player.getWorld().dropItemNaturally(player.getLocation(), item(id)); player.sendMessage(Component.text("A divine weapon fell from the cosmic fracture.", NamedTextColor.LIGHT_PURPLE));
    }

    @EventHandler public void onBossDamage(EntityDamageEvent event) { if (event.getEntity().getPersistentDataContainer().has(plugin.bossKey, PersistentDataType.BYTE) && event.getCause() == EntityDamageEvent.DamageCause.FALL) event.setCancelled(true); }

    void invokeEternity(Player player) {
        Location altar = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation(); if (!altar.getBlock().getType().isSolid()) { player.sendMessage(Component.text("Stand on a solid altar block.", NamedTextColor.RED)); return; }
        String[] weapons = {"destiny_book", "life_scepter", "paradox_sword", "chaos_scythe", "oracle"}; for (String weapon : weapons) if (!player.getInventory().containsAtLeast(item(weapon), 1)) { player.sendMessage(Component.text("The altar requires all five divine weapons.", NamedTextColor.RED)); return; }
        for (String weapon : weapons) player.getInventory().removeItem(item(weapon)); WitherSkeleton boss = player.getWorld().spawn(player.getLocation().add(0, 2, 0), WitherSkeleton.class); boss.customName(Component.text("A Eternidade", NamedTextColor.LIGHT_PURPLE)); boss.setCustomNameVisible(true); boss.getPersistentDataContainer().set(plugin.bossKey, PersistentDataType.BYTE, (byte) 1); boss.getAttribute(Attribute.MAX_HEALTH).setBaseValue(690000); boss.setHealth(690000); boss.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.35);
        BossBar bar = BossBar.bossBar(Component.text("A Eternidade", NamedTextColor.LIGHT_PURPLE), 1f, BossBar.Color.PURPLE, BossBar.Overlay.PROGRESS); Bukkit.getOnlinePlayers().forEach(bar::addViewer); bars.put(boss.getUniqueId(), bar); attacks.put(boss.getUniqueId(), System.currentTimeMillis() + 15000); player.sendMessage(Component.text("A Eternidade has awakened. Dodge every cosmic attack.", NamedTextColor.GOLD));
    }

    private void tickBosses() { for (Entity entity : Bukkit.getWorlds().stream().flatMap(w -> w.getEntities().stream()).toList()) { if (!(entity instanceof LivingEntity boss) || !boss.getPersistentDataContainer().has(plugin.bossKey, PersistentDataType.BYTE)) continue; BossBar bar = bars.get(boss.getUniqueId()); if (bar != null) bar.progress((float) Math.max(0, boss.getHealth() / 690000.0)); if (System.currentTimeMillis() < attacks.getOrDefault(boss.getUniqueId(), 0L)) continue; attacks.put(boss.getUniqueId(), System.currentTimeMillis() + 15000); for (Player player : nearby(boss, 24)) { player.damage(999999, boss); player.getWorld().strikeLightningEffect(player.getLocation()); } } }
    private List<Player> nearby(Entity center, double radius) { return center.getWorld().getPlayers().stream().filter(p -> p.getLocation().distanceSquared(center.getLocation()) <= radius * radius).toList(); }
    private void reveal(Player player) { for (LivingEntity target : player.getWorld().getLivingEntities()) if (target != player && target.getLocation().distanceSquared(player.getLocation()) < 400) player.sendActionBar(Component.text(target.getName() + " HP " + Math.round(target.getHealth()) + "/" + Math.round(target.getAttribute(Attribute.MAX_HEALTH).getValue()), NamedTextColor.YELLOW)); }
}
