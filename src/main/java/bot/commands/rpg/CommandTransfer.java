package bot.commands.rpg;

import static bot.data.items.ItemUtils.playCoinsName;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.SlashCommandOption;
import org.javacord.api.interaction.SlashCommandOptionChoice;
import org.javacord.api.interaction.SlashCommandOptionType;

import bot.Fluffer10kFun;
import bot.data.items.ItemUtils;
import bot.userData.ServerUserData;
import bot.util.subcommand.Command;

public class CommandTransfer extends Command {
	public static enum Currency {
		MONIES("monies", userData -> userData.monies, (userData, amount) -> userData.monies += amount,
				ItemUtils::getFormattedMonies), //
		PLAY_COINS(playCoinsName, userData -> userData.playCoins, (userData, amount) -> userData.playCoins += amount,
				ItemUtils::getFormattedPlayCoins), //
		MONSTER_GOLD("monster gold", userData -> userData.monsterGold,
				(userData, amount) -> userData.monsterGold += amount, ItemUtils::getFormattedMonsterGold);

		public static List<SlashCommandOptionChoice> getOptions() {
			final List<SlashCommandOptionChoice> currencyOptions = new ArrayList<>();
			for (final Currency currency : Currency.values()) {
				currencyOptions.add(SlashCommandOptionChoice.create(currency.name, currency.name()));
			}
			return currencyOptions;
		}

		public final String name;
		public final Function<ServerUserData, Long> getter;
		public final BiConsumer<ServerUserData, Long> adder;
		public final Function<Long, String> formatter;

		private Currency(final String name, final Function<ServerUserData, Long> getter,
				final BiConsumer<ServerUserData, Long> adder, final Function<Long, String> formatter) {
			this.name = name;
			this.getter = getter;
			this.adder = adder;
			this.formatter = formatter;
		}
	}

	private final Fluffer10kFun fluffer10kFun;

	public CommandTransfer(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "transfer", "Transfer currency to someone", //
				SlashCommandOption.create(SlashCommandOptionType.USER, "target", "person to transfer to", true), //
				SlashCommandOption.create(SlashCommandOptionType.LONG, "amount", "amount of currency to give", true), //
				SlashCommandOption.createWithChoices(SlashCommandOptionType.STRING, "currency", "currency to transfer",
						true, Currency.getOptions()));

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final User user = interaction.getUser();
		final User target = interaction.getOptionUserValueByName("target").get();
		final long amount = interaction.getOptionLongValueByName("amount").get();
		final Currency currency = Currency.valueOf(interaction.getOptionStringValueByName("currency").get());
		if (amount <= 0) {
			sendEphemeralMessage(interaction, "You can't give nothing");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), user.getId());
		if (currency.getter.apply(userData) < amount) {
			sendEphemeralMessage(interaction, "You don't have that much");
			return;
		}

		final ServerUserData targetData = fluffer10kFun.serverUserDataUtils.getUserData(server.getId(), target.getId());

		currency.adder.accept(userData, -amount);
		currency.adder.accept(targetData, amount);

		final String description = target.getDisplayName(server) + " got " + currency.formatter.apply(amount) + " from "
				+ user.getDisplayName(server);

		interaction.createImmediateResponder().append(target).addEmbed(makeEmbed("Transfer", description)).respond();
	}
}
