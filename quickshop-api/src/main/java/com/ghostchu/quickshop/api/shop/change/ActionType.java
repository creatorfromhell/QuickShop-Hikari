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

import java.util.function.Function;

/**
 * ChangeType
 *
 * @author creatorfromhell
 * @since 6.3.0.0
 */
public record ActionType<T extends ChangeArguments>(Function<T, ShopChange<T>> factory) {

  /**
   * Creates a new {@link ShopChange} instance using the provided arguments.
   *
   * @param arguments the arguments used to configure the {@link ShopChange} instance; must not be null.
   * @return a {@link ShopChange} instance configured with the given arguments.
   */
  public ShopChange<T> create(final T arguments) {

    return factory.apply(arguments);
  }

  /**
   * Creates a new {@link ActionType} instance with the given factory function.
   *
   * @param <T> the type of arguments that extend {@link ChangeArguments}.
   * @param factory a function that generates a {@link ShopChange} instance based on the provided arguments; must not be null.
   * @return a new {@link ActionType} instance configured with the provided factory function.
   */
  public static <T extends ChangeArguments> ActionType<T> of(final Function<T, ShopChange<T>> factory) {

    return new ActionType<>(factory);
  }
}