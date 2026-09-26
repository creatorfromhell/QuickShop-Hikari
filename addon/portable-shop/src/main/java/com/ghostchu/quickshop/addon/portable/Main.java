package com.ghostchu.quickshop.addon.portable;

import com.ghostchu.quickshop.api.event.management.ShopDeleteBlockEvent;
import com.ghostchu.quickshop.compatibility.CompatibilityModule;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;

public final class Main extends CompatibilityModule {

  public static final NamespacedKey KEY = new NamespacedKey("quickshop", "portable-shop");

  private boolean requireSilkTouch = true;
  private boolean forceOwner = false;

  @Override
  public void init() {

    requireSilkTouch = getConfig().getBoolean("silk-touch");
    forceOwner = getConfig().getBoolean("force-owner");
  }


  @EventHandler(ignoreCancelled = true)
  public void onDelete(final ShopDeleteBlockEvent event) {

    if (requireSilkTouch && !event.itemStack().containsEnchantment(Enchantment.SILK_TOUCH)) {
      return;
    }
  }
}