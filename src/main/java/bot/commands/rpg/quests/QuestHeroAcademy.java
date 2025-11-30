package bot.commands.rpg.quests;

import static bot.data.items.data.WeaponItems.getApprenticeStaffId;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;
import static bot.util.Utils.joinNames;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.getServer;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.ButtonStyle;
import org.javacord.api.entity.message.component.SelectMenu;
import org.javacord.api.entity.message.component.SelectMenuOption;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;
import org.javacord.api.interaction.callback.InteractionImmediateResponseBuilder;

import bot.Fluffer10kFun;
import bot.data.items.data.GemItems.GemType;
import bot.data.items.data.MagicScrollItems;
import bot.data.items.data.PotionItems;
import bot.data.items.data.WeaponItems;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserHeroAcademyQuestRogue2StepData;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.apis.APIUtils;

public class QuestHeroAcademy extends Quest {
	private static final long price = 2000;

	private final Fluffer10kFun fluffer10kFun;

	public QuestHeroAcademy(final Fluffer10kFun fluffer10kFun) {
		super(QuestType.HERO_ACADEMY_QUEST, 0);
		this.fluffer10kFun = fluffer10kFun;

		continueQuestHandlers.put(QuestStep.CHOOSE_CLASS, this::continueChooseClassStep);

		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("q_ha_class_remove", this::onRemove);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("q_ha_class", this::onClassPicked);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("q_ha_class_accept", this::onClassAccept);
		fluffer10kFun.apiUtils.commandHandlers.addMessageComponentHandler("q_ha_class_back", this::onClassBack);
	}

	private static final String chooseClassText = "You come back to the school for adventurers and continue pondering on which path to choose.";

	private static List<SelectMenuOption> classOptions = asList(HeroClass.values()).stream()
			.map(c -> SelectMenuOption.create(c.name, c.name())).collect(toList());

