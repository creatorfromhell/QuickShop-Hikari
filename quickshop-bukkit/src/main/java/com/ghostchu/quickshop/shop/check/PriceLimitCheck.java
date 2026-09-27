package com.ghostchu.quickshop.shop.check;

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

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.economy.EconomyProvider;
import com.ghostchu.quickshop.api.event.Phase;
import com.ghostchu.quickshop.api.shop.PriceLimiterCheckResult;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.check.ShopCheck;
import com.ghostchu.quickshop.api.shop.check.ShopCheckContext;
import com.ghostchu.quickshop.api.shop.check.ShopCheckResult;
import com.ghostchu.quickshop.economy.transaction.QSEconomyTransaction;
import com.ghostchu.quickshop.obj.QUserImpl;
import com.ghostchu.quickshop.util.Util;
import com.ghostchu.quickshop.util.logger.Log;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jspecify.annotations.NonNull;

import java.math.BigDecimal;

import static com.ghostchu.quickshop.api.QuickShopKeys.CHECK_PRICE_LIMIT;
import static com.ghostchu.quickshop.api.QuickShopKeys.CHECK_SHOP_LIMIT;
import static com.ghostchu.quickshop.api.QuickShopKeys.PDC_CHEST_SHOP;
import static com.ghostchu.quickshop.api.QuickShopKeys.PDC_CHEST_SHOP_OWNER;

/**
 * PriceLimitCheck
 *
 * @author creatorfromhell
 * @since 6.3.0.4
 */
public class PriceLimitCheck implements ShopCheck {

  /**
   * Retrieves the unique identifier for this shop check.
   *
   * @return a {@link Key} object representing the unique identifier of the shop check
   */
  @Override
  public Key identifier() {

    return CHECK_PRICE_LIMIT;
  }

  /**
   * Performs a check based on the provided shop context, evaluating various conditions related to
   * the shop and the player interacting with it.
   *
   * @param context the context containing information about the shop, player, and other related
   *                details used to perform this check
   *
   * @return a {@link ShopCheckResult} indicating whether the check passed or failed, along with
   * additional details such as message keys or arguments if applicable
   */
  @Override
  public @NonNull ShopCheckResult check(@NonNull final ShopCheckContext context) {

    final Shop shop = context.shop();
    final Location location = context.location();
    final Player player = context.player();
    if (shop == null || location == null || player == null) {

      Log.debug("Failed shop context nullability check. Shop: " + (shop == null) + ", Location: " + (location == null) + ", Player: " + (player == null) + ".");
      return ShopCheckResult.fail("no-double-chests");
    }

    final EconomyProvider econ = QuickShop.getInstance().getEconomyManager().provider();

    final PriceLimiterCheckResult priceCheckResult = QuickShop.getInstance().getShopManager().getPriceLimiter().check(context.player(), shop.getItem(), QuickShop.getInstance().getCurrency(), shop.getPrice(), shop.shopType());
    final String currency = (shop.getCurrency() == null)? ((QuickShop.getInstance().getCurrency() == null)? "" : QuickShop.getInstance().getCurrency()) : shop.getCurrency();
    final World world = shop.bukkitLocation().getWorld();

    final double min = priceCheckResult.getMin();
    final double max = priceCheckResult.getMax();
    final String minFormatted = econ.format(BigDecimal.valueOf(min), world.getName(), currency);
    final String maxFormatted = econ.format(BigDecimal.valueOf(max), world.getName(), currency);

    return switch(priceCheckResult.getStatus()) {

      case REACHED_PRICE_MIN_LIMIT -> ShopCheckResult.fail("price-too-cheap", minFormatted);
      case REACHED_PRICE_MAX_LIMIT -> ShopCheckResult.fail("price-too-high", maxFormatted);
      case PRICE_RESTRICTED -> {
        if(min > 0 && max >= 0) {
          yield ShopCheckResult.fail("restricted-prices", Util.getItemStackName(shop.getItem()), minFormatted, maxFormatted);
        } else if(min > 0) {
          yield ShopCheckResult.fail("restricted-price-min", Util.getItemStackName(shop.getItem()), minFormatted);
        } else {
          yield ShopCheckResult.fail("restricted-price-max", Util.getItemStackName(shop.getItem()), maxFormatted);
        }
      }
      case NOT_VALID -> ShopCheckResult.fail("not-a-number", shop.getPrice());
      case NOT_A_WHOLE_NUMBER -> ShopCheckResult.fail("not-a-integer", shop.getPrice());
      case PASS -> ShopCheckResult.pass();
    };
  }
}
