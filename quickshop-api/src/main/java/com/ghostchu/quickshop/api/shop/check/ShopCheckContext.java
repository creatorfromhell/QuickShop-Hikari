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

import com.ghostchu.quickshop.api.obj.QUser;
import com.ghostchu.quickshop.api.shop.Shop;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * ShopCheckContext
 *
 * @author creatorfromhell
 * @since 6.3.0.4
 */
public record ShopCheckContext(@Nullable Shop shop,
                               @NotNull QUser user,
                               @Nullable Player player,
                               @Nullable Block signBlock,
                               boolean bypassProtectionCheck,
                               boolean message,
                               boolean autoSign,
                               boolean allowNoSpaceForSign) {

  @Nullable
  public Location location() {

    if (shop == null) {
      return null;
    }

    return shop.bukkitLocation();
  }
}