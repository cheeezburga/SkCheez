package me.cheezburga.skcheez;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import me.cheezburga.skcheez.api.util.UpdateChecker;
import me.cheezburga.skcheez.api.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SkCheez extends JavaPlugin {

    private static SkCheez instance;
    private SkriptAddon addon;
    private PluginManager manager;

    @SuppressWarnings("unused")
    public static SkCheez getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        manager = Bukkit.getPluginManager();

        try {
            addon = Skript.registerAddon(this)
                    .loadClasses("me.cheezburga.skcheez", "elements")
                    .setLanguageFileDirectory("lang");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SkCheez!", e);
        }

        loadMetrics();

        String version = getDescription().getVersion();
        checkUpdate(version);

        Utils.log("&aEnabled v%s&7 in &b%.2f seconds", version, (float) (System.currentTimeMillis() - start) / 1000);
    }

    private void checkUpdate(String version) {
        UpdateChecker.checkForUpdate(version);
    }

    private void loadMetrics() {
        Metrics metrics = new Metrics(this, 21303);
        metrics.addCustomChart(new Metrics.SimplePie("skriptVersion", () -> Skript.getVersion().toString()));
    }

    @SuppressWarnings("unused")
    public void registerListener(Listener listener) {
        manager.registerEvents(listener, instance);
    }

    @SuppressWarnings("unused")
    public SkriptAddon getAddonInstance() {
        return addon;
    }
}
