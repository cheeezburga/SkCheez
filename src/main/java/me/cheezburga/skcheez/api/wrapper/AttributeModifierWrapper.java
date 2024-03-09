package me.cheezburga.skcheez.api.wrapper;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.inventory.EquipmentSlot;

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

    @SuppressWarnings("ConstantValue")
    public String toString() {
        String attribute = getAttribute() != null ? getAttribute().toString().toLowerCase().replace("_", " ") : "<none>";
        String name = getModifier().getName() != null ? "\"" + getModifier().getName() + "\"" : "<none>";
        String amount = getModifier().getAmount() != 0 ? String.valueOf(getModifier().getAmount()) : "<none>";
        String operation = getModifier().getOperation() != null ? getModifier().getOperation().toString().toLowerCase().replace("_", "") : "<none>";
        String slot = getModifier().getSlot() != null ? getModifier().getSlot().toString().toLowerCase() : "<none>";
        String uuid = getModifier().getUniqueId() != null ? getModifier().getUniqueId().toString() : "<none>";

        return attribute + " modifier with name " + name + ", amount " + amount + ", operation " + operation + ", slot " + slot + ", and uuid " + uuid;
    }
}
