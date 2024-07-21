package com.venned.simpleskywars.impl;

import com.venned.simpleskywars.utils.AbstractTier;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.List;

public class TierItemImpl extends AbstractTier {

    public TierItemImpl(Material material, int amount, List<Enchantment> enchantments, int chance, int enchant_level) {
        super(material, amount, enchantments, chance, enchant_level);
    }
}
