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

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author creatorfromhell
 * @since 6.3.0.0
 */
public interface ShopChange<T> {

  /**
   * Performs an operation on the given list of shops and returns the result encapsulated
   * within a {@link CompletableFuture}.
   *
   * @param shops a {@link List} of {@link Shop} instances on which the operation is to be performed.
   *              The list must not be null, and all shops provided should be properly initialized
   *              and valid for the operation.
   * @return a {@link CompletableFuture} that resolves to a {@link ShopChangeResult},
   *         containing details of the shops impacted by the operation and whether it was successful.
   */
  CompletableFuture<ShopChangeResult> perform(final List<Shop> shops);

  /**
   * Sets the arguments for the shop change operation and returns a new instance of {@code ShopChange}
   * with the specified arguments applied.
   *
   * @param arguments the arguments of type {@code T} that will be applied to the shop change operation.
   *                  Must not be null.
   * @return a new {@code ShopChange} instance with the specified arguments applied.
   */
  ShopChange<T> withArguments(final T arguments);
}