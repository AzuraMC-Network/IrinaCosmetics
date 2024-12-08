package cn.sakura.irinacosmetics.menu.player;

import cn.charlotte.pit.data.PlayerProfile;
import cn.charlotte.pit.util.item.ItemBuilder;
import cn.charlotte.pit.util.item.ItemUtil;
import cn.sakura.irinacosmetics.game.Register;
import cn.sakura.irinacosmetics.menu.AbstractMenu;
import cn.sakura.irinacosmetics.menu.player.impl.DeathEffect;
import cn.sakura.irinacosmetics.menu.player.impl.KillEffect;
import cn.sakura.irinacosmetics.menu.player.impl.ShootEffect;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

@Register
public class EffectTypeSelect extends AbstractMenu implements Listener {
    private static final HashMap<String, AbstractMenu> effectMenus = new HashMap<>();

    @Override
    public String getMenuName() {
        return "特效选择";
    }

    @Override
    public int getMenuSize() {
        return 27;
    }

    @Override
    protected void setupItems(Player player) {
        effectMenus.put("shoot", new ShootEffect());
        effectMenus.put("kill", new KillEffect());
        effectMenus.put("death", new DeathEffect());

        ItemStack whiteGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);
        whiteGlassPane.setDurability((short)0);
        ItemStack redGlassPane = new ItemStack(Material.STAINED_GLASS_PANE);
        redGlassPane.setDurability((short)14);

        PlayerProfile profile = PlayerProfile.getPlayerProfileByUuid(player.getUniqueId());

        addItemToInventory(12, new ItemBuilder(Material.ARROW).shiny().internalName("Shoot").build(), "&f弹射物轨迹", List.of("", "&e点击选择你的弹射物轨迹"));
        addItemToInventory(14, new ItemBuilder(Material.IRON_SWORD).shiny().internalName("Kill").build(), "&f击杀效果", List.of("", "&e点击选择你的击杀效果"));
        addItemToInventory(16, new ItemBuilder(Material.GHAST_TEAR).shiny().internalName("Death").build(), "&f亡语", List.of("", "&e点击选择你的亡语"));

        addItemToInventory(1, whiteGlassPane, "&r", List.of(""));
        addItemToInventory(10, whiteGlassPane, "&r", List.of(""));
        addItemToInventory(19, whiteGlassPane, "&r", List.of(""));

        addItemToInventory(18, new ItemBuilder(redGlassPane).internalName("Close").build(), "&c关闭", List.of(""));
        addItemToInventory(9, new ItemBuilder(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).setSkullOwner(player.getName()).build(), "&7余额: &e" + (int) profile.getCoins(), List.of("", "&bI&fRINA"));
    }

    @EventHandler
    public void handleClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(getMenuName())) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null) return;

        AbstractMenu menu = effectMenus.get(ItemUtil.getInternalName(clickedItem).toLowerCase());

        if (menu == null) return;
        menu.open(player);
    }
}