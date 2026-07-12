package com.ghostchu.quickshop.api.shop.change;

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

import com.ghostchu.quickshop.api.shop.Shop;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Optional;

/**
 * ShopChangeResult
 *
 * @author creatorfromhell
 * @since 6.3.0.0
 */
public record ShopChangeResult(Status status, List<Shop> failedShops, int successCount, Optional<Component> message) {

  /**
   * Constructs a {@link ShopChangeResult} instance while ensuring that the list of failed shops
   * is immutable by making a defensive copy of it.
   *
   * @param status the status of the shop change operation, indicating success, partial success, or failure.
   * @param failedShops the list of shops for which the change operation failed; must not be modified after initialization.
   * @param successCount the number of shops successfully modified during the operation.
   * @param message an optional message providing additional context or information about the result.
   */
  public ShopChangeResult {
    failedShops = List.copyOf(failedShops);
  }

  public static ShopChangeResult failure(final Component message) {
    return new ShopChangeResult(Status.FAILURE, List.of(), 0, Optional.of(message));
  }

  public static ShopChangeResult failure(final List<Shop> failedShops, final Component message) {
    return new ShopChangeResult(Status.FAILURE, failedShops, 0, Optional.of(message));
  }

  /**
   * Represents the possible outcomes of a shop change operation.
   *
   * Each status indicates the result of an attempted change to one or more shops:
   * - SUCCESS: The operation completed successfully for all targeted shops.
   * - PARTIAL_SUCCESS: The operation completed successfully for some targeted shops, but failed for others.
   * - FAILURE: The operation failed for all targeted shops.
   */
  public enum Status {
    SUCCESS,
    PARTIAL_SUCCESS,
    FAILURE
  }
}