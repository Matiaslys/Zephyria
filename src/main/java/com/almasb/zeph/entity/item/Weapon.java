package com.almasb.zeph.entity.item;

import com.almasb.fxgl.entity.Entity;
import com.almasb.zeph.R;
import com.almasb.zeph.combat.Element;
import com.almasb.zeph.combat.Rune;
import com.almasb.zeph.combat.Stat;
import com.almasb.zeph.entity.character.Player;

public class Weapon extends EquippableItem {

    private static final long serialVersionUID = 2639185454495196264L;

    public enum WeaponType {
        ONE_H_SWORD(2, 0.85f), ONE_H_AXE(2, 0.95f), DAGGER(1, 1.25f), SPEAR(3, 0.85f), MACE(2, 1.0f), ROD(5, 0.9f), SHIELD(0, 0.9f),   // 1H, shield only left-hand
        TWO_H_SWORD(2, 0.7f), TWO_H_AXE(2, 0.65f), KATAR(1, 0.85f), BOW(5, 0.75f);    // 2H

        public final int range;
        public final float aspdFactor;

        private WeaponType(int range, float aspdFactor) {
            this.range = range;
            this.aspdFactor = aspdFactor;
        }
    }

    public final WeaponType type;
    public final int range;
    public final int pureDamage;

    public Weapon(int id, String name, String description, String textureName,
            ItemLevel level, Element element, WeaponType type, int damage, Rune... defRunes) {
        super(id, name, description, textureName, level, element, defRunes);
        this.type = type;
        this.range = type.range;
        this.pureDamage = damage;
    }

    public Weapon(Weapon copy) {
        this(copy.getID(), copy.getName(), copy.getDescription(), copy.getTextureName(),
                copy.level, copy.element, copy.type, copy.pureDamage, copy.defaultRunes.toArray(new Rune[0]));
    }

    @Override
    public void onEquip(Player ch) {
        super.onEquip(ch);
        ch.addBonusStat(Stat.ATK, getDamage());
    }

    @Override
    public void onUnEquip(Player ch) {
        super.onUnEquip(ch);
        ch.addBonusStat(Stat.ATK, -getDamage());
    }

    public int getDamage() {
        return pureDamage + refineLevel * (refineLevel > 2 ? level.bonus + 5 : level.bonus);
    }

    @Override
    public String getFullDescription() {
        StringBuilder sb = new StringBuilder(getName());
        sb.append("\n").append(getDescription());
        sb.append("\n\n");
        sb.append("Damage: ").append(getDamage()).append("\n");
        sb.append("Type: ").append(type).append("\n");
        for (Rune rune : defaultRunes) {
            sb.append(rune).append("\n");
        }
        for (Rune rune : runes) {
            sb.append(rune).append("\n");
        }
        return sb.toString();
    }

    @Override
    public Entity toEntity() {
        Entity e = Entity.noType();
        e.setSceneView(R.assets.getTexture("items/weapons/" + getTextureName()));
        e.addComponent(new WeaponComponent(this));
        return e;
    }

    public static class WeaponBuilder {
        private int id;
        private String name;
        private String desc;
        private String texture;
        private ItemLevel level = ItemLevel.NORMAL;
        private Element element = Element.NEUTRAL;
        private WeaponType type;
        private int damage;
        private Rune[] runes = new Rune[0];

        public WeaponBuilder id(int id) {
            this.id = id;
            return this;
        }

        public WeaponBuilder name(String name) {
            this.name = name;
            return this;
        }

        public WeaponBuilder description(String description) {
            this.desc = description;
            return this;
        }

        public WeaponBuilder textureName(String textureName) {
            this.texture = textureName;
            return this;
        }

        public WeaponBuilder itemLevel(ItemLevel level) {
            this.level = level;
            return this;
        }

        public WeaponBuilder element(Element element) {
            this.element = element;
            return this;
        }

        public WeaponBuilder type(WeaponType type) {
            this.type = type;
            return this;
        }

        public WeaponBuilder damage(int damage) {
            this.damage = damage;
            return this;
        }

        public WeaponBuilder runes(Rune... runes) {
            this.runes = runes;
            return this;
        }

        public Weapon build() {
            return new Weapon(id, name, desc, texture, level, element, type, damage, runes);
        }
    }
}