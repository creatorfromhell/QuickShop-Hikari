package com.ghostchu.quickshop.shop.change;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.shop.change.ShopChange;
import com.ghostchu.quickshop.shop.change.arguments.BenefitArguments;

public abstract class BenefitChange implements ShopChange<BenefitArguments> {

  protected BenefitArguments arguments = null;

  public BenefitChange() {
  }

  public BenefitChange(final BenefitArguments arguments) {

    this.arguments = arguments;
  }

  @Override
  public ShopChange<BenefitArguments> withArguments(final BenefitArguments arguments) {

    this.arguments = arguments;
    return this;
  }

  protected boolean allowOffline() {

    return QuickShop.getInstance().getConfig().getBoolean("shop.allow-offline-benefit", false);
  }
}