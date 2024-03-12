package me.cheezburga.skcheez.api.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final String PREFIX = "&7[&cSk&6Cheez&7] ";
    private static final Pattern HEX_PATTERN = Pattern.compile("<#([A-Fa-f\\d]){6}>");
    private static final boolean SKRIPT_IS_THERE = Bukkit.getPluginManager().getPlugin("Skript") != null;

    /**
     * Converts a {@link UUID UUID} to an array of integers.
     *
     * @param uuid The UUID to convert.
     * @return An array of integers representing the UUID.
     */
    public static int[] uuidToIntArray(UUID uuid) {
        long most = uuid.getMostSignificantBits();
        long least = uuid.getLeastSignificantBits();
        return new int[]{
                (int) (most >> 32),
                (int) (most),
                (int) (least >> 32),
                (int) least
        };
    }

    /**
     * Converts an array of integers representing a {@link UUID uuid} to a string.
     *
     * @param array The array of integers representing the UUID.
     * @return A string representation of the UUID array.
     */
    public static String uuidIntArrayToString(int[] array) {
        return String.format("[I:%d,%d,%d,%d]", array[0], array[1], array[2], array[3]);
    }

    /**
     * Converts a string representation of a UUID array to an array of integers.
     *
     * @param uuid The string representation of the UUID array.
     * @return An array of integers representing the UUID.
     */
    public static int[] uuidStringToIntArray(String uuid) {
        String[] components = uuid.replace("[I;", "").replace("]", "").split(",");
        return new int[]{
                Integer.parseInt(components[0]),
                Integer.parseInt(components[1]),
                Integer.parseInt(components[2]),
                Integer.parseInt(components[3])
        };
    }

    /**
     * Converts an array of integers representing a {@link UUID UUID} back to a UUID.
     *
     * @param array The array of integers representing the UUID.
     * @return The UUID.
     */
    public static UUID uuidIntArrayToUUID(int[] array) {
        long most = (((long) array[0]) << 32) | (array[1] & 0xFFFFFFFFL);
        long least = (((long) array[2]) << 32) | (array[3] & 0xFFFFFFFFL);
        return new UUID(most, least);
    }

    /**
     * Method to get a coloured string from a string.
     * <p>
     *     This method is copied from SkBee.
     *     <a href="https://github.com/ShaneBeee/SkBee/blob/f6f85e3d9d9da0cd772b58e59fc288f7d1ec21f8/src/main/java/com/shanebeestudios/skbee/api/util/Util.java#L35">getColString(string)</a>
     * </p>
     *
     * @author ShaneBee
     *
     * @param string The string to convert to it's coloured version.
     * @return The coloured string.
     */
    @SuppressWarnings("deprecation") // Paper deprecation
    public static String getColouredString(String string) {
        Matcher matcher = HEX_PATTERN.matcher(string);
        if (SKRIPT_IS_THERE) {
            while (matcher.find()) {
                final ChatColor hexColor = ChatColor.of(matcher.group().substring(1, matcher.group().length() - 1));
                final String before = string.substring(0, matcher.start());
                final String after = string.substring(matcher.end());
                string = before + hexColor + after;
                matcher = HEX_PATTERN.matcher(string);
            }
        } else {
            string = HEX_PATTERN.matcher(string).replaceAll("");
        }
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    /**
     * Method to send a coloured message to a given receiver.
     * <p>
     *     This method is copied from SkBee.
     *     <a href="https://github.com/ShaneBeee/SkBee/blob/f6f85e3d9d9da0cd772b58e59fc288f7d1ec21f8/src/main/java/com/shanebeestudios/skbee/api/util/Util.java#L51">sendColMsg(receiver, format, objects)</a>
     * </p>
     *
     * @author ShaneBee
     *
     * @param receiver The CommandSender to receive the message.
     * @param format The format for the message.
     * @param objects The arguments for the message.
     */
    public static void sendColouredMessage(CommandSender receiver, String format, Object... objects) {
        receiver.sendMessage(getColouredString(String.format(format, objects)));
    }

    /**
     * Method to log a coloured message to the console.
     * <p>
     *     This method is copied from SkBee.
     *     <a href="https://github.com/ShaneBeee/SkBee/blob/f6f85e3d9d9da0cd772b58e59fc288f7d1ec21f8/src/main/java/com/shanebeestudios/skbee/api/util/Util.java#L55">log(format, objects)</a>
     * </p>
     *
     * @author ShaneBee
     *
     * @param format The format for the log.
     * @param objects The arguments for the log.
     */
    public static void log(String format, Object... objects) {
        String log = String.format(format, objects);
        Bukkit.getConsoleSender().sendMessage(getColouredString(PREFIX + log));
    }
}
