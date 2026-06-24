package com.ghostchu.quickshop.shop.change.arguments;

import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.Nullable;

public class BenefitArguments {

  private final String player;
  private final boolean add;
  private final String percent;

  public BenefitArguments(final @NotNull String player, boolean add, final @Nullable String percent) {
    this.player = player;
    this.add = add;
    this.percent = percent;
  }

  public String percent() {

    return percent;
  }

  public boolean add() {

    return add;
  }

  public String player() {

    return player;
  }
}