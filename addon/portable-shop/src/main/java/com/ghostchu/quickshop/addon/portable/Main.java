package com.ghostchu.quickshop.addon.portable;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.event.Phase;
import com.ghostchu.quickshop.api.event.management.ShopDeleteBlockEvent;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.compatibility.CompatibilityModule;
import com.google.gson.Gson;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.ghostchu.quickshop.api.QuickShopKeys.PDC_CHEST_SHOP_OWNER;

public final class Main extends CompatibilityModule {

  public static final NamespacedKey PORTABLE_KEY = new NamespacedKey(QuickShop.getInstance().getJavaPlugin(), "portable-shop");
  public static final NamespacedKey PORTABLE_VERSION = new NamespacedKey(QuickShop.getInstance().getJavaPlugin(), "portable-version");
  public static final NamespacedKey ID_KEY = new NamespacedKey(QuickShop.getInstance().getJavaPlugin(), "shop-id");
  public static final NamespacedKey TOKEN_KEY = new NamespacedKey(QuickShop.getInstance().getJavaPlugin(), "shop-token");
  public static final NamespacedKey CONTENTS_KEY = new NamespacedKey(QuickShop.getInstance().getJavaPlugin(), "shop-contents");

  private static final int CURRENT_VERSION = 1;

  private final Gson gson = new Gson();

  protected final ConcurrentHashMap<Long, UUID> portableShopTokens = new ConcurrentHashMap<>();

  private boolean requireSilkTouch = true;
  private boolean forceOwner = false;
  private boolean shulkerOnly = false;

  @Override
  public void init() {

    requireSilkTouch = getConfig().getBoolean("silk-touch");
    forceOwner = getConfig().getBoolean("force-owner");
    shulkerOnly = getConfig().getBoolean("shulker-only");
  }


  @EventHandler(ignoreCancelled = true)
  public void onDeletePre(final ShopDeleteBlockEvent event) {

    if (!event.isPhase(Phase.PRE)) {
      return;
    }

    final Optional<Shop> shopOptional = event.shop();
    if (shopOptional.isEmpty()) {
      return;
    }

    if (shulkerOnly && !event.blockState().getType().name().contains("SHULKER")) {
      return;
    }

    if (requireSilkTouch && !event.itemStack().containsEnchantment(Enchantment.SILK_TOUCH)) {
      return;
    }

    final ItemStack stack = ItemStack.of(event.blockState().getType(), 1);
    generatePDC(shopOptional.get(), event.player(), stack, event.blockState());
  }


  @EventHandler(ignoreCancelled = true)
  public void onDeletePost(final ShopDeleteBlockEvent event) {

    if (!event.isPhase(Phase.POST)) {
      return;
    }

    final Optional<Shop> shopOptional = event.shop();
    if (shopOptional.isEmpty()) {
      return;
    }

    if (!portableShopTokens.containsKey(shopOptional.get().getShopId())) {
      return;
    }
  }

  private void generatePDC(final Shop shop, final Player player, final ItemStack stack, final BlockState blockState) {

    final ItemMeta meta = stack.getItemMeta();
    if (meta == null || !(blockState instanceof final InventoryHolder holder)) {
      return;
    }

    final PersistentDataContainer pdc = meta.getPersistentDataContainer();
    final UUID owner = (forceOwner)? player.getUniqueId() : shop.getOwner().getUniqueId();
    final UUID token = UUID.randomUUID();

    pdc.set(PDC_CHEST_SHOP_OWNER, PersistentDataType.STRING, owner.toString());
    pdc.set(PORTABLE_KEY, PersistentDataType.BOOLEAN, true);
    pdc.set(PORTABLE_VERSION, PersistentDataType.INTEGER, CURRENT_VERSION);
    pdc.set(ID_KEY, PersistentDataType.LONG, shop.getShopId());
    pdc.set(TOKEN_KEY, PersistentDataType.STRING, token.toString());
    pdc.set(CONTENTS_KEY, PersistentDataType.STRING, serializeInventory(holder));

    stack.setItemMeta(meta);
  }

  private String serializeInventory(final InventoryHolder holder) {

    final List<SerializedSlot> contents = new ArrayList<>();

    final Inventory inventory = holder.getInventory();
    for (int slot = 0; slot < inventory.getSize(); slot++) {

      final ItemStack item = inventory.getItem(slot);
      if (item == null || item.getType().isAir()) {

        continue;
      }

      final String serialized = Base64.getEncoder().encodeToString(item.serializeAsBytes());

      contents.add(new SerializedSlot(slot, serialized));
    }

    return gson.toJson(contents);
  }

  private void restoreInventory(final InventoryHolder holder, final String json) {

    final SerializedSlot[] contents = gson.fromJson(json, SerializedSlot[].class);
    if (contents == null) {

      return;
    }

    final Inventory inventory = holder.getInventory();

    inventory.clear();

    for (final SerializedSlot content : contents) {

      if (content.slot() < 0 || content.slot() >= inventory.getSize()) {

        continue;
      }

      final byte[] bytes = Base64.getDecoder().decode(content.item());

      inventory.setItem(content.slot(), ItemStack.deserializeBytes(bytes));
    }
  }
}