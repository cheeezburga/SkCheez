package me.cheezburga.skcheez.api.util;

import java.util.UUID;

public class Utils {

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
}
