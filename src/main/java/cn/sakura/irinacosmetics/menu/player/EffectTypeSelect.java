package cn.sakura.irinacosmetics.menu.player;

import cn.sakura.irinacosmetics.data.BalanceManager;
import cn.sakura.irinacosmetics.util.ItemUtil;
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

import static cn.sakura.irinacosmetics.util.ItemUtil.createGlassPane;

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

        ItemStack whiteGlassPane = createGlassPane((short) 0);
        ItemStack redGlassPane = createGlassPane((short) 14);

        int coin = BalanceManager.getBalance(player.getUniqueId());

        addItemToInventory(12, new ItemUtil(Material.ARROW).shiny().setInternalName("Shoot").build(), "&f弹射物轨迹", List.of("", "&e点击选择你的弹射物轨迹"));
        addItemToInventory(14, new ItemUtil(Material.IRON_SWORD).shiny().setInternalName("Kill").build(), "&f击杀效果", List.of("", "&e点击选择你的击杀效果"));
        addItemToInventory(16, new ItemUtil(Material.GHAST_TEAR).shiny().setInternalName("Death").build(), "&f亡语", List.of("", "&e点击选择你的亡语"));

        int[] whitePaneSlots = {1, 10, 19};
        for (int slot : whitePaneSlots) {
            addItemToInventory(slot, whiteGlassPane, "&r", List.of(""));
        }

        addItemToInventory(18, new ItemUtil(redGlassPane).setInternalName("Close").build(), "&c关闭", List.of(""));
        addItemToInventory(9, new ItemUtil(new ItemStack(Material.SKULL_ITEM, 1, (short) 3)).setSkullOwner(player.getName()).build(), "&7余额: &e" + coin, List.of("", "&bI&fRINA"));
    }

    @EventHandler
    public void handleClick(InventoryClickEvent e) {
        if (!e.getView().getTitle().equals(getMenuName())) return;
        e.setCancelled(true);
        Player player = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();

        if (clickedItem == null || ItemUtil.getInternalName(clickedItem) == null) return;

        AbstractMenu menu = effectMenus.get(ItemUtil.getInternalName(clickedItem).toLowerCase());

        if (menu == null) return;
        menu.open(player);
    }
}