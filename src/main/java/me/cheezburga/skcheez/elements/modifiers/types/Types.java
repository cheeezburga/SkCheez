package me.cheezburga.skcheez.elements.modifiers.types;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.util.coll.CollectionUtils;
import me.cheezburga.skcheez.api.wrapper.AttributeModifierWrapper;
import me.cheezburga.skcheez.api.wrapper.EnumWrapper;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Types {

    public static boolean HAS_ATTRIBUTE_MODIFIERS = Skript.classExists("org.bukkit.attribute.AttributeModifier");

    static {
        if (HAS_ATTRIBUTE_MODIFIERS) {
            Classes.registerClass(new ClassInfo<>(AttributeModifierWrapper.class, "attributemodifier")
                    .user("attribute ?modifiers?")
                    .name("Attribute Modifier")
                    .description("Represents an attribute modifier, which can be applied to items.",
                            "Due to the complexity of these, they cannot be stored in variables long term.")
                    .examples("set {_m} to attribute modifier named \"example\" with amount 4 with operation add number")
                    .since("1.0.0")
                    .parser(new Parser<AttributeModifierWrapper>() {
                        @Override
                        public @NotNull String toString(@NotNull AttributeModifierWrapper o, int flags) {
                            return o.toString();
                        }

                        @Override
                        public boolean canParse(@NotNull ParseContext context) {
                            return false;
                        }

                        @Override
                        public @NotNull String toVariableNameString(@NotNull AttributeModifierWrapper o) {
                            return o.toString();
                        }
                    }));

            if (Classes.getExactClassInfo(AttributeModifier.Operation.class) == null) {
                EnumWrapper<AttributeModifier.Operation> ATTRIBUTE_MODIFIER_OPERATION_ENUM = new EnumWrapper<>(AttributeModifier.Operation.class, null, null);
                Classes.registerClass(ATTRIBUTE_MODIFIER_OPERATION_ENUM.getClassInfo("attributemodifieroperation")
                        .user("attribute ?modifier ?operations?")
                        .name("Attribute Modifier Operation")
                        .description("Enum class representing the operations an attribute modifier can use.")
                        .since("1.0.0"));
            }
        }

        if (Classes.getExactClassInfo(EquipmentSlot.class) == null) {
            EnumWrapper<EquipmentSlot> EQUIPMENT_SLOT_ENUM = new EnumWrapper<>(EquipmentSlot.class, null, "slot");
            Classes.registerClass(EQUIPMENT_SLOT_ENUM.getClassInfo("equipmentslot")
                    .user("equipment ?slots?")
                    .name("Equipment Slot")
                    .description("Enum class representing the equipment slots.")
                    .since("1.0.0"));
        }
    }
}
