package bot.commands.rpg.quests;

import static bot.util.CollectionUtils.addToIntOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.FurnitureType;
import bot.data.items.data.MonmusuDropItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserInsomniaQuestWaitingStepData;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.apis.APIUtils;

public class QuestInsomnia extends Quest {
	private static long getCurrentTime() {
		return System.currentTimeMillis() / 1000;
	}

	private static final long time1 = 60 * 60;
	private static final long time2 = 24 * 60 * 60;
	private static final long time3 = 2 * 24 * 60 * 60;
	private static final long time4 = 3 * 24 * 60 * 60;
	private static final long time5 = 4 * 24 * 60 * 60;

	public QuestInsomnia() {
		super(QuestType.INSOMNIA_QUEST, 4);

		continueQuestHandlers.put(QuestStep.GETTING_WERESHEEP_WOOL, this::continueGettingWeresheepWoolStep);
		continueQuestHandlers.put(QuestStep.GETTING_BAROMETZ_COTTON, this::continueGettingBarometzCottonStep);
		continueQuestHandlers.put(QuestStep.GETTING_ARACHNE_SILK, this::continueGettingArachneSilkStep);
		continueQuestHandlers.put(QuestStep.WAITING, this::continueWaitingStep);
	}

	private static final String stepGettingWeresheepWoolText = String.join("\n", //
			description(
					"While strolling through the city, you hear a weeping in one of the stores, and decide to go in to check it."), //
			description("Inside, a succubus is crying with face hidden in her hands."), //
			dialogue("I'm sorry, I'm closed, and I don't know when I will open again."), //
			description("You ask her what happened."), //
			dialogue(
					"I ran out of materials! I can't make anything without them! I will have to sell my own bed soon..."), //
			description("You offer to help her, and ask where the materials are."), //
			dialogue(
					"That's the problem, they didn't come. I don't know what happened, but last few shipments just didn't arrive, and now I'm gonna go bankrupt!"), //
			description("You ask the poor girl for what she needs."), //
			dialogue(
					"I need weresheep wool, or I won't have anything to put in the pillows and beds. And I need quite a lot of it too... Would you help me?"), //
			description("You agree to help and she stops crying, weeping away her tears."), //
			dialogue("Bring me five pieces, and I will be able to start working, please."), //
			description("After hearing that, you set out to get weresheep wool"));
	private static final String stepGettingWeresheepWoolDescription = String.join("\n", //
			"Collect five pieces of weresheep wool and bring them to the succubus.", //
			"Use the " + bold("/quest continue") + " to give her the items once you found them.");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.GETTING_WERESHEEP_WOOL,
				stepGettingWeresheepWoolDescription, true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage(stepGettingWeresheepWoolText).setImage(MonsterGirlRace.SUCCUBUS.imageLink))
				.respond();
	}

	private static final String stepGettingBarometzCottonText = String.join("\n", //
			description("You come in and the girl smiles at you."), //
			dialogue("Did you find some?"), //
			description("You hand her all of the pieces."), //
			dialogue(
					"Thank you so much! Now I can start working! But... That is not enough to fully fill everything, I will need some filler too. Would you bring me some Barometz's cotton? It's perfectly fluffy for this, three big pieces should be enough."), //
			description("You smile and walk out to get the cotton that nice girl asked for."));
	private static final String stepGettingBarometzCottonDescription = String.join("\n", //
			"Collect three pieces of barometz's cotton and bring them to the girl.", //
			"Use the " + bold("/quest continue") + " to give her the items once you found them.");

	private void continueGettingWeresheepWoolStep(final APIUtils apiUtils,
			final MessageComponentInteraction interaction, final ServerUserData userData) {
		defaultGiveItemStep(apiUtils, interaction, userData, type, MonmusuDropItems.WERESHEEP_WOOL, 5,
				QuestStep.GETTING_BAROMETZ_COTTON, true, stepGettingBarometzCottonText,
				stepGettingBarometzCottonDescription, MonsterGirlRace.SUCCUBUS.imageLink, 500);
	}

	private static final String stepGettingArachneSilkText = String.join("\n", //
			description("You come in and you see that the girl is sleeping, probably effect of all the wool."), //
			description(
					"You leave the cotton and a note saying that you visited her, and read her todo list, which stats that she needs some arachne silk to sew it all. Ten strings should be enough."));
	private static final String stepGettingArachneSilkDescription = String.join("\n", //
			"Collect ten strings of arachne silk and bring them to the girl.", //
			"Use the " + bold("/quest continue") + " to give her the items once you found them.");

	private void continueGettingBarometzCottonStep(final APIUtils apiUtils,
			final MessageComponentInteraction interaction, final ServerUserData userData) {
		defaultGiveItemStep(apiUtils, interaction, userData, type, MonmusuDropItems.BAROMETZ_COTTON, 3,
				QuestStep.GETTING_ARACHNE_SILK, true, stepGettingArachneSilkText, stepGettingArachneSilkDescription,
				MonsterGirlRace.SUCCUBUS.imageLink, 1500);
	}

	private static final String stepWaitingText = String.join("\n", //
			description("You come in and the girl hugs you as you come in."), //
			dialogue("Thank you for the cotton! I was so happy when it was there after I woke up~"), //
			description("You give the girl the strings."), //
			dialogue(
					"and you have the strings! I can finish sewing now! Thank you~! I don't have anything that I could give you, but when I get the business back on feet, I'll prepare something for you!"));
	private static final String stepWaitingDescription = String.join("\n", //
			"Come back later to collect your reward.", //
			"Use the " + bold("/quest continue") + " a bit later.");

	private void continueGettingArachneSilkStep(final APIUtils apiUtils, final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.hasItem(MonmusuDropItems.ARACHNE_SILK, 10)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You don't have the necessary items.")).update();
			return;
		}

		userData.rpg.setQuest(new UserInsomniaQuestWaitingStepData(stepWaitingDescription, getCurrentTime()));
		userData.addItem(MonmusuDropItems.ARACHNE_SILK, -10);

		interaction.createOriginalMessageUpdater()
				.addEmbeds(makeEmbed(type.name, stepWaitingText, MonsterGirlRace.SUCCUBUS.imageLink), //
						userData.addExpAndMakeEmbed(apiUtils, 5000, interaction.getUser(),
								interaction.getServer().get()))//
				.update();
	}

	private static final String time1Text = String.join("\n", //
			description("You come back soon after helping the poor succubus."), //
			dialogue(
					"Oh, it's you~ Nice to see you again, but I don't have anything for you right now, I must work on the business first."));
	private static final String time2Text = String.join("\n", //
			description("You come back a week later."), //
			dialogue(
					"Hello~ the shipments are arriving again, and I didn't go out of business thanks to you! Soon I will surely have your reward ready~"));
	private static final String time3Text = String.join("\n", //
			description("You come back two weeks later."), //
			dialogue(
					"Hi, hi~ I am getting clients again, all thanks to you! I hope you won't mind waiting just a bit more, right~?"));
	private static final String time4Text = String.join("\n", //
			description("You come back three weeks later."), //
			dialogue("Hello~ I'm getting the reward for you~ You will love it!"));
	private static final String time5Text = String.join("\n", //
			description("You come back four weeks later."), //
			dialogue(
					"Ohayou~! It's almost ready, but you need to wait just a bit longer, I want it to be perfect for you~ I heard you have a big harem, so I need to count that in~"));
	private static final String stepFinishedText = String.join("\n", //
			description("You come back five weeks later."), dialogue("Ohayou~! It's there! Tadah!"),
			description(
					"The succubus shows you a set of pillows and bedding that she crafted, with beautiful patterns made of golden strings."),
			dialogue("It's all yours! That's for helping me out before~"),
			description("She happily gives you your reward."));
	private static final String stepFinishedDescription = "Your new friend made you a set of bedding, to use for your house.";

	private void continueWaitingStep(final APIUtils apiUtils, final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		final UserInsomniaQuestWaitingStepData questData = (UserInsomniaQuestWaitingStepData) userData.rpg.quests
				.get(type);
		final long dt = getCurrentTime() - questData.startTime;
		if (dt < time1) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, time1Text, MonsterGirlRace.SUCCUBUS.imageLink)).update();
			return;
		}
		if (dt < time2) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, time2Text, MonsterGirlRace.SUCCUBUS.imageLink)).update();
			return;
		}
		if (dt < time3) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, time3Text, MonsterGirlRace.SUCCUBUS.imageLink)).update();
			return;
		}
		if (dt < time4) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, time4Text, MonsterGirlRace.SUCCUBUS.imageLink)).update();
			return;
		}
		if (dt < time5) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, time5Text, MonsterGirlRace.SUCCUBUS.imageLink)).update();
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		addToIntOnMap(userData.houseFurniture, FurnitureType.SLEEPFUL_BEDDING, 1);

		interaction.createOriginalMessageUpdater()
				.addEmbeds(makeEmbed(type.name, stepFinishedText, MonsterGirlRace.SUCCUBUS.imageLink), //
						userData.addExpAndMakeEmbed(apiUtils, 10000, interaction.getUser(),
								interaction.getServer().get()), //
						makeEmbed("Obtained reward", "You got a set of sleepful bedding for your house!"))//
				.update();
	}

}
