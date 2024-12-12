package cn.sakura.irinacosmetics.command;

import cn.sakura.irinacosmetics.IrinaCosmetics;
import cn.sakura.irinacosmetics.menu.AbstractMenu;
import cn.sakura.irinacosmetics.menu.player.EffectTypeSelect;
import cn.sakura.irinacosmetics.util.CC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MainCommand implements CommandExecutor{
    private static final String irina = IrinaCosmetics.irina;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(CC.translate("&b&l|&r &bI&fRINA &bCOSMETICS &8- &7v" + IrinaCosmetics.getInstance().getDescription().getVersion() + " &8- &f指令帮助"));
            sender.sendMessage(CC.translate("&b&l|&r &b主指令&f: /irina"));
            sender.sendMessage(CC.translate("&b&l|&r        &b&l- &fauthor    &7(展示制作人员名单)"));
            sender.sendMessage(CC.translate("&b&l|&r        &b&l- &fhelp    &7(你正在看着它)"));
            sender.sendMessage(CC.translate("&b&l|&r        &b&l- &fmenu    &7(打开饰品菜单)"));
            return true;
        }

        String subCommand = args[0].toLowerCase();
        switch (subCommand) {
            case "menu":
                if (sender instanceof Player) {
                    AbstractMenu menu = new EffectTypeSelect();
                    menu.open((Player) sender);
                } else {
                    sender.sendMessage(CC.translate(irina + "&c笨蛋主人, 这个指令只能以玩家身份执行啦!"));
                }
                break;

            case "author":
                sender.sendMessage(CC.translate("&b&l|&r &bI&fRINA &bCOSMETICS &8-- &f制作人员"));
                sender.sendMessage(CC.translate("&b&l|&r &b成员: &fIrina, MindsMaster_"));
                break;

            case "help":
                sender.sendMessage(CC.translate("&b&l|&r &bI&fRINA &bCOSMETICS &8- &7v" + IrinaCosmetics.getInstance().getDescription().getVersion() + " &8- &f指令帮助"));
                sender.sendMessage(CC.translate("&b&l|&r &b主指令&f: /irina"));
                sender.sendMessage(CC.translate("&b&l|&r        &b&l- &fauthor    &7(展示制作人员名单)"));
                sender.sendMessage(CC.translate("&b&l|&r        &b&l- &fhelp    &7(你正在看着它)"));
                sender.sendMessage(CC.translate("&b&l|&r        &b&l- &fmenu    &7(打开饰品菜单)"));
                break;

            default:
                sender.sendMessage(CC.translate(irina + "&c杂鱼主人, 指令错了都不知道, 真是杂鱼~"));
        }
        return true;
    }
}
