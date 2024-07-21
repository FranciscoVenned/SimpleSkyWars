package com.venned.simpleskywars.utils;

import com.venned.simpleskywars.face.TierItem;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public abstract class AbstractTier implements TierItem {
    private Material material;
    private int amount;
    private List<Enchantment> enchantments;
    private int chance;
    private int enchant_level;

    public AbstractTier(Material material, int amount, List<Enchantment> enchantments, int chance, int enchant_level) {
        this.material = material;
        this.amount = amount;
        this.enchantments = enchantments;
        this.chance = chance;
        this.enchant_level = enchant_level;

    }

    @Override
    public Material getMaterial() {
        return material;
    }

    @Override
    public int getEnchantmentLevel(Enchantment enchantment) {
        return enchant_level;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public List<Enchantment> getEnchantments() {
        return enchantments;
    }

    @Override
    public int getChance() {
        return chance;
    }
}
