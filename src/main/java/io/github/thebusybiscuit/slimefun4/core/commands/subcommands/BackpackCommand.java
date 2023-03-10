package io.github.thebusybiscuit.slimefun4.core.commands.subcommands;

import io.github.thebusybiscuit.slimefun4.api.player.PlayerBackpack;
import io.github.thebusybiscuit.slimefun4.core.commands.SlimefunCommand;
import io.github.thebusybiscuit.slimefun4.core.commands.SubCommand;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunItems;
import io.github.thebusybiscuit.slimefun4.implementation.items.backpacks.RestoredBackpack;
import io.github.thebusybiscuit.slimefun4.utils.ChestMenuUtils;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import me.mrCookieSlime.CSCoreLibPlugin.general.Inventory.ChestMenu;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * This command that allows for backpack retrieval in the event they are lost.
 * The command accepts a name and id, if those match up it spawns a Medium Backpack
 * with the correct lore set in the sender's inventory.
 *
 * @author Sfiguz7
 * @see RestoredBackpack
 */
class BackpackCommand extends SubCommand {
    private final int DISPLAY_START_SLOT = 9;

    @ParametersAreNonnullByDefault
    BackpackCommand(Slimefun plugin, SlimefunCommand cmd) {
        super(plugin, cmd, "backpack", false);
    }

    @Override
    protected String getDescription() {
        return "commands.backpack.description";
    }

    @Override
    public void onExecute(CommandSender sender, String[] args) {
        if (sender instanceof Player player) {
            if (sender.hasPermission("slimefun.command.backpack")) {
                if (args.length < 1) {
                    Slimefun.getLocalization().sendMessage(sender, "messages.usage", true, msg -> msg.replace("%usage%", "/sf backpack (玩家名)"));
                    return;
                }

                OfflinePlayer backpackOwner;

                if (args.length == 2) {
                    if (sender.hasPermission("slimefun.command.backpack.other")) {
                        backpackOwner = Bukkit.getOfflinePlayer(args[1]);
                    } else {
                        Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
                        return;
                    }
                } else {
                    backpackOwner = player;
                }

                if (!(backpackOwner instanceof Player) && !backpackOwner.hasPlayedBefore()) {
                    Slimefun.getLocalization().sendMessage(sender, "commands.backpack.player-never-joined");
                    return;
                }

                openBackpackMenu(backpackOwner, player, 1);
            } else {
                Slimefun.getLocalization().sendMessage(sender, "messages.no-permission", true);
            }
        } else {
            Slimefun.getLocalization().sendMessage(sender, "messages.only-players", true);
        }
    }

    private void openBackpackMenu(@Nonnull OfflinePlayer owner, @Nonnull Player p, int page) {
        Validate.notNull(p, "The player cannot be null!");
        Validate.isTrue(page > 0, "Backpack page must greater than 0!");

        var bps = Slimefun.getDatabaseManager().getProfileDataController()
                .getBackpacks(owner.getUniqueId().toString()).stream().toList();

        if (bps.isEmpty()) {
            Slimefun.getLocalization().sendMessage(p, "commands.backpack.backpack-does-not-exist");
            return;
        }

        var menu = new ChestMenu(owner.getName() + " 拥有的背包列表");
        menu.setEmptySlotsClickable(false);

        var pages = bps.size() / 36;

        // Draw background start
        for (int i = 0; i < 9; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground());
            menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
        }

        // max display 36 backpacks per page
        for (int i = 0; i <= 36; i++) {
            int slot = DISPLAY_START_SLOT + i;
            var index = i + 36 * (page - 1);
            if (index >= bps.size()) {
                break;
            }
            var bp = bps.get(index);

            var visualBackpack = SlimefunItems.RESTORED_BACKPACK.clone();
            var im = visualBackpack.getItemMeta();
            im.setDisplayName(bp.getName().isEmpty() ? "背包 #" + bp.getId() : bp.getName());
            var lore = new ArrayList<String>();
            lore.add("");
            lore.add(ChatColor.translateAlternateColorCodes('&', "&a左键 获取此背包"));
            im.setLore(lore);
            visualBackpack.setItemMeta(im);

            var restoreBp = SlimefunItems.RESTORED_BACKPACK.clone();
            PlayerBackpack.bindItem(restoreBp, bp);
            menu.addItem(slot, visualBackpack);
            menu.addMenuClickHandler(slot, (p1, slot1, item, action) -> {
                if (!action.isRightClicked() && !action.isShiftClicked() && p1.getUniqueId() == p.getUniqueId()) {
                    p1.getInventory().addItem(restoreBp);
                    p1.closeInventory();
                    Slimefun.getLocalization().sendMessage(p1, "commands.backpack.restored-backpack-given");
                }

                return false;
            });
        }

        for (int i = 45; i < 54; i++) {
            menu.addItem(i, ChestMenuUtils.getBackground());
            menu.addMenuClickHandler(i, (pl, slot, item, action) -> false);
        }

        // Draw background end

        if (pages > 1) {
            menu.addItem(46, ChestMenuUtils.getPreviousButton(p, page, pages));
            menu.addMenuClickHandler(46, (pl, slot, item, action) -> {
                int next = page - 1;

                if (next != page && next > 0) {
                    openBackpackMenu(owner, p, next);
                }

                return false;
            });

            if (page < pages) {
                menu.addItem(52, ChestMenuUtils.getNextButton(p, page, pages));
                menu.addMenuClickHandler(52, (pl, slot, item, action) -> {
                    int next = page + 1;

                    if (next != page && next <= pages) {
                        openBackpackMenu(owner, p, next);
                    }

                    return false;
                });
            }
        }
    }
}
