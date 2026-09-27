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
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.check.ShopCheck;
import com.ghostchu.quickshop.api.shop.check.ShopCheckContext;
import com.ghostchu.quickshop.api.shop.check.ShopCheckResult;
import com.ghostchu.quickshop.util.holder.Result;
import com.ghostchu.quickshop.util.logger.Log;
import net.kyori.adventure.key.Key;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NonNull;

import static com.ghostchu.quickshop.api.QuickShopKeys.CHECK_PROTECTION;
import static com.ghostchu.quickshop.api.QuickShopKeys.CHECK_SHOP_LIMIT;

/**
 * ProtectionCheck
 *
 * @author creatorfromhell
 * @since 6.3.0.4
 */
public class ProtectionCheck implements ShopCheck {

  /**
   * Retrieves the unique identifier for this shop check.
   *
   * @return a {@link Key} object representing the unique identifier of the shop check
   */
  @Override
  public Key identifier() {

    return CHECK_PROTECTION;
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

    if (context.bypassProtectionCheck()) {
      return ShopCheckResult.pass();
    }

    final Shop shop = context.shop();
    final Location location = context.location();
    final Player player = context.player();
    if (shop == null || location == null || player == null) {

      Log.debug("Failed shop context nullability check. Shop: " + (shop == null) + ", Location: " + (location == null) + ", Player: " + (player == null) + ".");
      return ShopCheckResult.fail("3rd-plugin-build-check-failed");
    }

    final Result result = QuickShop.getInstance().getPermissionChecker().canBuild(player, shop.bukkitLocation());
    if (!result.isSuccess()) {

      if (QuickShop.getInstance().perm().hasPermission(player, "quickshop.alerts")) {

        QuickShop.getInstance().text().of(player, "3rd-plugin-build-check-failed-admin", result.getMessage(), result.getListener()).send();
      }
      Log.debug("Failed to create shop because protection check failed, found:" + result.getMessage());
      return ShopCheckResult.fail("3rd-plugin-build-check-failed", result.getMessage());
    }
    return ShopCheckResult.pass();
  }
}
