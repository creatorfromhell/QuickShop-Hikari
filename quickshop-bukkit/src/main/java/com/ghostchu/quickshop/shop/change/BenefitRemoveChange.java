package com.ghostchu.quickshop.shop.change;

import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.change.ShopChangeResult;
import com.ghostchu.quickshop.shop.change.arguments.BenefitArguments;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BenefitRemoveChange extends BenefitChange {

  public BenefitRemoveChange() {
    super();
  }

  public BenefitRemoveChange(final BenefitArguments arguments) {

    super(arguments);
  }

  @Override
  public CompletableFuture<ShopChangeResult> perform(final CommandSender performer, final List<Shop> shops) {

    return null;
  }
}