package cn.sakura.irinacosmetics.util;

import cn.charlotte.pit.UtilKt;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemUtil {
    private ItemStack is;

    public ItemUtil(Material mat) {
        this.is = new ItemStack(mat);
    }

    public ItemUtil(ItemStack is) {
        this.is = is;
    }

    public ItemUtil material(Material mat) {
        this.is = new ItemStack(mat);
        return this;
    }

    public ItemUtil amount(int amount) {
        this.is.setAmount(amount);
        return this;
    }

    public ItemUtil name(String name) {
        ItemMeta meta = this.is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemUtil lore(String name) {
        ItemMeta meta = this.is.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemUtil lore(String... lore) {
        List<String> toSet = new ArrayList<>();
        ItemMeta meta = this.is.getItemMeta();

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        this.is.setItemMeta(meta);
        return this;
    }

    public ItemUtil changeNbt(String key, String value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }

        extra.setString(key, value);
        tag.set("extra", extra);
        nmsItem.setTag(tag);
        this.is = CraftItemStack.asBukkitCopy(nmsItem);
        return this;
    }

    public ItemUtil changeNbt(String key, boolean value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }

        extra.setBoolean(key, value);
        tag.set("extra", extra);
        nmsItem.setTag(tag);
        this.is = CraftItemStack.asBukkitCopy(nmsItem);
        return this;
    }

    public ItemUtil changeNbt(String key, int value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }

        extra.setInt(key, value);
        tag.set("extra", extra);
        nmsItem.setTag(tag);
        this.is = CraftItemStack.asBukkitCopy(nmsItem);
        return this;
    }

    public ItemUtil changeNbt(String key, double value) {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(this.is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        NBTTagCompound extra = tag.getCompound("extra");
        if (extra == null) {
            extra = new NBTTagCompound();
        }

        extra.setDouble(key, value);
        tag.set("extra", extra);
        nmsItem.setTag(tag);
        this.is = CraftItemStack.asBukkitCopy(nmsItem);
        return this;
    }

    public ItemUtil setSkullOwner(String owner) {
        SkullMeta im = (SkullMeta)this.is.getItemMeta();
        im.setOwner(owner);
        this.is.setItemMeta(im);
        return this;
    }

    public ItemUtil setInternalName(String name) {
        this.changeNbt("internal", name);
        return this;
    }

    public static String getInternalName(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
            NBTTagCompound tag = null;
            if (nmsItem != null) {
                tag = nmsItem.getTag();
            }
            if (tag == null) {
                return null;
            } else {
                NBTTagCompound extra = tag.getCompound("extra");
                if (extra == null) {
                    return null;
                } else {
                    return !extra.hasKey("internal") ? null : extra.getString("internal");
                }
            }
        } else {
            return null;
        }
    }

    public ItemStack build() {
        return this.is;
    }

    public ItemStack buildWithUnbreakable() {
        net.minecraft.server.v1_8_R3.ItemStack nmsItem = UtilKt.reflectGetNmsItem(this.is);
        NBTTagCompound tag = nmsItem.getTag();
        if (tag == null) {
            tag = new NBTTagCompound();
        }

        tag.setBoolean("Unbreakable", true);
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }
}
