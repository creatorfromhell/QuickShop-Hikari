package com.ghostchu.quickshop.api.event.management;

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

import com.ghostchu.quickshop.api.event.Phase;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.check.ShopCheck;
import com.ghostchu.quickshop.api.shop.check.ShopCheckContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ShopCheckEvent
 *
 * @author creatorfromhell
 * @since 6.3.0.4
 */
public class ShopCheckEvent extends ShopEvent {


  protected final ShopCheck check;
  protected final ShopCheckContext context;

  public ShopCheckEvent(final @Nullable Shop shop, final @NotNull ShopCheck check, final @NotNull ShopCheckContext context) {

    super(shop);

    this.check = check;
    this.context = context;
  }

  public ShopCheckEvent(final Phase phase, final @Nullable Shop shop, final @NotNull ShopCheck check, final @NotNull ShopCheckContext context) {

    super(phase, shop);

    this.check = check;
    this.context = context;
  }

  public ShopCheck check() {

    return check;
  }

  public ShopCheckContext context() {

    return context;
  }

  /**
   * Creates a new instance of PhasedEvent with the specified newPhase.
   *
   * @param newPhase The new Phase for the cloned PhasedEvent
   *
   * @return A new instance of PhasedEvent with the specified newPhase
   */
  @Override
  public ShopCheckEvent clone(final Phase newPhase) {

    return new ShopCheckEvent(newPhase, this.shop, this.check, this.context);
  }
}