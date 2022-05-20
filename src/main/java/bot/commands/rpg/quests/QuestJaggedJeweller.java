package bot.commands.rpg.quests;

import static bot.data.MonsterGirls.MonsterGirlRace.AUTOMATON;
import static bot.data.items.data.GemItems.GemRefinement.REFINED;
import static bot.data.items.data.GemItems.GemType.AMETHYST;
import static bot.data.items.data.GemItems.GemType.DIAMOND;
import static bot.data.items.data.GemItems.GemType.EMERALD;
import static bot.data.items.data.GemItems.GemType.GARNET;
import static bot.data.items.data.GemItems.GemType.JADE;
import static bot.data.items.data.GemItems.GemType.OPAL;
import static bot.data.items.data.GemItems.GemType.QUARTZ;
import static bot.data.items.data.GemItems.GemType.RUBY;
import static bot.data.items.data.GemItems.GemType.SAPPHIRE;
import static bot.data.items.data.GemItems.GemType.TOPAZ;
import static bot.data.quests.QuestType.JAGGED_JEWELLER_QUEST;
import static bot.data.quests.QuestType.MINERS_HOME;
import static bot.userData.rpg.questData.QuestStep.FINISHED;
import static bot.userData.rpg.questData.QuestStep.STEP_1;
import static bot.userData.rpg.questData.QuestStep.STEP_2;
import static bot.userData.rpg.questData.QuestStep.STEP_3;
import static bot.userData.rpg.questData.QuestStep.STEP_4;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.CollectionUtils.toSet;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.data.GemItems.GemType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;
import bot.util.pages.messages.PagedPickerMessage.OnPickHandler;

public class QuestJaggedJeweller extends Quest {
	private final Fluffer10kFun fluffer10kFun;

	public QuestJaggedJeweller(final Fluffer10kFun fluffer10kFun) {
		super(JAGGED_JEWELLER_QUEST, 5, MINERS_HOME);

		this.fluffer10kFun = fluffer10kFun;

		continueQuestHandlers.put(STEP_1, this::continueStep1);
		continueQuestHandlers.put(STEP_2, this::continueStep2);
		continueQuestHandlers.put(STEP_3, this::continueStep3);
		continueQuestHandlers.put(STEP_4, this::continueStep4);
	}

