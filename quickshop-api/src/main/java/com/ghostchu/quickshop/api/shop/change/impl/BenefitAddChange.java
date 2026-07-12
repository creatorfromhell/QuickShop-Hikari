package com.ghostchu.quickshop.api.shop.change.impl;

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

import com.ghostchu.quickshop.api.QuickShopAPI;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.change.ShopChange;
import com.ghostchu.quickshop.api.shop.change.ShopChangeResult;
import com.ghostchu.quickshop.api.shop.change.arguments.BenefitAddArgument;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * BenefitAddChange
 *
 * @author creatorfromhell
 * @since 6.3.0.0
 */
public class BenefitAddChange implements ShopChange<BenefitAddArgument> {

  private BenefitAddArgument argument;

  /**
   * Configures the {@link ShopChange} with the specified argument to modify its behavior.
   *
   * @param argument the argument used to customize the action performed by this
   *                 {@link ShopChange}.
   *
   * @return the current {@link ShopChange} instance with the provided argument applied.
   */
  @Override
  public ShopChange<BenefitAddArgument> withArguments(final BenefitAddArgument argument) {

    this.argument = argument;
    return this;
  }

  /**
   * Performs an action on a list of shops and returns the result as a {@link CompletableFuture}.
   *
   * @param performer the entity that performs the action, typically a {@link CommandSender}.
   * @param shops     the list of shops to undergo the action.
   *
   * @return a {@link CompletableFuture} holding a {@link ShopChangeResult} that provides
   * information on the success or failure of the operation.
   */
  @Override
  public CompletableFuture<ShopChangeResult> perform(final CommandSender performer, final List<Shop> shops) {

    if (shops.isEmpty()) {
      return CompletableFuture.completedFuture(ShopChangeResult.failure(QuickShopAPI.getInstance().getTextManager().of(performer, "not-looking-at-shop").forLocale()));
    }

    return null;
  }
}