package me.cheezburga.skcheez;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class SkCheez extends JavaPlugin {

    private static SkCheez instance;
    private SkriptAddon addon;

    @Override
    public void onEnable() {
        if (!Skript.isRunningMinecraft(1,17)) {
            getPluginLoader().disablePlugin(this);
            getLogger().info("SkCheez only works on 1.17+");
            return;
        }
        instance = this;
        loadMetrics();
        try {
            this.addon = Skript.registerAddon(this)
                    .loadClasses("me.cheezburga.skcheez", "elements")
                    .setLanguageFileDirectory("lang");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load SkCheez.", e);
        }
    }

    private void loadMetrics() {
        Metrics metrics = new Metrics(this, 21303);
        metrics.addCustomChart(new Metrics.SimplePie("skriptVersion", () -> Skript.getVersion().toString()));
    }

    public static SkCheez getInstance() {
        return instance;
    }

    public SkriptAddon getAddonInstance() {
        return addon;
    }
}
