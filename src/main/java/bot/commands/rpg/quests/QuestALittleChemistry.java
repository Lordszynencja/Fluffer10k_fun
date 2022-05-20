package bot.commands.rpg.quests;

import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;

import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.commands.rpg.danuki.DanukiShopUnlock;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.items.data.MonmusuDropItems;
import bot.data.items.data.PotionItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;

public class QuestALittleChemistry extends Quest {
	public QuestALittleChemistry() {
		super(QuestType.A_LITTLE_CHEMISTRY_QUEST, 6);

		continueQuestHandlers.put(QuestStep.GETTING_WERESHEEP_WOOL, this::continueGettingWeresheepWoolStep);
		continueQuestHandlers.put(QuestStep.GETTING_SLIME_JELLY, this::continueGettingSlimeJellyStep);
		continueQuestHandlers.put(QuestStep.GETTING_PRISONER_FRUIT, this::continueGettingPrisonerFruitStep);
	}

	private static final String stepGettingWeresheepWoolText = String.join("\n", //
			description(
					"Walking along the path to the next village, you see a Gyoubu Danuki carrying a rather large sack, sitting on the side of the road. Curious, you investigate and walk up to her."), //
			description(
					"She seemed lost in though until she noticed you and smiled brightly before getting up and showing you her sack."), //
			dialogue("Finally! A customer is here!"), //
			description("You ask what she has for sale."), //
			dialogue("Potions, my dear. Potions galore!"), //
			description(
					"You peer into her sack and see a vast amount of potions, though none of them at all looked familiar to you."), //
			dialogue(
					"Well.. I should say very special potions. Brewed them myself. Can't get them anywhere else though."), //
			description("She seemed a bit desperate however."), //
			description("You ask how much they are getting out a coin bag for her."), //
			dialogue("Oh, no.. no. Gold coins won't do for me. I've got a far better currency than that cheap metal."), //
			description("She explains that she buys fruits and certain items exchanging her own currency."), //
			dialogue("MG's as I like to call them. Monster Gold, a superior alternative."), //
			description(
					"You stare at the coin she shows you, noticing it's just a regular gold coin with the letters 'MG' engraved into it."), //
			dialogue(
					"Seems like you don't have any MG's, so I'll cut you a deal. Bring me some specific items and we can make this work~"), //
			description("You agree and ask for her first item."), //
			dialogue("I'm running low on some Weresheep wool. Bring me some quickly and we can move on from there."));
	private static final String stepGettingWeresheepWoolDescription = String.join("\n", //
			"The Danuki has asked you to give her some Weresheep wool.", //
			"Use the " + bold("/quest continue") + " to give her the items once you found them.");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.GETTING_WERESHEEP_WOOL,
				stepGettingWeresheepWoolDescription, true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder()
				.addEmbed(
						newQuestMessage(stepGettingWeresheepWoolText).setImage(MonsterGirlRace.GYOUBU_DANUKI.imageLink))
				.respond();
	}

	private static final String stepSlimeJellyText = String.join("\n", //
			description("You hand the Danuki the Weresheep wool."), //
			dialogue("Perfect. Now for the next item, I need 3 pieces of slime jelly."));
	private static final String stepSlimeJellyDescription = String.join("\n", //
			"The Danuki needs 3 pieces of slime jelly.", //
			"Use the " + bold("/quest continue") + " to give her the items once you found them.");

	private void continueGettingWeresheepWoolStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		defaultGiveItemStep(interaction, userData, type, MonmusuDropItems.WERESHEEP_WOOL, 1,
				QuestStep.GETTING_SLIME_JELLY, true, stepSlimeJellyText, stepSlimeJellyDescription,
				MonsterGirlRace.GYOUBU_DANUKI.imageLink, 200);
	}

	private static final String stepPrisonerFruitText = String.join("\n", //
			description("You give the Danuki the slime jelly pieces."), //
			dialogue("Let me think.. Ahh! I think some prisoner fruit would be good too. 4 should be enough."));
	private static final String stepPrisonerFruitDescription = String.join("\n", //
			"The Danuki has asked you for 4 prisoner fruits.", //
			"Use the " + bold("/quest continue") + " to give her the items once you found them.");

	private void continueGettingSlimeJellyStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		defaultGiveItemStep(interaction, userData, type, MonmusuDropItems.SLIME_JELLY, 3,
				QuestStep.GETTING_PRISONER_FRUIT, true, stepPrisonerFruitText, stepPrisonerFruitDescription,
				MonsterGirlRace.GYOUBU_DANUKI.imageLink, 400);
	}

	private static final String stepFinishedText = String.join("\n", //
			description("You give the Danuki the prisoner fruits."), //
			description("She seemed distracted for a small while."), //
			dialogue("Need cat repellant.. blasted cat keeps stealing my fruit.."), //
			dialogue("I think that's all for now. I can sure make new things with these~"), //
			dialogue(
					"Should you find any new items that may be of interest to me, come on by and give them. Who knows, maybe I'll have new things to sell~"));
	private static final String stepFinishedDescription = "The Danuki is happy to find a new customer, and offers special items to sell, only for her own currency though.";

	private void continueGettingPrisonerFruitStep(final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		if (!userData.hasItem(MonmusuDropItems.PRISONER_FRUIT, 4)) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed(type.name, "You don't have the necessary item.")).update();
			return;
		}

		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, stepFinishedDescription));
		userData.addItem(MonmusuDropItems.PRISONER_FRUIT, -4);
		userData.danukiShopAvailable = true;
		userData.danukiShopUnlocks.add(DanukiShopUnlock.STRENGTH_1_POTION);
		userData.danukiShopUnlocks.add(DanukiShopUnlock.AGILITY_1_POTION);
		userData.danukiShopUnlocks.add(DanukiShopUnlock.INTELLIGENCE_1_POTION);
		userData.addItem(PotionItems.AGILITY_3_POTION);
		userData.addItem(PotionItems.INTELLIGENCE_3_POTION);
		userData.addItem(PotionItems.STRENGTH_3_POTION);

		interaction.createOriginalMessageUpdater()
				.addEmbeds(makeEmbed(type.name, stepFinishedText, MonsterGirlRace.GYOUBU_DANUKI.imageLink), //
						userData.addExpAndMakeEmbed(600, interaction.getUser(), interaction.getServer().get()), //
						makeEmbed("Obtained reward", "You got a set of potions and unlocked Danuki shop!"))//
				.update();
	}
}
