package com.ghostchu.quickshop.shop.change;

import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.change.ShopChange;
import com.ghostchu.quickshop.api.shop.change.ShopChangeResult;
import com.ghostchu.quickshop.shop.change.arguments.BenefitArguments;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BenefitChange implements ShopChange<BenefitArguments> {

  private BenefitArguments arguments = null;

  public BenefitChange() {
  }

  public BenefitChange(final BenefitArguments arguments) {

    this.arguments = arguments;
  }

  @Override
  public CompletableFuture<ShopChangeResult> perform(final CommandSender performer, final List<Shop> shops) {

    return null;
  }

  @Override
  public ShopChange<BenefitArguments> withArguments(final BenefitArguments arguments) {

    this.arguments = arguments;
    return this;
  }
}