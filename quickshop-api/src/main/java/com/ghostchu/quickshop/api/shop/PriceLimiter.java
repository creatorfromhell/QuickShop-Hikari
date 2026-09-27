package com.ghostchu.quickshop.api.shop;

import com.ghostchu.quickshop.api.obj.QUser;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Utility used for shop price validating
 */
public interface PriceLimiter {

  /**
   * Check the price restriction rules
   *
   * @param sender   the sender
   * @param stack    the item to check
   * @param currency the currency
   * @param price    the price
   *
   * @return the result
   */
  @NotNull
  PriceLimiterCheckResult check(@NotNull CommandSender sender, @NotNull ItemStack stack, @Nullable String currency, double price);

  @NotNull
  default PriceLimiterCheckResult check(@NotNull final CommandSender sender, @NotNull final ItemStack stack, @Nullable final String currency, final double price, @Nullable final IShopType shopType) {
    return check(sender, stack, currency, price);
  }

  /**
   * Check the price restriction rules
   *
   * @param user     the user
   * @param stack    the item to check
   * @param currency the currency
   * @param price    the price
   *
   * @return the result
   */
  @NotNull
  PriceLimiterCheckResult check(@NotNull QUser user, @NotNull ItemStack stack, @Nullable String currency, double price);

  @NotNull
  default PriceLimiterCheckResult check(@NotNull final QUser user, @NotNull final ItemStack stack, @Nullable final String currency, final double price, @Nullable final IShopType shopType) {

    return check(user, stack, currency, price);
  }
}
