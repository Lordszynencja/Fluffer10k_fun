package bot.commands.rpg;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.apis.MessageUtils.getServerTextChannel;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static bot.util.modularPrompt.ModularPromptButton.button;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.server.Server;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.subcommand.Command;

public class CommandImprove extends Command {
	private final Fluffer10kFun fluffer10kFun;

	public CommandImprove(final Fluffer10kFun fluffer10kFun) {
		super(fluffer10kFun.apiUtils, "improve", "Improve statistic (needs improvement point)");

		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final SlashCommandInteraction interaction) {
		final Server server = interaction.getServer().orElse(null);
		if (server == null) {
			sendEphemeralMessage(interaction, "Can't use this command here");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(server, interaction.getUser());
		fluffer10kFun.modularPromptUtils.addMessage(makePrompt(userData, ""), interaction);
	}

	private ModularPrompt makePrompt(final ServerUserData userData, final String prepend) {
		final String description = prepend + "Current stats:\n"//
				+ "Strength: " + userData.rpg.strength + "\n"//
				+ "Agility: " + userData.rpg.agility + "\n"//
				+ "Intelligence: " + userData.rpg.intelligence;

		final String pointsLeftMsg = userData.rpg.improvementPoints > 0
				? " (" + userData.rpg.improvementPoints + " points left)"
				: "";

		final ModularPrompt prompt = new ModularPrompt(makeEmbed("Pick stat to improve" + pointsLeftMsg, description));
		if (userData.rpg.improvementPoints > 0) {
			prompt.addButton(button("Strength", ButtonStyle.PRIMARY, //
					in -> onClickStrength(in, userData), //
					userData.rpg.strength >= userData.maxStatValue()))//
					.addButton(button("Agility", ButtonStyle.PRIMARY, //
							in -> onClickAgility(in, userData), //
							userData.rpg.agility >= userData.maxStatValue()))//
					.addButton(button("Intelligence", ButtonStyle.PRIMARY, //
							in -> onClickIntelligence(in, userData), //
							userData.rpg.intelligence >= userData.maxStatValue()));
		}

		return prompt;
	}

	private void onClickStrength(final MessageComponentInteraction in, final ServerUserData userData) {
		if (userData.rpg.improvementPoints > 0 && userData.rpg.strength < userData.maxStatValue()) {
			userData.rpg.strength++;
			userData.rpg.improvementPoints--;

			if (userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.BERSERKER_2)
					&& userData.rpg.strength >= 2) {
				fluffer10kFun.questUtils.questHeroAcademy().continueBerserker2Step(getServerTextChannel(in), userData,
						in.getUser());
			}
		}

		fluffer10kFun.modularPromptUtils.addMessage(makePrompt(userData, "Strength upgraded\n\n"), in);
	}

	private void onClickAgility(final MessageComponentInteraction in, final ServerUserData userData) {
		if (userData.rpg.improvementPoints > 0 && userData.rpg.agility < userData.maxStatValue()) {
			userData.rpg.agility++;
			userData.rpg.improvementPoints--;
		}

		fluffer10kFun.modularPromptUtils.addMessage(makePrompt(userData, "Agility upgraded\n\n"), in);
	}

	private void onClickIntelligence(final MessageComponentInteraction in, final ServerUserData userData) {
		if (userData.rpg.improvementPoints > 0 && userData.rpg.intelligence < userData.maxStatValue()) {
			userData.rpg.intelligence++;
			userData.rpg.improvementPoints--;
		}

		fluffer10kFun.modularPromptUtils.addMessage(makePrompt(userData, "Intelligence upgraded\n\n"), in);
	}

}
