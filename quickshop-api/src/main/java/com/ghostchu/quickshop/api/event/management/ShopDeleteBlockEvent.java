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
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * ShopDeleteEvent is a class representing an event that occurs when a shop's block is deleted.
 *
 * @author creatorfromhell
 * @since 6.3.0.4
 */
public class ShopDeleteBlockEvent extends ShopEvent {

  protected final ItemStack itemStack;
  protected final InventoryHolder holder;

  public ShopDeleteBlockEvent(final @NotNull Shop shop, final ItemStack itemStack, final InventoryHolder holder) {

    super(shop);

    this.itemStack = itemStack;
    this.holder = holder;
  }

  public ShopDeleteBlockEvent(final Phase phase, final @NotNull Shop shop, final ItemStack itemStack, final InventoryHolder holder) {

    super(phase, shop);

    this.itemStack = itemStack;
    this.holder = holder;
  }

  /**
   * Creates a new instance of PhasedEvent with the specified newPhase.
   *
   * @param newPhase The new Phase for the cloned PhasedEvent
   *
   * @return A new instance of PhasedEvent with the specified newPhase
   */
  @Override
  public ShopDeleteBlockEvent clone(final Phase newPhase) {

    return new ShopDeleteBlockEvent(newPhase, this.shop, this.itemStack, this.holder);
  }

  public ItemStack itemStack() {

    return itemStack;
  }

  public InventoryHolder holder() {

    return holder;
  }
}