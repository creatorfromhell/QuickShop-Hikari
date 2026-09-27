package com.ghostchu.quickshop.api;

/*
 * QuickShop-Hikari
 * Copyright (C) 2026 Daniel "creatorfromhell" Vidmar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import org.bukkit.NamespacedKey;

/**
 * QuickShopKeys
 *
 * @author creatorfromhell
 * @since 6.3.0.4
 */
public class QuickShopKeys {

  public static final NamespacedKey PDC_CHEST_SHOP = key("chest_shop");
  public static final NamespacedKey PDC_CHEST_SHOP_OWNER = key("chest_shop_owner");
  public static final NamespacedKey PDC_DISPLAY_ITEM_KEY_INSTANCE = key("qs-display-interaction");

  public static final NamespacedKey CHECK_AUTO_SIGN = key("check_auto_sign");
  public static final NamespacedKey CHECK_BLOCK = key("check_block");
  public static final NamespacedKey CHECK_DOUBLE_CHEST = key("check_double_chest");
  public static final NamespacedKey CHECK_ITEM_BLACKLIST = key("check_item_blacklist");
  public static final NamespacedKey CHECK_PRICE_LIMIT = key("check_price_limit");
  public static final NamespacedKey CHECK_PROTECTION = key("check_protection");
  public static final NamespacedKey CHECK_SHOP_EXISTS = key("check_shop_exists");
  public static final NamespacedKey CHECK_SHOP_LIMIT = key("check_shop_limit");

  public static NamespacedKey key(final String key) {

    return new NamespacedKey(QuickShopAPI.getPluginInstance(), key);
  }
}