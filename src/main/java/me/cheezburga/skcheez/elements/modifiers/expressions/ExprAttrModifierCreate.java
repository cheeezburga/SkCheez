package me.cheezburga.skcheez.elements.modifiers.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.cheezburga.skcheez.api.wrapper.AttributeModifierWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.event.Event;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ExprAttrModifierCreate extends SimpleExpression<AttributeModifierWrapper> {

    static {
        Skript.registerExpression(ExprAttrModifierCreate.class, AttributeModifierWrapper.class, ExpressionType.COMBINED,
                "[new] %attributetype% [attribute] modifier (named|with name|with id) %string% with amount %number% with operation %attributemodifieroperation% " +
                "[with uuid %-string%] [for [equipment] slot %-equipmentslot%]");
    }

    private Expression<Attribute> type;
    private Expression<String> name;
    private Expression<Number> amount;
    private Expression<AttributeModifier.Operation> operation;
    private Expression<String> uuid;
    private Expression<EquipmentSlot> slot;

    @SuppressWarnings({"NullableProblems", "unchecked"})
    @Override
    public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
        this.type = (Expression<Attribute>) exprs[0];
        this.name = (Expression<String>) exprs[1];
        this.amount = (Expression<Number>) exprs[2];
        this.operation = (Expression<AttributeModifier.Operation>) exprs[3];
        this.uuid = (Expression<String>) exprs[4];
        this.slot = (Expression<EquipmentSlot>) exprs[5];
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected @Nullable AttributeModifierWrapper[] get(Event event) {
        if (this.type == null || this.name == null || this.amount == null || this.operation == null) return null;

        Attribute type = this.type.getSingle(event);
        String name = this.name.getSingle(event);
        Number amount = this.amount.getSingle(event);
        AttributeModifier.Operation operation = this.operation.getSingle(event);
        if (type == null || name == null || amount == null || operation == null) return null;

        AttributeModifier toWrap;

        UUID uuid = UUID.randomUUID();
        if (this.uuid != null) {
            String u = this.uuid.getSingle(event);
            if (u != null) { uuid = UUID.fromString(u); }
        }
        EquipmentSlot slot = null;
        if (this.slot != null) {
            EquipmentSlot s = this.slot.getSingle(event);
            if (s != null) { slot = s; }
        }

        if (slot == null) {
            toWrap = new AttributeModifier(uuid, name, amount.doubleValue(), operation);
        } else {
            toWrap = new AttributeModifier(uuid, name, amount.doubleValue(), operation, slot);
        }
        return new AttributeModifierWrapper[]{new AttributeModifierWrapper(type, toWrap)};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public @NotNull Class<? extends AttributeModifierWrapper> getReturnType() {
        return AttributeModifierWrapper.class;
    }

    @Override
    public @NotNull String toString(@Nullable Event event, boolean b) {
        String name = "attribute modifier with name " + this.name.toString(event, b);
        String amount = " with amount " + this.amount.toString(event, b);
        String operation = " with operation " + this.operation.toString(event, b);
        String uuid = this.uuid != null ? " with uuid " + this.uuid.toString(event, b) : "";
        String slot = this.slot != null ? " with slot " + this.slot.toString(event, b) : "";
        return name + amount + operation + uuid + slot;
    }
}
