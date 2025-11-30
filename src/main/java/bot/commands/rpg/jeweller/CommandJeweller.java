package bot.commands.rpg.jeweller;

import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.stream.Collectors.toSet;

import java.util.Set;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.userData.ServerUserData;
import bot.util.SingleTimeSet;
import bot.util.subcommand.Command;

public class CommandJeweller extends Command {
	public SingleTimeSet<Set<String>> includedItems = new SingleTimeSet<>();

	public static final String jewellerImgUrl = MonsterGirlRace.AUTOMATON.imageLink;

	private final Fluffer10kFun fluffer10kFun;

	private void init() {
		includedItems.set(fluffer10kFun.items.items.values().stream()//
				.filter(item -> item.gemType != null)//
				.map(item -> item.id)//
				.collect(toSet()));
	}

	public CommandJeweller(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "jeweller", "Jeweller commands", false, //
				new CommandJewellerBuy(fluffer10kFun), //
				new CommandJewellerJoin(fluffer10kFun), //
				new CommandJewellerRefine(fluffer10kFun), //
				new CommandJewellerSell(fluffer10kFun));

		this.fluffer10kFun = fluffer10kFun;

		fluffer10kFun.apiUtils.initUtils.addInit("jeweller", this::init);
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) throws Exception {
		if (!interaction.getServer().isPresent()) {
			sendEphemeralMessage(interaction, "This command can only be used on server");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		if (!userData.jewellerAvailable) {
			sendEphemeralMessage(interaction, "You didn't unlock this feature yet");
			return;
		}

		subcommandHandler.handle(interaction);
	}
}
