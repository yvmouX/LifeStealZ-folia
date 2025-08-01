package com.zetaplugins.lifestealz.listeners.revivebeacon;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import com.zetaplugins.lifestealz.LifeStealZ;
import com.zetaplugins.lifestealz.util.MessageUtils;
import com.zetaplugins.lifestealz.util.customblocks.CustomBlock;
import com.zetaplugins.lifestealz.util.customitems.CustomItemManager;
import com.zetaplugins.lifestealz.util.customitems.CustomItemType;
import com.zetaplugins.lifestealz.util.customitems.customitemdata.CustomReviveBeaconItemData;

public final class ReviveBeaconPlaceListener implements Listener {
    private final LifeStealZ plugin;

    public ReviveBeaconPlaceListener(LifeStealZ plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        if (!block.getType().equals(Material.BEACON)) return;

        ItemStack itemInHand = event.getItemInHand();
        if (!CustomItemType.REVIVE_BEACON.is(itemInHand)) return;

        String customItemId = CustomItemManager.getCustomItemId(itemInHand);
        CustomReviveBeaconItemData itemData;

        try {
            itemData = new CustomReviveBeaconItemData(customItemId);
        } catch (IllegalArgumentException e) {
            return;
        }

        String world = block.getWorld().getName();
        if (!itemData.isAllowedInWorld(world)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(MessageUtils.getAndFormatMsg(
                    false,
                    "noItemUseInWorld",
                    "&cYou cannot use this item in this world!"
            ));
            return;
        }

        CustomBlock.REVIVE_BEACON.make(block, customItemId);

        plugin.getReviveBeaconEffectManager().startIdleEffects(
                block.getLocation(),
                itemData.shouldShowEnchantParticles(),
                itemData.getDecoyMaterial()
        );
    }
}
