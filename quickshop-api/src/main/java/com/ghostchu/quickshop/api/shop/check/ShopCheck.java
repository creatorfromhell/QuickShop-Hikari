package com.ghostchu.quickshop.api.shop.check;

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

import net.kyori.adventure.key.Key;
import org.jetbrains.annotations.NotNull;

/**
 * ShopCheck
 *
 * @author creatorfromhell
 * @since 6.3.0.4
 */
public interface ShopCheck {

  /**
   * Retrieves the unique identifier for this shop check.
   *
   * @return a {@link Key} object representing the unique identifier of the shop check
   */
  @NotNull
  Key identifier();

  /**
   * Determines whether this shop check applies to the specified {@link ShopCheckType}.
   *
   * @param type the {@link ShopCheckType} representing the context in which the shop check is being applied
   * @return {@code true} if this shop check applies to the specified type, otherwise {@code false}
   */
  default boolean appliesTo(@NotNull final ShopCheckType type) {
    return true;
  }

  /**
   * Performs a check based on the provided shop context, evaluating various
   * conditions related to the shop and the player interacting with it.
   *
   * @param context the context containing information about the shop, player,
   *                and other related details used to perform this check
   * @return a {@link ShopCheckResult} indicating whether the check passed or
   *         failed, along with additional details such as message keys
   *         or arguments if applicable
   */
  @NotNull
  ShopCheckResult check(@NotNull final ShopCheckContext context);
}