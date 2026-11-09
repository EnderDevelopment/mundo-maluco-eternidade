package com.enderdevelopment.plugin;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.persistence.PersistentDataType;
import java.util.Objects;

public final class Main extends JavaPlugin implements Listener {
    NamespacedKey artifactKey;
    NamespacedKey blessingKey;
    NamespacedKey bossKey;
    private RealitySystems systems;
    private RaidSystems raids;

    @Override public void onEnable() {
        artifactKey = new NamespacedKey(this, "artifact");
        blessingKey = new NamespacedKey(this, "blessing");
        bossKey = new NamespacedKey(this, "eternity");
        systems = new RealitySystems(this);
        raids = new RaidSystems(this);
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(systems, this);
        Bukkit.getPluginManager().registerEvents(raids, this);
        Objects.requireNonNull(getCommand("eternidade")).setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) { sender.sendMessage("Players only."); return true; }
            systems.invokeEternity(player); return true;
        });
        Objects.requireNonNull(getCommand("mundomalco")).setTabCompleter((sender, command, alias, args) -> args.length == 1 ? java.util.List.of("fran", "denise", "rene", "valka", "varkhen", "azra", "hacker", "luciano", "destino_corrompido", "litoral", "larissa", "ze_do_po", "idris", "jade", "reparador", "gatuno", "dorminhoco", "acucarina", "reflexo", "joao_ninguem", "operarios").stream().filter(s -> s.startsWith(args[0].toLowerCase())).toList() : java.util.List.of());
        Objects.requireNonNull(getCommand("mundomalco")).setExecutor((sender, command, label, args) -> {
            if (!(sender instanceof Player player)) { sender.sendMessage("Players only."); return true; }
            if (args.length > 0 && raids.spawnNamed(player, args[0])) return true;
            systems.openBook(player); return true;
        });
    }

    @Override public void onDisable() { if (systems != null) systems.shutdown(); if (raids != null) raids.shutdown(); }

    @EventHandler public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.DARKNESS, 100, 0, false, true, true));
        player.spawnParticle(org.bukkit.Particle.END_ROD, player.getLocation().add(0, 1.5, 0), 36, 0.8, 0.6, 0.8, 0.02);
        player.sendMessage(net.kyori.adventure.text.Component.text("Cosmic eyes watch from beyond reality.", net.kyori.adventure.text.format.NamedTextColor.LIGHT_PURPLE));
        boolean hasBook = player.getInventory().containsAtLeast(systems.item("reality_book"), 1);
        if (!hasBook) player.getInventory().addItem(systems.item("reality_book"));
    }
}
