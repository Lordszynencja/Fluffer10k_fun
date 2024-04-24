package bot.commands.cookies;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.userData.ServerUserData;
import bot.util.subcommand.Subcommand;

public class CommandMilksShow extends Subcommand {
	private final Fluffer10kFun fluffer10kFun;

	public CommandMilksShow(final Fluffer10kFun fluffer10kFun) {
		super("show", "Show one of your milk bottles", //
				SlashCommandOption.create(SlashCommandOptionType.STRING, "milk_name", "Milk to show", true));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final long serverId = interaction.getServer().get().getId();
		final long userId = interaction.getUser().getId();

		final String milkName = getOption(interaction).getArgumentStringValueByName("milk_name").get();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(serverId, userId);
		if (userData.cookies.milkCounts.getOrDefault(milkName, 0L) == 0) {
			sendEphemeralMessage(interaction, "Couldn't find such milk type in your collection");
			return;
		}

		final EmbedBuilder embed = makeEmbed("Your " + milkName)//
				.setImage(fluffer10kFun.cookieUtils.getMilkFile(milkName))//
				.setColor(CookieUtils.milkColor);
		interaction.respondLater().join().addEmbed(embed).update();
	}
}
