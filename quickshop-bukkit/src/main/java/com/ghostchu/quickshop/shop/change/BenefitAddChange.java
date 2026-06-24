package com.ghostchu.quickshop.shop.change;

import com.ghostchu.quickshop.QuickShop;
import com.ghostchu.quickshop.api.economy.benefit.BenefitOverflowException;
import com.ghostchu.quickshop.api.economy.benefit.BenefitProvider;
import com.ghostchu.quickshop.api.economy.benefit.BenefitsAlreadyException;
import com.ghostchu.quickshop.api.event.Phase;
import com.ghostchu.quickshop.api.event.settings.type.benefit.ShopBenefitAddEvent;
import com.ghostchu.quickshop.api.shop.Shop;
import com.ghostchu.quickshop.api.shop.change.ShopChangeResult;
import com.ghostchu.quickshop.common.util.CommonUtil;
import com.ghostchu.quickshop.obj.QUserImpl;
import com.ghostchu.quickshop.shop.change.arguments.BenefitArguments;
import com.ghostchu.quickshop.util.Util;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class BenefitAddChange extends BenefitChange {

  public BenefitAddChange() {
    super();
  }

  public BenefitAddChange(final BenefitArguments arguments) {

    super(arguments);
  }

  @Override
  public CompletableFuture<ShopChangeResult> perform(final CommandSender performer, final List<Shop> shops) {

    final QuickShop plugin = QuickShop.getInstance();

    final CompletableFuture<ShopChangeResult> result = new CompletableFuture<>();

    QUserImpl.createAsync(plugin.getPlayerFinder(), arguments.player()).thenAccept(qUser->{
      if(qUser == null) {
        plugin.text().of(performer, "unknown-player", arguments.player()).send();
        return;
      }

      if(!plugin.getConfig().getBoolean("shop.allow-offline-benefit", false) && qUser.getBukkitPlayer().isEmpty()) {

        plugin.text().of(performer, "player-offline", arguments.player()).send();
        return;
      }

      if(!parser.getArgs().get(2).endsWith("%")) {
        // Force player enter '%' to avoid player type something like 0.01 for 1%
        plugin.text().of(sender, "invalid-percentage", parser.getArgs().getFirst()).send();
        return;
      }
      final String percentageStr = CommonUtil.subBeforeLast(parser.getArgs().get(2), "%");
      Util.mainThreadRun(()->{
        try {
          double percent = Double.parseDouble(percentageStr);
          if(Double.isInfinite(percent) || Double.isNaN(percent)) {
            plugin.text().of(sender, "not-a-number", parser.getArgs().get(2)).send();
            return;
          }

          for(final Shop shop : shops) {

          }

          ShopBenefitAddEvent event = ShopBenefitAddEvent.PRE(shop, qUser, 0.0d, percent);
          event.callEvent();

          event = event.clone(Phase.MAIN);
          if(event.callCancellableEvent()) {

            plugin.logger().info("Plugin cancelled ShopBenefitAddEvent");
            plugin.text().of(sender, "internal-error").send();
            return;
          }

          percent = event.updated();

          if(percent <= 0 || percent >= 100) {
            plugin.text().of(sender, "argument-must-between", "percentage", ">0%", "<100%").send();
            return;
          }

          final BenefitProvider benefit = shop.getShopBenefit();


          benefit.add(qUser, BigDecimal.valueOf(percent / 100d));
          shop.setShopBenefit(benefit);

          event = event.clone(Phase.POST);
          event.callEvent();

          plugin.text().of(sender, "benefit-added", qUser.getDisplay()).send();
        } catch(final NumberFormatException ignore) {
          plugin.text().of(sender, "not-a-number", percentageStr).send();
        } catch(final BenefitOverflowException e) {
          plugin.text().of(sender, "benefit-overflow", (e.benefit().doubleValue() * 100) + "%").send();
        } catch(final BenefitsAlreadyException ignore) {
          plugin.text().of(sender, "benefit-exists").send();
        }
      });
    }).exceptionally(e->{
      plugin.logger().warn("Failed to get uuid of player " + player, e);
      plugin.text().of(sender, "internal-error").send();
      return null;
    });

    return null;
  }
}