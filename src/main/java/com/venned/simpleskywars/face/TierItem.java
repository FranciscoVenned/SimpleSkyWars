package com.venned.simpleskywars.face;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public interface TierItem {
    Material getMaterial();
    int getAmount();
    List<Enchantment> getEnchantments();
    int getChance();
    int getEnchantmentLevel(Enchantment enchantment);
}
