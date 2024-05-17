package bot.commands.rpg.quests;

import static bot.util.CollectionUtils.mapToSet;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.Utils.bold;
import static bot.util.Utils.fixString;
import static bot.util.apis.MessageUtils.getServerTextChannel;
import static java.util.Arrays.asList;

import java.util.HashSet;
import java.util.Set;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.QuestItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;
import bot.userData.rpg.questData.UserSphinxQuestionsQuestAnsweringQuestionsStepData;
import bot.util.Utils;
import bot.util.apis.MessageUtils;

public class QuestSphinxQuestions extends Quest {
	public enum SphinxQuestion {
		DANCING_DRAGON("My dance you will see, and you won't get free, who am I?", //
				"otohime"), //
		DANGEROUS_SOUVENIR("What souvenir makes its owner feel old and long for the place he left?", //
				"tamate- bako", "tamate-bako"), //
		DEMON_REAL_SILVER("What metal would you use to make a magic item?", //
				"demon realm silver", "realmsilver"), //
		DOUBLE_FRUIT("Which fruit is always double?", //
				"couple's fruit"), //
		EARTH_DRAGON("My sisters fly and swim, while I stay underground, who am I?", //
				"wurm"), //
		EDIBLE_RACE("Which monster is tasty?", //
				"slime"), //
		GHOUL_MOUTH("I will please you with my mouth, but if you try to please me, I'll go insane, who am I?", //
				"ghoul"), //
		GROWING_SLEEP("Who grows sleep?", //
				"weresheep"), //
		INFILTRATOR("Who infiltrates ant hives?", //
				"ant arachne"), //
		LILIM_ORB("What is Lilim sitting on?", //
				"mamono", "mamono mana", "solidified mamono mana"), //
		MANA_CAGE("What is used to transport mana?", //
				"mana cage"), //
		MONSTER_LORD_DAUGHTERS("How are the daughters of Monster Lord called?", //
				"lilim"), //
		MONSTER_THREE("Which monster is three?", //
				"kamaitachi"), //
		MORE_THAN_ONE("I am one body and I am many races, who am I?", //
				"chimaera"), //
		MOST_AMAZING("Which monster is said to have the most amazing genitalia?", //
				"baphomet"), //
		MOST_SUBSPECIES("Which monster race is the has more subspecies than any other?", //
				"succubus", "succubi"), //
		PHANTOM_LOVE("Whose love is a tragedy?", //
				"phantom"), //
		PRISONER_FRUIT("What is said to imprison those who use it?", //
				"prisoner fruit"), //
		REPRESENTATIVE("Which monster race represents monsters the best?", //
				"succubus", "succubi"), //
		SHOCKINGLY_WET("If I'm wet, I'm also shockingly dangerous, who am I?", //
				"raiju"), //
		SPHINX("If you look around, you see me, who am I?", //
				"sphinx"), //
		SWIMMING_DRAGON("Which dragon is the best swimmer?", //
				"otohime"), //
		SUCCUBUS_ENERGY("Which race is connected to every other?", //
				"succubus", "succubi"), //
		THE_STRONGEST("I am the strongest, who am I?", //
				"baphomet"), //
		WONDERLAND_CREATOR("Who created a wonderful dimension?", //
				"queen of hearts"), //
		WONDROUS_DRAGON("Who is a wondrous dragon?", //
				"jabberwock");

		private static SphinxQuestion pickNext(final Set<SphinxQuestion> answered) {
			SphinxQuestion question = getRandom(values());
			while (answered.contains(question)) {
				question = getRandom(values());
			}
			return question;
		}

		public final String question;
		public final Set<String> answers;

		private SphinxQuestion(final String question, final String... answers) {
			this.question = question;
			this.answers = mapToSet(asList(answers), Utils::fixString);
		}

	}

	private final Fluffer10kFun fluffer10kFun;

	public QuestSphinxQuestions(final Fluffer10kFun fluffer10kFun) {
		super(QuestType.SPHINX_QUESTIONS_QUEST, 9);

		this.fluffer10kFun = fluffer10kFun;

		continueQuestHandlers.put(QuestStep.ANSWERING_QUESTIONS, this::continueAnsweringQuestionsStep);
	}

	private static final String stepAnsweringQuestionsText = String.join("\n", //
			description(
					"You are walking through the desert as you notice some kind of a tomb. Thinking you could find something valuable in there, you come closer, and you are surprised by a Sphinx tapping you on the shoulder."), //
			dialogue("Looking for the treasure, aren't you~?"), //
			description("You don't know what to answer, so you just stay quiet."), //
			dialogue("Well I could tell you where it is. I-f you answer my questions that is!"), //
			description(
					"She grips your shoulder tightly, holding you in place. It seems like you don't have a choice."), //
			dialogue("Hmm, let me think of one... Oh, I know! %1$s"), //
			description("You start thinking about the answer as she grins."), //
			dialogue("Now don't answer wrong, or you will get punished~"));
	private static final String stepAnsweringQuestionsDescription = String.join("\n", //
			"Answer the Sphinx's question!", //
			dialogue("%1$s"), //
			"Use the " + bold("/quest continue") + " to answer the question.");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final SphinxQuestion question = SphinxQuestion.pickNext(new HashSet<>());

