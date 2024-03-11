package me.cheezburga.skcheez.api.wrapper;

import me.cheezburga.skcheez.api.modifiers.ModifierUtils;
import me.cheezburga.skcheez.api.util.Utils;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.Nullable;

import java.util.StringJoiner;
import java.util.UUID;

/**
 * A wrapper class for an {@link Attribute attribute} and a {@link AttributeModifier modifier}.
 */
public class AttributeModifierWrapper {

    private Attribute attribute;
    private AttributeModifier modifier;

    /**
     * Constructs a wrapper with the specified attribute and modifier.
     *
     * @param attribute The attribute to be wrapped.
     * @param modifier The modifier to be wrapped.
     */
    public AttributeModifierWrapper(Attribute attribute, AttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    /**
     * Gets the wrapped attribute.
     *
     * @return The wrapped attribute.
     */
    public Attribute getAttribute() {
        return attribute;
    }

    /**
     * Gets the wrapped modifier.
     *
     * @return The wrapped modifier.
     */
    public AttributeModifier getModifier() {
        return modifier;
    }

    /**
     * Sets the attribute of this wrapper.
     *
     * @param attribute The attribute to set.
     */
    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    /**
     * Sets the modifier of this wrapper.
     *
     * @param modifier The modifier to set.
     */
    public void setModifier(AttributeModifier modifier) {
        this.modifier = modifier;
    }

    /**
     * Modifies the name of the modifier.
     *
     * @param name The new name for the modifier.
     */
    public void modifyName(String name) {
        modifier = new AttributeModifier(modifier.getUniqueId(), name, modifier.getAmount(), modifier.getOperation(), modifier.getSlot());
    }

    /**
     * Modifies the amount of the modifier.
     *
     * @param amount The new amount for the modifier.
     */
    public void modifyAmount(double amount) {
        modifier = new AttributeModifier(modifier.getUniqueId(), modifier.getName(), amount, modifier.getOperation(), modifier.getSlot());
    }

    /**
     * Modifies the operation of the modifier.
     *
     * @param operation The new operation for the modifier.
     */
    public void modifyOperation(Operation operation) {
        modifier = new AttributeModifier(modifier.getUniqueId(), modifier.getName(), modifier.getAmount(), operation, modifier.getSlot());
    }

    /**
     * Modifies the UUID of the modifier.
     *
     * @param uuid The new UUID for the modifier.
     */
    public void modifyUUID(UUID uuid) {
        modifier = new AttributeModifier(uuid, modifier.getName(), modifier.getAmount(), modifier.getOperation(), modifier.getSlot());
    }

    /**
     * Modifies the slot of the modifier.
     *
     * @param slot The new equipment slot for the modifier.
     */
    public void modifySlot(EquipmentSlot slot) {
        modifier = new AttributeModifier(modifier.getUniqueId(), modifier.getName(), modifier.getAmount(), modifier.getOperation(), slot);
    }

    /**
     * Checks if this wrapper is valid (i.e. both the attribute and modifier are set).
     *
     * @return If both the attribute and modifier are not null then true, otherwise false.
     */
    public boolean isValid() {
        return attribute != null && modifier != null;
    }

    /**
     * Returns the NBT string representation of this wrapper.
     *
     * @return The NBT string representation, or null if the attribute or modifier is invalid.
     */
    public @Nullable String getNBTString() {
        if (!isValid()) { return null; }
        else {
            StringJoiner joiner = new StringJoiner(",");
            joiner.add("{AttributeName:\"" + attribute.getKey().asString().substring(10) + "\"");
            joiner.add("Name:\"" + modifier.getName() + "\"");
            joiner.add("Amount:" + modifier.getAmount());
            joiner.add("Operation:" + modifier.getOperation().ordinal());
            if (modifier.getSlot() != null) { joiner.add("Slot:" + ModifierUtils.getSlotNBTString(modifier.getSlot())); }
            joiner.add("UUID:" + Utils.uuidIntArrayToString(Utils.uuidToIntArray(modifier.getUniqueId())) + "}");

            return joiner.toString();
        }
    }

    /**
     * Returns a string representation of this wrapper.
     *
     * @return The string representation.
     */
    public String toString() {
        if (attribute != null && modifier != null) {
            String type = attribute.toString().toLowerCase().replace("_", " ");
            String name = "\"" + modifier.getName() + "\"";
            String amount = String.valueOf(modifier.getAmount());
            String operation = modifier.getOperation().toString().toLowerCase().replace("_", " ");
            String slot = modifier.getSlot() != null ? modifier.getSlot().toString().toLowerCase() : "any";
            String uuid = Utils.uuidIntArrayToString(Utils.uuidToIntArray(modifier.getUniqueId()));
            return String.format("%s attribute modifier with name %s, amount %s, operation %s, slot %s, and uuid %s",
                    type, name, amount, operation, slot, uuid);
        } else if (attribute != null) {
            return "empty " + attribute.toString().toLowerCase().replace("_", " ") + " attribute modifier";
        } else if (modifier != null) {
            String name = "\"" + modifier.getName() + "\"";
            String amount = String.valueOf(modifier.getAmount());
            String operation = modifier.getOperation().toString().toLowerCase().replace("_", " ");
            String slot = modifier.getSlot() != null ? modifier.getSlot().toString().toLowerCase() : "any";
            String uuid = modifier.getUniqueId().toString();
            return String.format("%s attribute modifier with name %s, amount %s, operation %s, slot %s, and uuid %s",
                    "unknown", name, amount, operation, slot, uuid);
        }
        return null;
    }
}