	private void addChooseClassEmbed(final MessageComponentInteraction interaction) {
		final SelectMenu select = SelectMenu.createStringMenu("q_ha_class " + interaction.getUser().getId(),
				classOptions);

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, chooseClassText))//
				.addComponents(ActionRow.of(select), //
						ActionRow.of(Button.create("q_ha_class_remove", ButtonStyle.DANGER, "Go away")))//
				.update();
	}

	private void continueChooseClassStep(final APIUtils apiUtils, final MessageComponentInteraction interaction,
			final ServerUserData userData) {
		addChooseClassEmbed(interaction);
	}

	private static final String startStepText = String.join("\n", //
			description("While walking through the town, you hear someone shouting loudly in front of the school."), //
			dialogue("Join today! become hero tomorrow!"), //
			description("Intrigued, you come closer to the man."), //
			dialogue(
					"Aha! Young man, I see a spark of adventure in your eyes! Don't you want to travel the world and help everyone around?"), //
			description("You nod and confirm."), //
			dialogue(
					"Marvelous! Right here, in our Hero Academy, you can learn how to do just that! For a small fee of "
							+ price + " gold we will teach you how to fight monsters and keep yourself alive!"), //
			description("You quiver when you hear the price, and start thinking about it."), //
			dialogue(
					"Of course, after finishing the course you get to keep the weapons, and you can never lose the experience! So, if you are ready to see the big world, pay the fee and choose one of our classes!"), //
			description("The man smiles at you warmly as you tell him you will think about it."), //
			dialogue(
					"Of course, take your time, but don't hesitate too long or someone else will take all the glory! Ha ha ha!"), //
			description("You walk away thinking which class to take"));
	private static final String startStepDescription = String.join("\n", //
			"Choose your path as a future hero! (for " + price + " gold coins, results may vary)", //
			"Use the " + bold("/quest continue") + " to choose your class");

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final UserQuestData questData = new UserQuestData(type, QuestStep.CHOOSE_CLASS, startStepDescription, true);
		userData.rpg.setQuest(questData);
		interaction.createImmediateResponder().addEmbed(newQuestMessage(startStepText)).respond();
	}

	private enum HeroClass {
		BERSERKER("Berserker", //
				dialogue(
						"A powerful berserker? I once seen one with axe so big, it could probably cut a dragon in half!")
						+ "\n\n"//
						+ dialogue("You will get an axe and a strength potion when your training is finished.")), //
		MAGE("Mage", //
				dialogue(
						"A magnificent mage? Though they seem weak, I've seen the most powerful of them destroy whole enemy units with just one spell!")
						+ "\n\n"//
						+ dialogue("You will learn basic spells and receive a mana potion during the training.")), //
		PALADIN("Paladin", //
				dialogue(
						"A holy paladin? Everyone's heart warms up when a paladin shows up, with his huge shield and holy healing for the wounded.")
						+ "\n\n"//
						+ dialogue("You will receive a sword and a shield.")), //
		RANGER("Ranger", //
				dialogue(
						"A nimble ranger? Shooting your enemies from far away and dodging every attack targeted at you, doesn't that seem good?")//
						+ "\n\n"//
						+ dialogue("You will get a bow during the training")), //
		ROGUE("Rogue", //
				dialogue(
						"A rogue lurking in the shadows? While I don't really like the idea myself, we have classes for those who want to strike the enemies in their vital points and end them quickly.")//
						+ "\n\n"//
						+ dialogue("You will get a knife and a potion")), //
		WARLOCK("Warlock", //
				dialogue(
						"A warlock? Casting spells that no mage would be ashamed of, while being able to match most fighters in close quarters.")//
						+ "\n\n"//
						+ dialogue("You will get a staff and a spell"));

		public final String name;
		public final String description;

		private HeroClass(final String name, final String description) {
			this.name = name;
			this.description = description;
		}
	}

	private static final String notEnoughFundsText = dialogue(
			"I'm sorry young man, but you need a bit more money to start your adventure. Maybe you could work somewhere in the city?");

	private final Map<HeroClass, BiConsumer<MessageComponentInteraction, ServerUserData>> firstStepHandlers = toMap(//
			pair(HeroClass.BERSERKER, this::chooseClassBerserker), //
			pair(HeroClass.MAGE, this::chooseClassMage), //
			pair(HeroClass.PALADIN, this::chooseClassPaladin), //
			pair(HeroClass.RANGER, this::chooseClassRanger), //
			pair(HeroClass.ROGUE, this::chooseClassRogue), //
			pair(HeroClass.WARLOCK, this::chooseClassWarlock));

	private void onClassAccept(final MessageComponentInteraction interaction) {
		final User user = interaction.getUser();
		final String[] tokens = interaction.getCustomId().split(" ");
		final long userId = Long.valueOf(tokens[1]);
		if (userId != user.getId()) {
			sendEphemeralMessage(interaction, "Not your choice");
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				userId);
		if (userData.monies < price) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, notEnoughFundsText)).update();
			return;
		}
		userData.monies -= price;

		final HeroClass c = HeroClass.valueOf(tokens[2]);
		firstStepHandlers.get(c).accept(interaction, userData);
	}

	private void onClassBack(final MessageComponentInteraction interaction) {
		final User user = interaction.getUser();
		final long userId = Long.valueOf(interaction.getCustomId().split(" ")[1]);
		if (userId != user.getId()) {
			sendEphemeralMessage(interaction, "Not your choice");
			return;
		}

		addChooseClassEmbed(interaction);
	}

	private void onClassPicked(final MessageComponentInteraction interaction) {
		final User user = interaction.getUser();
		final long userId = Long.valueOf(interaction.getCustomId().split(" ")[1]);
		if (userId != user.getId()) {
			sendEphemeralMessage(interaction, "Not your choice");
			return;
		}

		final HeroClass c = HeroClass
				.valueOf(interaction.asSelectMenuInteraction().get().getChosenOptions().get(0).getValue());

		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, c.description))//
				.addComponents(ActionRow.of(
						Button.create("q_ha_class_accept " + userId + " " + c.name(), ButtonStyle.SUCCESS, "Accept"), //
						Button.create("q_ha_class_back " + userId, ButtonStyle.DANGER, "Back")))//
				.update();
	}

	private static final String removeText = description(
			"You tell the man that you will think about the options, and then come back to roaming the streets.");

	private void onRemove(final MessageComponentInteraction interaction) {
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, removeText)).update();
	}

	private static final String berserkerStep1Text = String.join("\n", dialogue(
			"A berserker? Well... of course, we will teach you everything you need, but you will have to work hard to become strong enough to wield something proper."), //
			description(
					"After paying the fee the man leads you inside where you meet a half-naked, muscular man carrying a huge axe."), //
			dialogue(
					"This is Rudy, he will show you basics of being a berserker. Just don't lose hope when he will train you, haha!"), //
			description(
					"The man goes back to his position near the entry, as Rudy approaches you, now towering over you like a mountain."), //
			dialogue("You? Want to be a berserker? Hah, I will have to work really hard with you!"), //
			description("He tightens his muscles and shows you his biceps."), //
			dialogue("See this? That is what you need. Now go and show me you can pack a punch."));
	private static final String berserkerStep1Description = "As a berserker, your first task is to hit any enemy for at least 2 points of damage.";

	private void chooseClassBerserker(final MessageComponentInteraction interaction, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.BERSERKER_1, berserkerStep1Description));
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, berserkerStep1Text)).update();
	}

	private static final String berserker2StepText = String.join("\n", dialogue("Good, good, that was a good hit!"), //
			description("Rudy cheers you."), //
			dialogue("I want you to fight some more, until you gain some strength, and then come back to me."));
	private static final String berserker2StepDescription = "Rudy wants you to improve your strength.";

	public void continueBerserker1Step(final TextChannel channel, final ServerUserData userData, final User user) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.BERSERKER_2, berserker2StepDescription));
		channel.sendMessage(makeEmbed(type.name, berserker2StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));

		if (userData.rpg.strength >= 2) {
			continueBerserker2Step(channel, userData, user);
		}
	}

	private static final String berserker3StepText = dialogue(
			"Oh yes, look at this, this is a muscle growing if I've ever seen one! Now go and show me you can use it.");
	private static final String berserker3StepDescription = "Rudy wants you to deal at least 3 damage with a powerful hit.";

	public void continueBerserker2Step(final TextChannel channel, final ServerUserData userData, final User user) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.BERSERKER_3, berserker3StepDescription));
		channel.sendMessage(makeEmbed(type.name, berserker3StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user,
						channel.asServerChannel().map(c -> c.getServer()).orElse(null)));
	}

	public void continueBerserker2Step(final InteractionImmediateResponseBuilder responder,
			final ServerUserData userData, final Server server, final User user) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.BERSERKER_3, berserker3StepDescription));
		responder.addEmbed(makeEmbed(type.name, berserker3StepText))//
				.addEmbed(userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, server));
	}

	private static final String berserker4StepText = String.join("\n",
			dialogue("Yes! That's it, do you feel that power now?"), //
			description("Rudy is shouting at you ecstatically."), //
			dialogue("Feel that power, brother! And release it all at your enemy! Whew!"));
	private static final String berserker4StepDescription = "Finish the fight.";

	public void continueBerserker3Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.BERSERKER_4, berserker4StepDescription));
		channel.sendMessage(makeEmbed(type.name, berserker4StepText));
	}

	private static final String berserkerFinishedStepText = dialogue(
			"What a fight! Here, this is for you. Use it well, and tell me if you find something better, I will want to swing it.");
	private static final String berserkerFinishedStepDescription = "Rudy is very happy that you've become stronger.";

	public void continueBerserker4Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, berserkerFinishedStepDescription));

		final String itemNames = joinNames(asList(WeaponItems.LUMBERJACK_AXE, PotionItems.STRENGTH_1_POTION).stream()//
				.peek(userData::addItem)//
				.map(fluffer10kFun.items::getName)//
				.collect(toList()));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, berserkerFinishedStepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 500, user, getServer(channel)), //
				makeEmbed("Obtained items", "You got " + itemNames));
	}

	private static final String mageStep1Text = String.join("\n",
			dialogue("A powerful mage? Well, let's just hope you won't get weaker than you are now, ha ha!"), //
			description(
					"After paying the fee the man leads you inside where you meet a simple looking guy reading a book."), //
			dialogue("This is Jenkins, our mage. Jenkins? Hey, Jenkins, you have new student!"), //
			description("Mage looks like he's ignoring you, but eventually he speaks."), //
			dialogue("I'll take care of him."), //
			description("The man that brought you here returns to the entry while Jenkins continues to read the book."), //
			dialogue(
					"Do you have any magic abilities? Actually, let's just see if you can cast a simple spell, here, learn this one for now. That one is always useful in a fight."), //
			description(
					"He hands you the scroll he grabbed from the nearby drawer, while his book levitates in place."));
	private static final String mageStep1Description = "As a mage apprentice, your first task is to cast any spell.";

	private void chooseClassMage(final MessageComponentInteraction interaction, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.MAGE_1, mageStep1Description));
		userData.addItem(MagicScrollItems.MAGIC_SCROLL_HEAL);
		interaction.createOriginalMessageUpdater().addEmbeds(makeEmbed(type.name, mageStep1Text), //
				makeEmbed("Obtained item", "You got " + fluffer10kFun.items.getName("MAGIC_SCROLL_HEAL", 1))).update();
	}

	private static final String mage2StepText = dialogue(
			"Ok, you can cast spells, now do that until you are no longer able to.");
	private static final String mage2StepDescription = "Jenkins wants you to see what happens when you run out of mana.";

	public void continueMage1Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.MAGE_2, mage2StepDescription));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, mage2StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));
	}

	private static final String mage3StepText = dialogue(
			"As you can feel, you aren't able to cast any spells when you are out of mana. But it regenerates so you should be able to use your spells once you wait a bit. Wait and see.");
	private static final String mage3StepDescription = "Jenkins waits for you to regen some mana.";

	public void continueMage2Step(final TextChannel channel, final ServerUserData userData, final User user) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.MAGE_3, mage3StepDescription));

		channel.sendMessage(makeEmbed(type.name, mage3StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));
	}

	private static final String mage4StepText = dialogue(
			"See? You can't generate more than your power allows though. There are magic items and gems that can increase your magic power and the amount of mana you can store. Now you know how to use it. Talk to me after the fight.");
	private static final String mage4StepDescription = "You need to finish the fight to get reward.";

	public void continueMage3Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.MAGE_4, mage4StepDescription));

		channel.sendMessage(makeEmbed(type.name, mage4StepText));
	}

	private static final String mageFinishedStepText = dialogue(
			"I guess I didn't teach you many spells yet, but most of them need a lot of magic power just to use, so for now use this. And have a mana potion too.");
	private static final String mageFinishedStepDescription = "Jenkins is satisfied that you know how to use mana properly.";

	public void continueMage4Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, mageFinishedStepDescription));

		final String itemNames = joinNames(
				asList(MagicScrollItems.MAGIC_SCROLL_FORCE_HIT, PotionItems.MANA_POTION).stream()//
						.peek(userData::addItem)//
						.map(fluffer10kFun.items::getName)//
						.collect(toList()));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, mageFinishedStepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 500, user, getServer(channel)), //
				makeEmbed("Obtained items", "You got " + itemNames + "!"));
	}

	private static final String paladinStep1Text = String.join("\n",
			dialogue("A paladin? That's great! Never enough of the holy fighters."), //
			description(
					"After paying the fee the man leads you inside where you meet a strong and friendly man carrying sword and shield on his back."), //
			dialogue("This is Roland, our protector. I'm sure you will get along nicely."), //
			description("The man goes back to the entrance as Roland looks at you and smiles warmly."), //
			dialogue("Well, hello, my friend! I hope that you are in good health?"), //
			description("His charisma is striking when he starts his explanations."), //
			dialogue(
					"Paladin usually carries a sword, but his real weapon is a shield, with which it can protect himself and his friends. And with this, we need to protect our dearest from the unholy monstrosities of this world! Let's see one, so I can tell you what are their weaknesses."));
	private static final String paladinStep1Description = "As a paladin, your first task is to fight any undead monster.";

	private void chooseClassPaladin(final MessageComponentInteraction interaction, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.PALADIN_1, paladinStep1Description));
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, paladinStep1Text)).update();
	}

	private static final String paladin2StepText = dialogue(
			"That was a good fight, now you know what you're fighting against. Now take this shield and try to block enemy attack with it");
	private static final String paladin2StepDescription = "Roland wants you to block an attack using your shield.";

	public void continuePaladin1Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(
				new UserQuestData(QuestType.HERO_ACADEMY_QUEST, QuestStep.PALADIN_2, paladin2StepDescription));
		userData.addItem(WeaponItems.WOODEN_SHIELD);

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, paladin2StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)), //
				makeEmbed("Obtained item", "You got a wooden shield"));
	}

	private static final String paladin3StepText = dialogue("Great! Now use that to defeat the unholy monster.");
	private static final String paladin3StepDescription = "Roland wants you to defeat an undead enemy.";

	public void continuePaladin2Step(final TextChannel channel, final ServerUserData userData, final User user) {
		userData.rpg.setQuest(
				new UserQuestData(QuestType.HERO_ACADEMY_QUEST, QuestStep.PALADIN_3, paladin3StepDescription));
		channel.sendMessage(makeEmbed(type.name, paladin3StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));
	}

	private static final String paladinFinishedStepText = dialogue(
			"Wonderful! I can feel holy power blessing you! Here is your weapon, use it to banish the enemy from this land. And also some potions to help you in those hard times. And I hope I will hear many good things about you in the future!");
	private static final String paladinFinishedStepDescription = "Roland wishes you good luck and blesses you on your journey.";

	public void continuePaladin3Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, paladinFinishedStepDescription));
		userData.addItem(WeaponItems.SHORT_SWORD);
		userData.addItem(PotionItems.HEALTH_POTION, 2);

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, paladinFinishedStepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 500, user, getServer(channel)), //
				makeEmbed("Obtained items", "You got a short sword and two health potions!"));
	}

	private static final String rangerStep1Text = String.join("\n",
			dialogue("So you want to be a ranger? I hope one day you will be compared to elven archers, boy!"), //
			description("After paying the fee the man leads you inside where you meet woman carrying a long bow."), //
			dialogue("This is Jenna, she will teach you basics of shooting a bow. I hope you get along well!"), //
			description("The man goes back to his position near the entry, as Jenna smiles at you."), //
			dialogue(
					"New ranger, hmm? I hope you know what a bow is? Just kidding, but here, take a look at my long bow"), //
			description("Jenna holds out her bow."), //
			dialogue(
					"While being a weapon mostly used for ranged attacks, it can also be used effectively at close range."), //
			description(
					"She pauses for a moment, pulling the string, then let's the arrow fly into the practice target as she grabs another arrow and pretends to stab you in the stomach with it, stopping just a centimeter short."), //
			dialogue(
					"Gotcha~ Always be careful, that is what you have to remember. Now, let's see your fighting, I want to see you fight some flying creature."));
	private static final String rangerStep1Description = "As a ranger, your first task is to find a flying enemy.";

	private void chooseClassRanger(final MessageComponentInteraction interaction, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.RANGER_1, rangerStep1Description));
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, rangerStep1Text)).update();
	}

	private static final String ranger2StepText = String.join("\n", //
			dialogue("Hmm, I will need to teach you a thing or two, but it wasn't so bad for a newcomer."), //
			description("Jenna remarks."), //
			dialogue("Now, I wanna see you fight someone on the ground"));
	private static final String ranger2StepDescription = "Jenna wants to see you fighting enemy that isn't flying.";

	public void continueRanger1Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg
				.setQuest(new UserQuestData(QuestType.HERO_ACADEMY_QUEST, QuestStep.RANGER_2, ranger2StepDescription));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, ranger2StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));
	}

	private static final String ranger3StepText = String.join("\n", //
			dialogue("Okay, your technique there will needs a lot of improvement."), //
			description("after returning to the school Jenna helps you practice and improve your technique."), //
			dialogue("Now, it's time for you to win!"));
	private static final String ranger3StepDescription = "Win a fight.";

	public void continueRanger2Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg
				.setQuest(new UserQuestData(QuestType.HERO_ACADEMY_QUEST, QuestStep.RANGER_3, ranger3StepDescription));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, ranger3StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));
	}

	private static final String rangerFinishedStepText = String.join("\n", //
			dialogue("Great! With this, you are now a ranger. Time to give you your equipment, come with me."), //
			description("Jenna opens a chest and gives you a bow and few potions from it."), //
			dialogue("This ends the training. But come visit me sometimes so we can practice together!"));
	private static final String rangerFinishedStepDescription = "Jenna is happy that you've become a ranger.";

	public void continueRanger3Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(
				new UserQuestData(QuestType.HERO_ACADEMY_QUEST, QuestStep.FINISHED, rangerFinishedStepDescription));
		userData.addItem(WeaponItems.SHORT_BOW);
		userData.addItem(PotionItems.AGILITY_1_POTION);

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, rangerFinishedStepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 500, user, getServer(channel)), //
				makeEmbed("Obtained item", "You got a short bow and a potion of swiftness!"));
	}

	private static final String rogue1StepText = String.join("\n", dialogue("A rogue? Uhh... f-fine I guess."), //
			description(
					"After paying the fee the man leads you inside where you are surprised by a dagger appearing right in front of your face."), //
			dialogue("Easy there, it's our new student, don't scare him."), //
			description("Dagger disappears as man in black clothes shows himself to you."), //
			dialogue("I'll be going, teach our student everything and don't kill him, ok?"), //
			description("Welcoming man eagerly leaves the room, as your teacher tells you your first task."), //
			dialogue(
					"You should be careful who you trust, you could have get robbed or even killed in there. Now go and show me you can at least evade an expected attack."));
	private static final String rogue1StepDescription = "As a rogue, your first task is to evade an attack.";

	private void chooseClassRogue(final MessageComponentInteraction interaction, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.ROGUE_1, rogue1StepDescription));
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, rogue1StepText)).update();
	}

	private static final String rogue2StepText = dialogue(
			"Good, but maybe you were just lucky. Do it two times in a row.");
	private static final String rogue2StepDescription = "Your next task is to evade two consecutive attacks.";

	public void continueRogue1Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserHeroAcademyQuestRogue2StepData(rogue2StepDescription));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, rogue2StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));
	}

	private static final String rogue3StepText = dialogue(
			"Ok, I guess you can dodge pretty well now, but can you also kill? Prove it.");
	private static final String rogue3StepDescription = "Your next task is to defeat an enemy.";

	public void continueRogue2Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.ROGUE_3, rogue3StepDescription));
		userData.addItem(WeaponItems.KNIFE);

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, rogue3StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)), //
				makeEmbed("Obtained item", "You got a knife."));
	}

	private static final String rogueFinishedStepText = String.join("\n", //
			dialogue("That does it."), description(
					"The mysterious man leaves sack with reward in it and walks away, quickly disappearing from your sight."));
	private static final String rogueFinishedStepDescription = "Your teacher (which somehow you don't even know the name of) taught you how to kill without being killed.";

	public void continueRogue3Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, rogueFinishedStepDescription));
		userData.addItem(PotionItems.AGILITY_1_POTION);

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, rogueFinishedStepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 500, user, getServer(channel)), //
				makeEmbed("Obtained item", "You got an agility potion!"));
	}

	private static final String warlockStep1Text = String.join("\n", //
			dialogue("Ah, so you want to be a warlock? Mighty fine, let's get you inside so you can start learning!"), //
			description(
					"After paying the fee the man leads you inside where you meet a strong man carrying a magic staff."), //
			dialogue(
					"This is Dared, he will teach you how to be a warlock. He will show you everything you need to know."), //
			description(
					"The man goes back to his position near the entry, as Dared smirks at you and starts the conversation."), //
			dialogue(
					"So you want to be a warlock, huh? Have both impressive feats of strength as well as powerful spells under your control? Well, the first thing you need to know is a staff, just like mine, isn't just for looks"), //
			description("Dared shows you his staff."), //
			dialogue(
					"It improves both magic capabilities as well as your melee fighting. I will teach you how to concentrate your magic, dispell enemy magic and also how to strike enemy properly when he walks too close to you."), //
			description(
					"He pauses for a moment, letting you take a look at his staff, and then straps it on his back while saying."), //
			dialogue(
					"Your first task will need you to find an enemy that uses magic. And don't worry, if anything bad happens, I'll be close, though you might still get a bit roughed if you are careless."));
	private static final String warlockStep1Description = "As a warlock, your first task is to find and fight an enemy that uses magic.";

	private void chooseClassWarlock(final MessageComponentInteraction interaction, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.WARLOCK_1, warlockStep1Description));
		interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(type.name, warlockStep1Text)).update();
	}

	private static final String warlock2StepText = dialogue(
			"See, when this happens, you should fight fire with fire. Don't let the enemy have all the fun. Hit them hard!");
	private static final String warlock2StepDescription = "Dared wants you to use a Force Hit magic spell.";

	public void continueWarlock1Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.WARLOCK_2, warlock2StepDescription));

		userData.addItem(MagicScrollItems.MAGIC_SCROLL_FORCE_HIT);

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, warlock2StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)), //
				makeEmbed("Obtained item", "You got a magic scroll!"));
	}

	private static final String warlock3StepText = dialogue(
			"Good, now don't use just your magic, hit them hard with your weapon when you have no mana!");
	private static final String warlock3StepDescription = "Dared wants you to deal at least 3 damage with your physical attack.";

	public void continueWarlock2Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.WARLOCK_3, warlock3StepDescription));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, warlock3StepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 250, user, getServer(channel)));
	}

	private static final String warlock4StepText = dialogue(
			"That's it! Hit them on the head with your fist and then hit them with magic!");
	private static final String warlock4StepDescription = "Finish the fight.";

	public void continueWarlock3Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.WARLOCK_4, warlock4StepDescription));
		channel.sendMessage(makeEmbed(type.name, warlock4StepText));
	}

	private static final String warlockFinishedStepText = dialogue(
			"That is nice to see, I hope you will continue fighting. Next thing you should search for is a counterspell. Don't have any on me and the budget wouldn't let us give it to you anyway, but you know what to search for.");
	private static final String warlockFinishedStepDescription = "Dared is pleased with your strength and magical ability.";

	public void continueWarlock4Step(final TextChannel channel, final ServerUserData userData) {
		userData.rpg.setQuest(new UserQuestData(type, QuestStep.FINISHED, warlockFinishedStepDescription));
		userData.addItem(getApprenticeStaffId(GemType.TOPAZ));

		final User user = fluffer10kFun.apiUtils.getUser(userData.userId);
		channel.sendMessage(makeEmbed(type.name, warlockFinishedStepText), //
				userData.addExpAndMakeEmbed(fluffer10kFun.apiUtils, 500, user, getServer(channel)), //
				makeEmbed("Obtained item", "You got an apprentice staff and a strength potion!"));
	}

}
