package bot.commands.rpg.quests;

import static bot.data.items.ItemUtils.formatNumber;
import static bot.util.CollectionUtils.mapToList;
import static bot.util.CollectionUtils.toSet;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;
import static bot.util.modularPrompt.ModularPromptButton.button;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.blacksmith.tasks.BlacksmithTier;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.ItemUtils;
import bot.data.items.data.OreItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserBusinessAsUsual2QuestGatheringResourcesStepData;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.EmbedUtils.EmbedField;
import bot.util.modularPrompt.ModularPrompt;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;

public class QuestBusinessAsUsual2 extends Quest {
	public static final long startingValue = 4_000;

	private final Fluffer10kFun fluffer10kFun;

	public QuestBusinessAsUsual2(final Fluffer10kFun fluffer10kFun) {
		super(QuestType.BUSINESS_AS_USUAL_2_QUEST, 8, QuestType.BUSINESS_AS_USUAL_1_QUEST);

		this.fluffer10kFun = fluffer10kFun;

		continueQuestHandlers.put(QuestStep.GATHERING_RESOURCES, this::continueGatheringResourcesStep);
	}

	private static final String stepGatheringResourcesText = String.join("\n", //
			description("You visit your favorite blacksmith, and she is excited to tell you her new plans."), //
			dialogue(
					"I'm expanding! I'll get new tools and some machines to work with better materials! Now, what I need is coal, copper and iron to upgrade some things. Give me some and I will be able to make new things for you!"));
	private static final String stepGatheringResourcesDescription = String.join("\n", //
			"The blacksmith asked you to get her some materials, namely coal, copper and iron. Value needed: %1$s.", //
			"Use " + bold("/quest continue") + " to give her the items once you gather them.");

	private String getStepDescription(final long value) {
		return String.format(stepGatheringResourcesDescription, ItemUtils.formatNumber(value));
	}

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserBusinessAsUsual2QuestGatheringResourcesStepData(
				getStepDescription(startingValue));
		userData.rpg.setQuest(questData);

		interaction.createImmediateResponder()
				.addEmbed(newQuestMessage(stepGatheringResourcesText).setImage(MonsterGirlRace.DWARF.imageLink))
				.respond();
	}

	private static final Set<String> acceptedItems = toSet(//
			OreItems.ORE_COAL, //
			OreItems.ORE_COPPER, //
			OreItems.ORE_IRON);

	public void continueGatheringResourcesStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		final List<ItemAmount> items = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0 && acceptedItems.contains(entry.getKey()))//
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.collect(toList());
		final List<EmbedField> fields = mapToList(items, item -> item.getAsFieldWithPrice());

		final PagedMessage message = new PagedPickerMessageBuilder<>("Pick resource to give", 5, fields, items)//
				.imgUrl(MonsterGirlRace.DWARF.imageLink)//
				.onPick((interaction2, page, itemAmount) -> onOrePick(interaction2, userData, itemAmount))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(message, interaction);
	}

	private void onOrePick(final MessageComponentInteraction interaction, final ServerUserData userData,
			final ItemAmount itemAmount) {
		final EmbedBuilder embed = makeEmbed("How many of " + itemAmount.item.namePlural + " do you want to give?");
		final ModularPrompt prompt = new ModularPrompt(embed);

		final List<Long> amounts = asList(1L, 2L, 5L, 10L, 20L, 50L);
		for (final long amount : amounts) {
			if (itemAmount.amount >= amount) {
				prompt.addButton(button(formatNumber(amount), ButtonStyle.PRIMARY,
						interaction2 -> onAmountPick(interaction2, userData, itemAmount.item, amount)));
			}
		}
		prompt.addButton(button("All", ButtonStyle.PRIMARY,
				interaction2 -> onAmountPick(interaction2, userData, itemAmount.item, itemAmount.amount)));

		fluffer10kFun.modularPromptUtils.addMessage(prompt, interaction);
	}

	private static final String stepFinishedText = String.join("\n", //
			description("The dwarf finally finished upgrading her forge."),
			dialogue("It's finally ready. Let's try to make something better now, shall we?"));
	private static final String stepFinishedDescription = "You helped the dwarf to expand her forge.";

	private void onAmountPick(final MessageComponentInteraction interaction, final ServerUserData userData,
			final Item item, final long amount) {

		if (!userData.hasItem(item.id, amount)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("You don't have this item anymore!", null, MonsterGirlRace.DWARF.imageLink))
					.update();
			return;
		}

		final UserBusinessAsUsual2QuestGatheringResourcesStepData quest = (UserBusinessAsUsual2QuestGatheringResourcesStepData) userData.rpg.quests
				.get(type);
		userData.addItem(item, -amount);
		quest.resourcesLeft -= item.price * amount;

		if (quest.resourcesLeft > 0) {
			quest.description = getStepDescription(quest.resourcesLeft);
			interaction.createOriginalMessageUpdater().addEmbed(
					makeEmbed("Thank you for the donation!", " I need more though.", MonsterGirlRace.DWARF.imageLink))
					.update();
			return;
		}

		boolean unlocked = false;
		if (!userData.blacksmith.tiersUnlocked.contains(BlacksmithTier.TIER_2)) {
			unlocked = true;
			userData.blacksmith.tiersUnlocked.add(BlacksmithTier.TIER_2);
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));

		final List<EmbedBuilder> embeds = new ArrayList<>(
				asList(makeEmbed(type.name, stepFinishedText, MonsterGirlRace.DWARF.imageLink), //
						userData.addExpAndMakeEmbed(10_000, interaction.getUser(), interaction.getServer().get())));

		if (unlocked) {
			embeds.add(makeEmbed("Unlocked tier 2 crafting!"));
		}

		interaction.createOriginalMessageUpdater().addEmbeds(embeds).update();
	}
}