		final UserSphinxQuestionsQuestAnsweringQuestionsStepData questData = new UserSphinxQuestionsQuestAnsweringQuestionsStepData(
				String.format(stepAnsweringQuestionsDescription, question.question), question);
		userData.rpg.setQuest(questData);

		final String text = String.format(stepAnsweringQuestionsText, question.question);
		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage(text).setImage(MonsterGirlRace.SPHINX.imageLink)).respond();
	}

	private static final String askingText = String.join("\n", //
			dialogue("Answer my question, mortal."), //
			dialogue("%1$s"));

	private void continueAnsweringQuestionsStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		final UserSphinxQuestionsQuestAnsweringQuestionsStepData questData = (UserSphinxQuestionsQuestAnsweringQuestionsStepData) userData.rpg.quests
				.get(type);
		if (System.currentTimeMillis() <= questData.loveEndTime) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You are still making love to the Sphinx!")).update();
			return;
		}

		final String text = String.format(askingText, questData.currentQuestion.question);
		interaction.createOriginalMessageUpdater()
				.addEmbed(
						makeEmbed(type.name, text, MonsterGirlRace.SPHINX.imageLink).setFooter("Use /answer to answer"))
				.update();
		fluffer10kFun.commandAnswer.addAnswerHandler(MessageUtils.getServerTextChannel(interaction).getId(),
				interaction.getUser().getId(), this::onAnswer);
	}

	private static final String incorrectAnswerText = String.join("\n", //
			dialogue("%1$s") + " - " + description("you answer."), //
			description("Sphinx grins as she listens to your answer."), //
			dialogue("Incorrect~"), //
			description("suddenly, she jumps on you and makes love to you!"), //
			dialogue("You will have another chance after I'm done~."));
	private static final String correctAnswerText = String.join("\n", //
			dialogue("%1$s") + " - " + description("you answer correctly."), //
			description("Sphinx thinks for a moment."), //
			dialogue("Yes, that seems right. That was an easy one though!"), //
			description("She thinks for a bit more before asking next question."), //
			dialogue("%2$s"));
	private static final String lastQuestionText = String.join("\n", //
			dialogue("%1$s") + " - " + description("you answer correctly again."), //
			dialogue("That's right. Hmm, I will ask final question then."), //
			dialogue("%2$s"));
	private static final String stepFinishedText = String.join("\n", //
			dialogue("%1$s") + " - " + description("you answer to the last question."), //
			description("Sphinx sighs."), //
			dialogue(
					"Yes, this is correct too... Damn you, explorers, getting smarter every day! Well, come with me and I'll show you the treasure. To be honest it's not that big. But there was one mage that gave me a golden ring, I guess you can have it too."));
	private static final String stepFinishedDescription = "Sphinx let you take some things from the tomb, which is almost empty anyway, including golden ring with initials of the weird mage on it.";

	private void onAnswer(final SlashCommandInteraction interaction, final String answer) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());

		final UserSphinxQuestionsQuestAnsweringQuestionsStepData questData = (UserSphinxQuestionsQuestAnsweringQuestionsStepData) userData.rpg.quests
				.get(type);

		final String answerFixed = fixString(answer);
		if (!questData.currentQuestion.answers.contains(answerFixed)) {
			final int cums = getRandomInt(15, 30);
			questData.loveEndTime = System.currentTimeMillis() + cums * 60 * 1000;

			final EmbedBuilder embed = makeEmbed(type.name, String.format(incorrectAnswerText, answer),
					MonsterGirlRace.SPHINX.imageLink);
			fluffer10kFun.commandMgLove.addCums(interaction.getServer().get(), interaction.getUser(), cums);
			final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(interaction.getServer().get(),
					interaction.getUser(), cums, "Sphinx");

			interaction.createImmediateResponder().addEmbeds(embed, loveEmbed).respond();
			return;
		}

		questData.answeredQuestions.add(questData.currentQuestion);
		if (questData.answeredQuestions.size() < 3) {
			questData.currentQuestion = SphinxQuestion.pickNext(questData.answeredQuestions);
			questData.description = String.format(stepAnsweringQuestionsDescription,
					questData.currentQuestion.question);

			final String textFormat = questData.answeredQuestions.size() == 1 ? correctAnswerText : lastQuestionText;
			final String text = String.format(textFormat, answer, questData.currentQuestion.question);
			interaction.createImmediateResponder()
					.addEmbeds(makeEmbed(type.name, text, MonsterGirlRace.SPHINX.imageLink), //
							userData.addExpAndMakeEmbed(1_000, interaction.getUser(), interaction.getServer().get()))
					.respond();

			fluffer10kFun.commandAnswer.addAnswerHandler(getServerTextChannel(interaction).getId(),
					interaction.getUser().getId(), this::onAnswer);
			return;
		}

		userData.monies += 5_000;
		userData.addItem(QuestItems.WEIRD_MAGE_RING);
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));

		final String text = String.format(stepFinishedText, answer);
		interaction.createImmediateResponder().addEmbeds(makeEmbed(type.name, text, MonsterGirlRace.SPHINX.imageLink), //
				userData.addExpAndMakeEmbed(5_000, interaction.getUser(), interaction.getServer().get()), //
				makeEmbed("Obtained reward", "You got a huge pile of gold and a golden ring with initials!"))//
				.respond();
	}
}
