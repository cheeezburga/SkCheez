package me.cheezburga.skcheez.api.modifiers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import me.cheezburga.skcheez.api.wrapper.AttributeModifierWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class ModifierUtils {
    public static Multimap<Attribute, AttributeModifier> convertWrappers(AttributeModifierWrapper[] wrappers) {
        Multimap<Attribute, AttributeModifier> map = HashMultimap.create();
        for (AttributeModifierWrapper wrapper : wrappers) {
            map.put(wrapper.getAttribute(), wrapper.getModifier());
        }
        return map;
    }

    public static @Nullable AttributeModifier getConflictingModifier(ItemMeta meta, Attribute attribute, AttributeModifier modifier) {
        // this initially returned a wrapper, not just the modifier
        // if there can ever be a conflicting modifier NOT of the same attribute, change back
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
}
