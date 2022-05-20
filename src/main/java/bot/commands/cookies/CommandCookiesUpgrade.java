package bot.commands.cookies;

import bot.Fluffer10kFun;
import bot.util.subcommand.SubcommandGroup;

public class CommandCookiesUpgrade extends SubcommandGroup {
	public CommandCookiesUpgrade(final Fluffer10kFun fluffer10kFun) {
		super("upgrade", "Upgrade your cookie baking skills", //
				new CommandCookiesUpgradeBuy(fluffer10kFun), //
				new CommandCookiesUpgradeList(fluffer10kFun));
	}
}
