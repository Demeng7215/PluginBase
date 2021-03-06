package dev.demeng.pluginbase;

import com.cryptomorin.xseries.XMaterial;
import dev.demeng.pluginbase.chat.ChatUtils;
import dev.demeng.pluginbase.plugin.BaseLoader;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** Commonly used methods and utilities. */
public class Common {

  /**
   * If the server software the plugin is running on is Spigot or a fork of Spigot. Used internally
   * for some Spigot-only features or optimizations.
   */
  public static final boolean SPIGOT = Validate.checkClass("net.md_5.bungee.api.ChatColor") != null;

  // -----------------------------------------------------------------------------------------------------
  // PLUGIN INFORMATION
  // -----------------------------------------------------------------------------------------------------

  /**
   * Gets the name of the plugin, as defined in plugin.yml.
   *
   * @return The name of the plugin
   */
  @NotNull
  public static String getName() {
    return BaseLoader.getPlugin().getDescription().getName();
  }

  /**
   * Gets the version string, as defined in plugin.yml.
   *
   * @return The version of the plugin
   */
  @NotNull
  public static String getVersion() {
    return BaseLoader.getPlugin().getDescription().getVersion();
  }

  // -----------------------------------------------------------------------------------------------------
  // SERVER INFORMATION
  // -----------------------------------------------------------------------------------------------------

  /**
   * Gets the server's major Minecraft version.
   *
   * <p>For example, if a server is running 1.16.4, this will return 16.
   *
   * @return The server's major Minecraft version.
   */
  public static int getServerMajorVersion() {
    return XMaterial.getVersion();
  }

  /**
   * Checks if the server's major version is at least the specified version.
   *
   * @param version The minimum major version
   * @return True if equal or greater to the provided version, false otherwise
   */
  public static boolean isServerVersionAtLeast(int version) {
    return getServerMajorVersion() >= version;
  }

  // -----------------------------------------------------------------------------------------------------
  // MISC
  // -----------------------------------------------------------------------------------------------------

  /**
   * Returns the nullable value if not null, or the default value if it is null.
   *
   * @param nullable The nullable value
   * @param def The default value
   * @return The nullable if not null, default otherwise
   */
  @NotNull
  public static <T> T getOrDefault(T nullable, T def) {
    return nullable != null ? nullable : def;
  }

  /**
   * Reports an error in the plugin.
   *
   * @param error The exception
   * @param description A brief description of the error
   * @param disable If the plugin should be disabled
   * @param players Any players associated with this error, a message will be sent to them
   */
  public static void error(
      Throwable error, String description, boolean disable, Player... players) {

    if (error != null) {
      error.printStackTrace();
    }

    ChatUtils.coloredConsole(
        "&4" + ChatUtils.CONSOLE_LINE,
        "&cAn internal error has occured in " + Common.getName() + "!",
        "&cContact the plugin author if you cannot fix this error.",
        "&cDescription: &6" + description,
        "&4" + ChatUtils.CONSOLE_LINE);

    for (Player p : players) {
      ChatUtils.tellMessage(
          p,
          "&6An internal error has occurred in "
              + Common.getName()
              + ". Further details have been printed in console.");
    }

    if (disable && Bukkit.getPluginManager().isPluginEnabled(BaseLoader.getPlugin())) {
      Bukkit.getPluginManager().disablePlugin(BaseLoader.getPlugin());
    }
  }
}
