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

public class AttributeModifierWrapper {
    private Attribute attribute;
    private AttributeModifier modifier;

    public AttributeModifierWrapper(Attribute attribute) {
        this.attribute = attribute;
    }

    public AttributeModifierWrapper(AttributeModifier modifier) {
        this.modifier = modifier;
    }

    public AttributeModifierWrapper(Attribute attribute, AttributeModifier modifier) {
        this.attribute = attribute;
        this.modifier = modifier;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public AttributeModifier getModifier() {
        return modifier;
    }

    public void setAttribute(Attribute attribute) {
        this.attribute = attribute;
    }

    public void setModifier(AttributeModifier modifier) {
        this.modifier = modifier;
    }

    public void modifyName(String name) {
        modifier = new AttributeModifier(modifier.getUniqueId(), name, modifier.getAmount(), modifier.getOperation(), modifier.getSlot());
    }

    public void modifyAmount(double amount) {
        modifier = new AttributeModifier(modifier.getUniqueId(), modifier.getName(), amount, modifier.getOperation(), modifier.getSlot());
    }

    public void modifyOperation(Operation operation) {
        modifier = new AttributeModifier(modifier.getUniqueId(), modifier.getName(), modifier.getAmount(), operation, modifier.getSlot());
    }

    public void modifyUUID(UUID uuid) {
        modifier = new AttributeModifier(uuid, modifier.getName(), modifier.getAmount(), modifier.getOperation(), modifier.getSlot());
    }

    public void modifySlot(EquipmentSlot slot) {
        modifier = new AttributeModifier(modifier.getUniqueId(), modifier.getName(), modifier.getAmount(), modifier.getOperation(), slot);
    }

    public boolean isValid() {
        return attribute != null && modifier != null;
    }

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