	private static final String step1Text = String.join("\n", //
			description(
					"You walk down the street in a city, when you notice some girl doing something weird in her store."), //
			dialogue("That's not good... That doesn't work... This failed..."), //
			description(
					"You walk in and see that she looks all cracked and chipped, and she looks like she tries to repair herself with... glass?"), //
			dialogue("I'm closed until I repair myself."), description("You ask her when will that happen."), //
			dialogue("I can't find any good material to fill the gaps. Gem would help. Do you have any?"), //
			description(
					"She looks up and stares at you. Eventually you break the silence by telling her you will search for some."), //
			dialogue(
					"Don't bring anything expensive, I'm not sure if it will work. But it has to be refined. Amethyst, jade, quartz or topaz should suffice."));
	private static final String step1Description = String.join("\n", //
			"You need to get the girl a refined amethyst, jade, quartz or topaz so she can repair herself.", //
			"Use the " + bold("/quest continue") + " to give her the gem once you found it");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, STEP_1, step1Description, true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder().addEmbed(newQuestMessage(step1Text).setImage(AUTOMATON.imageLink))
				.respond();
	}

	private static EmbedBuilder toEmbed(final ItemAmount itemAmount) {
		return makeEmbed("Give " + itemAmount.item.name + "?", "You have " + itemAmount.amount, AUTOMATON.imageLink);
	}

	private void pickGem(final MessageComponentInteraction interaction, final ServerUserData userData,
			final Set<GemType> goodTypes, final OnPickHandler<ItemAmount> onPick) {
		final List<ItemAmount> fittingGems = new ArrayList<>();
		for (final Entry<String, Long> itemAmount : userData.items.entrySet()) {
			if (itemAmount.getValue() > 0) {
				final Item item = fluffer10kFun.items.getItem(itemAmount.getKey());
				if (item.gemRefinement == REFINED && goodTypes.contains(item.gemType)) {
					fittingGems.add(new ItemAmount(item, itemAmount.getValue()));
				}
			}
		}

		if (fittingGems.isEmpty()) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, "You don't have any fitting gem."))
					.update();
			return;
		}
		fittingGems.sort(null);
		final List<EmbedField> fields = mapToList(fittingGems, itemAmount -> itemAmount.getAsField());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick gem", 5, fields, fittingGems)//
				.description("it will be given to the girl")//
				.imgUrl(AUTOMATON.imageLink)//
				.dataToEmbed(QuestJaggedJeweller::toEmbed)//
				.onConfirm(onPick)//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, interaction);
	}

	private void defaultOnPick(final MessageComponentInteraction interaction, final ItemAmount itemAmount,
			final QuestStep currentStep, final QuestStep nextStep, final String stepText, final String stepDescription,
			final long exp) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		if (!userData.rpg.questIsOnStep(type, currentStep)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, "You already did this step."))
					.update();
			return;
		}

		defaultGiveItemStep(interaction, userData, type, itemAmount.item.id, 1, nextStep, true, stepText,
				stepDescription, AUTOMATON.imageLink, exp);
	}

	private static final Set<GemType> step1Gems = toSet(AMETHYST, JADE, QUARTZ, TOPAZ);

	private void continueStep1(final MessageComponentInteraction interaction, final ServerUserData userData) {
		pickGem(interaction, userData, step1Gems, this::onPickStep1);
	}

	private static final String step2Text = String.join("\n", //
			description("You hand the automaton a gem. She fiddles with it and then tries to use it for repair."), //
			dialogue("Hmm... it worked a bit, but it's too weak, I need something better. Maybe a garnet or an opal?"));
	private static final String step2Description = String.join("\n", //
			"You need to get the girl a refined garnet or opal.", //
			"Use the " + bold("/quest continue") + " to give her the gem once you found it");

	private void onPickStep1(final MessageComponentInteraction interaction, final int page,
			final ItemAmount itemAmount) {
		defaultOnPick(interaction, itemAmount, STEP_1, STEP_2, step2Text, step2Description, 400);
	}

	private static final Set<GemType> step2Gems = toSet(GARNET, OPAL);

	private void continueStep2(final MessageComponentInteraction interaction, final ServerUserData userData) {
		pickGem(interaction, userData, step2Gems, this::onPickStep2);
	}

	private static final String step3Text = String.join("\n", //
			description("You hand over the gem. She tries to use it, but after some time she stops."), //
			dialogue(
					"It's better, but still not enough. Emeralds, rubies and sapphires are stronger, but I couldn't ask you to get one of those for me, they are a bit expensive."), //
			description("You say that it's fine to which she nods."), //
			dialogue("Thank you. I really hope that will be enough."));
	private static final String step3Description = String.join("\n", //
			"You need to get the girl a refined emerald, ruby or sapphire.", //
			"Use the " + bold("/quest continue") + " to give her the gem once you found it");

	private void onPickStep2(final MessageComponentInteraction interaction, final int page,
			final ItemAmount itemAmount) {
		defaultOnPick(interaction, itemAmount, STEP_2, STEP_3, step3Text, step3Description, 600);
	}

	private static final Set<GemType> step3Gems = toSet(EMERALD, RUBY, SAPPHIRE);

	private void continueStep3(final MessageComponentInteraction interaction, final ServerUserData userData) {
		pickGem(interaction, userData, step3Gems, this::onPickStep3);
	}

	private static final String step4Text = String.join("\n", //
			description("Once again the girl gets gem and starts working on her."), //
			dialogue(
					"Sigh... That wasn't enough... Would you please bring me a diamond? I hoped something else could replace it, but it doesn't seem so."), //
			description("You agree and set out in search for a diamond."));
	private static final String step4Description = String.join("\n", //
			"You need to get the girl a refined diamond.", //
			"Use the " + bold("/quest continue") + " to give her the gem once you found it");

	private void onPickStep3(final MessageComponentInteraction interaction, final int page,
			final ItemAmount itemAmount) {
		defaultOnPick(interaction, itemAmount, STEP_3, STEP_4, step4Text, step4Description, 1000);
	}

	private static final Set<GemType> step4Gems = toSet(DIAMOND);

	private void continueStep4(final MessageComponentInteraction interaction, final ServerUserData userData) {
		pickGem(interaction, userData, step4Gems, this::onPickStep4);
	}

	private static final String stepFinishedText = String.join("\n", //
			description(
					"The girl grabs the diamond from your hands and fits it in the cracks, filling them fully, then moves a bit to test it."), //
			dialogue(
					"That works. Sorry for all the trouble. I'll pay you a bit for the trouble, and if you want, I could take care of your gems by combining and refining them. It does look pretty though."), //
			description("She looks at the filling and how it reflects the light."), //
			dialogue("Now, how may I be of service?"));
	private static final String stepFinishedDescription = "You helped the jeweller girl and she offered you her services.";

	private void onPickStep4(final MessageComponentInteraction interaction, final int page,
			final ItemAmount itemAmount) {
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				interaction.getUser());
		if (!userData.rpg.questIsOnStep(type, STEP_4)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, "You already did this step."))
					.update();
			return;
		}
		if (!userData.hasItem(itemAmount.item)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You don't have that item anymore.")).update();
			return;
		}

		userData.addItem(itemAmount.item, -1);
		userData.monies += 2000;
		userData.jewellerAvailable = true;

		userData.rpg.setQuest(new UserQuestData(type, FINISHED, stepFinishedDescription));

		interaction.createOriginalMessageUpdater()
				.addEmbeds(makeEmbed(type.name, stepFinishedText, AUTOMATON.imageLink), //
						userData.addExpAndMakeEmbed(2500, interaction.getUser(), interaction.getServer().get()), //
						makeEmbed("Quest reward", "You got 2000 gold and unlocked jeweller!"))//
				.update();
	}
}
