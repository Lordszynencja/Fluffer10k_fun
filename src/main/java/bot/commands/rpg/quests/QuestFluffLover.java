package bot.commands.rpg.quests;

import static bot.data.items.data.PotionItems.AGILITY_3_POTION;
import static bot.data.items.data.PotionItems.HEALTH_POTION_MAJOR;
import static bot.data.items.data.PotionItems.INTELLIGENCE_3_POTION;
import static bot.data.items.data.PotionItems.MANA_POTION_MAJOR;
import static bot.data.items.data.PotionItems.STRENGTH_3_POTION;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;
import static bot.util.Utils.joinNames;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserFluffLoverQuestFluffingStepData;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.apis.APIUtils;

public class QuestFluffLover extends Quest {
	public QuestFluffLover() {
		super(QuestType.FLUFF_LOVER_QUEST, 3);

		continueQuestHandlers.put(QuestStep.FLUFFING, this::continueFluffingStep);
	}

	private static final List<MonsterGirlRace> races = asList(//
			MonsterGirlRace.ANUBIS, //
			MonsterGirlRace.BAPHOMET, //
			MonsterGirlRace.BASILISK, //
			MonsterGirlRace.CAIT_SITH, //
			MonsterGirlRace.CHESHIRE_CAT, //
			MonsterGirlRace.CU_SITH, //
			MonsterGirlRace.DORMOUSE, //
			MonsterGirlRace.GRIZZLY, //
			MonsterGirlRace.GYOUBU_DANUKI, //
			MonsterGirlRace.HAKUTAKU, //
			MonsterGirlRace.HELLHOUND, //
			MonsterGirlRace.HOLSTAUR, //
			MonsterGirlRace.INARI, //
			MonsterGirlRace.JINKO, //
			MonsterGirlRace.KAKUEN, //
			MonsterGirlRace.KAMAITACHI, //
			MonsterGirlRace.KIKIMORA, //
			MonsterGirlRace.KOBOLD, //
			MonsterGirlRace.MARCH_HARE, //
			MonsterGirlRace.MINOTAUR, //
			MonsterGirlRace.MOTHMAN, //
			MonsterGirlRace.NEKOMATA, //
			MonsterGirlRace.OCELOMEH, //
			MonsterGirlRace.OWL_MAGE, //
			MonsterGirlRace.RAIJU, //
			MonsterGirlRace.RATATOSKR, //
			MonsterGirlRace.SATYROS, //
			MonsterGirlRace.SPHINX, //
			MonsterGirlRace.WERERABBIT, //
			MonsterGirlRace.WERESHEEP, //
			MonsterGirlRace.WEREWOLF, //
			MonsterGirlRace.YOUKO);

	private static final String stepFluffingText = String.join("\n", //
			description(
					"While walking the path to another village in the mountains, you get the feeling someone is following you. To check it you walked behind a corner and waited, just to find someone was actually stalking you! You grab him and ask why is he walking after you."), //
			dialogue("I-I-I don't mean any harm, I swear! I just thought that I could see who you are fighting!"), //
			description("You ask him why is he so interested in the fights and didn't just tell you."), //
			dialogue("Y-you see... I'm interested in specific fights"), //
			description("He pauses, looks away and blushes."), //
			dialogue("I'm interested in... girls with fluffy tails..."), //
			description("You try to make something out of it as he starts to beg you."), //
			dialogue(
					"Please, can I come with you? A-and could you touch the tails of different monster girls for me and tell me which one is the best?"), //
			description("You think for a moment before agreeing to the weird request."), //
			dialogue("T-Thank you! This will help me choose my perfect wife! H-here's the list if you don't mind."));
	private static final String stepFluffingDescription = String.join("\n", //
			"Fluff the tails of each monster girl race from the list.", //
			"Races left: %1$s.", //
			"Use the " + bold("/quest continue") + " to tell the boy the results once you do that.");

	public static String getStepFluffingDescription(final List<MonsterGirlRace> racesLeft) {
		return String.format(stepFluffingDescription, joinNames(mapToList(racesLeft, race -> race.race)));
	}

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final List<MonsterGirlRace> racesLeft = new ArrayList<>(races);

		final UserQuestData questData = new UserFluffLoverQuestFluffingStepData(getStepFluffingDescription(racesLeft),
				racesLeft);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder().addEmbed(newQuestMessage(stepFluffingText)).respond();
	}

	private static final String stepFinishedText = String.join("\n", //
			description("You come back to the weird guy and tell him about your experiences."), //
			dialogue(
					"Oh yes, great, thank you very much, now I will pick my perfect wife! Uhm... W-wait, which one should I pick, so many are really good. Aaaaah, I have no idea what to do now! Ah right, you probably should have something for all that trouble. Don't worry, I have money, h-here you go. Now I will go and read everything. Thank you again, without you I would just get attacked by first girl I would meet."), //
			description(
					"Man leaves you your reward and hurriedly goes to hide in his tent. You get a feeling this is not the end."));
	private static final String stepFinishedDescription = "You helped a man gather data about various fluffy girls. Who knows what will happen next? At least you touched some fluffy tails.";

	private void continueFluffingStep(final APIUtils apiUtils, final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		final UserFluffLoverQuestFluffingStepData questData = userData.rpg.quests.get(type).asSpecific();
		if (!questData.girlsLeft.isEmpty()) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You didn't fluff all the girls from the list.")).update();
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		userData.monies += 10_000;
		userData.addItem(HEALTH_POTION_MAJOR, 5);
		userData.addItem(MANA_POTION_MAJOR, 5);
		userData.addItem(STRENGTH_3_POTION);
		userData.addItem(AGILITY_3_POTION);
		userData.addItem(INTELLIGENCE_3_POTION);

		interaction.createOriginalMessageUpdater().addEmbeds(makeEmbed(type.name, stepFinishedText), //
				userData.addExpAndMakeEmbed(apiUtils, 20_000, interaction.getUser(), interaction.getServer().get()), //
				makeEmbed("Obtained items", "You got 10 000 gold and bunch of potions"))//
				.update();
	}
}
