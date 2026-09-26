package com.ghostchu.quickshop.addon.portable;

import com.ghostchu.quickshop.api.event.management.ShopDeleteEvent;
import com.ghostchu.quickshop.compatibility.CompatibilityModule;
import org.bukkit.NamespacedKey;

public final class Main extends CompatibilityModule {

  public static final NamespacedKey KEY = new NamespacedKey("quickshop", "portable-shop");

  private boolean requireSilkTouch = true;
  private boolean forceOwner = false;

  @Override
  public void init() {

    requireSilkTouch = getConfig().getBoolean("silk-touch");
    forceOwner = getConfig().getBoolean("force-owner");
  }

  public void onDelete(final ShopDeleteEvent event) {

  }
}