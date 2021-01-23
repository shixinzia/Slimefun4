package io.github.thebusybiscuit.slimefun4.core.attributes;

import io.github.thebusybiscuit.slimefun4.core.guide.SlimefunGuide;
import io.github.thebusybiscuit.slimefun4.implementation.SlimefunPlugin;
import io.github.thebusybiscuit.slimefun4.implementation.items.geo.GEOMiner;
import io.github.thebusybiscuit.slimefun4.implementation.items.tools.GoldPan;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.SlimefunItem;
import me.mrCookieSlime.Slimefun.Objects.SlimefunItem.abstractItems.AGenerator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * This interface, when attache to a {@link SlimefunItem} class will make additional items
 * appear in the {@link SlimefunGuide}.
 * These additional items can be used represent recipes or resources that are associated
 * with this {@link SlimefunItem}.
 * <p>
 * You can find a few examples below.
 *
 * @author TheBusyBiscuit
 * @see GoldPan
 * @see GEOMiner
 * @see AGenerator
 */

public interface RecipeDisplayItem extends ItemAttribute {

    /**
     * This is the list of items to display alongside this {@link SlimefunItem}.
     * Note that these items will be filled in from top to bottom first.
     * So if you want it to express a recipe, add your input {@link ItemStack}
     * and then your output {@link ItemStack}.
     *
     * @return The recipes to display in the {@link SlimefunGuide}
     */
    @Nonnull
    List<ItemStack> getDisplayRecipes();

    @Nonnull
    default String getLabelLocalPath() {
        return "guide.tooltips.recipes.machine";
    }

    @Nonnull
    default String getRecipeSectionLabel(Player p) {
        return "&7\u21E9 " + SlimefunPlugin.getLocalization().getMessage(p, getLabelLocalPath()) + " \u21E9";
    }
}
