package me.cheezburga.skcheez.api.modifiers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.cheezburga.skcheez.api.wrapper.AttributeModifierWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Utility methods for {@link Attribute attributes}, {@link AttributeModifier modifiers}, and their {@link AttributeModifierWrapper wrappers}.
 */
public class ModifierUtils {

    /**
     * Converts an array of {@link AttributeModifierWrapper wrappers} to a {@link Multimap multimap} of attributes to modifiers.
     *
     * @param wrappers The wrappers to convert.
     * @return A Multimap containing attributes mapped to modifiers.
     */
    public static Multimap<Attribute, AttributeModifier> convertWrappers(AttributeModifierWrapper[] wrappers) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        for (AttributeModifierWrapper wrapper : wrappers) {
            map.put(wrapper.getAttribute(), wrapper.getModifier());
        }
        return map;
    }

    /**
     * Gets a conflicting modifier for the given {@link Attribute attribute} and {@link AttributeModifier modifier} from the given {@link ItemMeta ItemMeta}.
     *
     * @param meta The item meta to check for conflicting modifiers.
     * @param attribute The attribute to check with.
     * @param modifier The modifier to check with.
     * @return The conflicting modifier, or null if no conflict is found.
     */
    public static @Nullable AttributeModifier getConflictingModifier(ItemMeta meta, Attribute attribute, AttributeModifier modifier) {
        // this initially returned a wrapper, not just the modifier
        // if there can ever be a conflicting modifier NOT of the same attribute, change back and adjust logic in modifiers of item expression
        if (!meta.hasAttributeModifiers()) { return null; }
        else {
            Collection<AttributeModifier> modifiers = meta.getAttributeModifiers(attribute);
            if (modifiers != null) {
                // AttributeModifierWrapper conflict = new AttributeModifierWrapper(attribute);
                for (AttributeModifier m : modifiers) {
                    if (m.getUniqueId() == modifier.getUniqueId()) {
                        // conflict.setModifier(m);
                        // return conflict;
                        return m;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Gets the NBT string representation of an equipment slot.
     *
     * @param slot The equipment slot.
     * @return The NBT string representation of the slot.
     */
    public static String getSlotNBTString(EquipmentSlot slot) {
        return switch (slot) {
            case HAND -> "mainhand";
            case OFF_HAND -> "offhand";
            case HEAD, CHEST, LEGS, FEET -> slot.toString().toLowerCase();
        };
    }
}
