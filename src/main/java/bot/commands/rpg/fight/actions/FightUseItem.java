package bot.commands.rpg.fight.actions;

import static bot.util.CollectionUtils.mapToList;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.partialRoll;
import static bot.util.RandomUtils.roll;
import static bot.util.Utils.Pair.pair;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

import org.javacord.api.interaction.MessageComponentInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.FightActionHandler;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.Targetting;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.skills.Skill;
import bot.data.fight.FightData;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.FighterStatusData;
import bot.data.fight.PlayerFighterData;
import bot.data.items.Item;
import bot.data.items.ItemAmount;
import bot.data.items.ItemClass;
import bot.data.items.data.MonmusuDropItems;
import bot.data.items.data.PotionItems;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.util.EmbedUtils.EmbedField;
import bot.util.pages.builders.PagedPickerMessageBuilder;
import bot.util.pages.messages.PagedMessage;

public class FightUseItem implements FightActionHandler {
	private static Targetting defaultEnemyTargetting = new Targetting(TargetCheck.ENEMY.alive());

	private final Fluffer10kFun fluffer10kFun;

	public FightUseItem(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void handle(final FightTempData data, final RPGFightAction action) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			fluffer10kFun.apiUtils.messageUtils
					.sendMessageToMe("Non player fighter used action " + action + "!!!\n" + data.activeFighter.toMap());
			return;
		}

		final PlayerFighterData player = (PlayerFighterData) data.activeFighter;
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);

		final List<ItemAmount> itemAmounts = userData.items.entrySet().stream()//
				.filter(entry -> entry.getValue() > 0)//
				.map(entry -> fluffer10kFun.items.getItemAmount(entry.getKey(), entry.getValue()))//
				.filter(itemAmount -> itemAmount.item.classes.contains(ItemClass.USE_IN_COMBAT))//
				.sorted()//
				.collect(toList());
		final List<Item> items = mapToList(itemAmounts, itemAmount -> itemAmount.item);
		final List<EmbedField> fields = mapToList(itemAmounts, itemAmount -> itemAmount.getAsFieldWithDescription());

		final PagedMessage msg = new PagedPickerMessageBuilder<>("Pick item to use", 10, fields, items)//
				.onPick((interaction, page, spell) -> onPick(interaction, spell, data))//
				.build();
		fluffer10kFun.pagedMessageUtils.addMessage(msg, player.userId, data.channel);
	}

	private void onPick(final MessageComponentInteraction interaction, final Item item, final FightTempData data) {
		final long userId = interaction.getUser().getId();

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(interaction.getServer().get(),
				userId);
		if (userData.rpg.fightId == null) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You aren't in a fight anymore")).update();
			return;
		}

		final FightData fight = fluffer10kFun.botDataUtils.fights.get(userData.rpg.fightId);
		if (fight == null) {
			interaction.createOriginalMessageUpdater()
					.addEmbed(makeEmbed("There was an error saving your fight, clearing it")).update();
			userData.rpg.fightId = null;
			return;
		}
		final FighterData fighter = fight.getCurrentFighter();
		if (fighter.type != FighterType.PLAYER || ((PlayerFighterData) fighter).userId != userId) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("It's not your turn!")).update();
			return;
		}

		if (!userData.hasItem(item)) {
			interaction.createOriginalMessageUpdater().addEmbed(makeEmbed("You don't have the item anymore!")).update();
			return;
		}

		if (item.classes.contains(ItemClass.SINGLE_USE)) {
			userData.addItem(item, -1);
		}

		interaction.acknowledge();
		interaction.getMessage().delete();
		fluffer10kFun.fightActionsHandler.handleAction(data, d -> itemUseHandlers.get(item.id).accept(d, item),
				RPGFightAction.USED_ITEM);
	}

	private final Map<String, BiConsumer<FightTempData, Item>> itemUseHandlers = toMap(//
			pair(PotionItems.HEALTH_POTION_MINOR, this::useMinorHealthPotion), //
			pair(MonmusuDropItems.HOLSTAUR_MILK, this::useMinorHealthPotion), //
			pair(PotionItems.HEALTH_POTION, this::useHealthPotion), //
			pair(PotionItems.HEALTH_POTION_MAJOR, this::useMajorHealthPotion), //
			pair(PotionItems.MANA_POTION_MINOR, this::useMinorManaPotion), //
			pair(PotionItems.MANA_POTION, this::useManaPotion), //
			pair(PotionItems.MANA_POTION_MAJOR, this::useMajorManaPotion), //
			pair(PotionItems.AGILITY_1_POTION, this::usePotionOfSwiftness), //
			pair(PotionItems.AGILITY_2_POTION, this::usePotionOfAgility), //
			pair(PotionItems.AGILITY_3_POTION, this::usePotionOfAcrobatics), //
			pair(PotionItems.STRENGTH_1_POTION, this::usePotionOfStrength), //
			pair(PotionItems.STRENGTH_2_POTION, this::usePotionOfGreaterStrength), //
			pair(PotionItems.STRENGTH_3_POTION, this::usePotionOfInhumanStrength), //
			pair(PotionItems.INTELLIGENCE_1_POTION, this::usePotionOfPotence), //
			pair(PotionItems.INTELLIGENCE_2_POTION, this::usePotionOfIntelligence), //
			pair(PotionItems.INTELLIGENCE_3_POTION, this::usePotionOfMastermind), //
			pair(PotionItems.DOPPELGANGER_POTION, this::useDoppelgangerPotion), //
			pair(MonmusuDropItems.WERESHEEP_WOOL, this::useWeresheepWool));

	private void drinkHPPotion(final FightTempData data, int regen, final String potionName) {
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(Blessing.POTION_MASTER)) {
				regen = (regen + 2) * 6 / 5;
			}
			if (userData.rpg.skills.contains(Skill.FAST_HEALING_3)) {
				regen *= 2;
			}
		}

		regen = min(regen, data.activeFighter.maxHp - data.activeFighter.hp);
		data.activeFighter.hp += regen;
		data.fight.addTurnDescription(
				data.activeFighter.name + " drinks " + potionName + " and regenerates " + regen + " hp.");
	}

	private void useMinorHealthPotion(final FightTempData data, final Item item) {
		final int regen = 5 + partialRoll(data.activeFighter.maxHp / 4.0, 2);
		drinkHPPotion(data, regen, item.name);
	}

	private void useHealthPotion(final FightTempData data, final Item item) {
		final int regen = 10 + partialRoll(data.activeFighter.maxHp / 3.0, 2);
		drinkHPPotion(data, regen, item.name);
	}

	private void useMajorHealthPotion(final FightTempData data, final Item item) {
		final int regen = 15 + partialRoll(data.activeFighter.maxHp / 2.0, 2);
		drinkHPPotion(data, regen, item.name);
	}

	private void drinkManaPotion(final FightTempData data, int regen, final String potionName) {
		final PlayerFighterData player = data.activeFighter.player();
		if (player != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
			if (userData.blessings.blessingsObtained.contains(Blessing.POTION_MASTER)) {
				regen = (regen + 2) * 6 / 5;
			}
		}

		regen = min(regen, player.maxMana - player.mana);
		player.mana += regen;
		data.fight.addTurnDescription(
				data.activeFighter.name + " drinks " + potionName + " and regenerates " + regen + " mana.");
	}

	private void useMinorManaPotion(final FightTempData data, final Item item) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			fluffer10kFun.apiUtils.messageUtils
					.sendMessageToMe("wrong fighter used mana potion!\n" + data.activeFighter.toMap());
			return;
		}

		final PlayerFighterData player = (PlayerFighterData) data.activeFighter;
		final int regen = 3 + partialRoll(player.maxMana / 8.0, 2);
		drinkManaPotion(data, regen, item.name);
	}

	private void useManaPotion(final FightTempData data, final Item item) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			fluffer10kFun.apiUtils.messageUtils
					.sendMessageToMe("wrong fighter used mana potion!\n" + data.activeFighter.toMap());
			return;
		}

		final PlayerFighterData player = (PlayerFighterData) data.activeFighter;
		final int regen = 5 + partialRoll(player.maxMana / 6.0, 2);
		drinkManaPotion(data, regen, item.name);
	}

	private void useMajorManaPotion(final FightTempData data, final Item item) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			fluffer10kFun.apiUtils.messageUtils
					.sendMessageToMe("wrong fighter used mana potion!\n" + data.activeFighter.toMap());
			return;
		}

		final PlayerFighterData player = (PlayerFighterData) data.activeFighter;
		final int regen = 7 + partialRoll(player.maxMana / 4.0, 2);
		drinkManaPotion(data, regen, item.name);
	}

	private void drinkAgilityPotion(final FightTempData data, final FighterStatus status, final String itemName) {
		data.activeFighter.statuses.removeStatus(FighterStatus.AGILITY_1);
		data.activeFighter.statuses.removeStatus(FighterStatus.AGILITY_2);
		data.activeFighter.statuses.removeStatus(FighterStatus.AGILITY_3);

		data.activeFighter.statuses.addStatus(new FighterStatusData(status).endless());
		data.fight.addTurnDescription(data.activeFighter.name + " drinks " + itemName + " and gains an agility buff!");
	}

	private void usePotionOfSwiftness(final FightTempData data, final Item item) {
		drinkAgilityPotion(data, FighterStatus.AGILITY_1, item.name);
	}

	private void usePotionOfAgility(final FightTempData data, final Item item) {
		drinkAgilityPotion(data, FighterStatus.AGILITY_2, item.name);
	}

	private void usePotionOfAcrobatics(final FightTempData data, final Item item) {
		drinkAgilityPotion(data, FighterStatus.AGILITY_3, item.name);
	}

	private void drinkStrengthPotion(final FightTempData data, final FighterStatus status, final String itemName) {
		data.activeFighter.statuses.removeStatus(FighterStatus.STRENGTH_1);
		data.activeFighter.statuses.removeStatus(FighterStatus.STRENGTH_2);
		data.activeFighter.statuses.removeStatus(FighterStatus.STRENGTH_3);

		data.activeFighter.statuses.addStatus(new FighterStatusData(status).endless());
		data.fight.addTurnDescription(data.activeFighter.name + " drinks " + itemName + " and gains a strength buff!");
	}

	private void usePotionOfStrength(final FightTempData data, final Item item) {
		drinkStrengthPotion(data, FighterStatus.STRENGTH_1, item.name);
	}

	private void usePotionOfGreaterStrength(final FightTempData data, final Item item) {
		drinkStrengthPotion(data, FighterStatus.STRENGTH_2, item.name);
	}

	private void usePotionOfInhumanStrength(final FightTempData data, final Item item) {
		drinkStrengthPotion(data, FighterStatus.STRENGTH_3, item.name);
	}

	private void drinkIntelligencePotion(final FightTempData data, final FighterStatus status, final String itemName) {
		data.activeFighter.statuses.removeStatus(FighterStatus.INTELLIGENCE_1);
		data.activeFighter.statuses.removeStatus(FighterStatus.INTELLIGENCE_2);
		data.activeFighter.statuses.removeStatus(FighterStatus.INTELLIGENCE_3);

		data.activeFighter.statuses.addStatus(new FighterStatusData(status).endless());
		data.fight.addTurnDescription(
				data.activeFighter.name + " drinks " + itemName + " and gains an intelligence buff!");
	}

	private void usePotionOfPotence(final FightTempData data, final Item item) {
		drinkIntelligencePotion(data, FighterStatus.INTELLIGENCE_1, item.name);
	}

	private void usePotionOfIntelligence(final FightTempData data, final Item item) {
		drinkIntelligencePotion(data, FighterStatus.INTELLIGENCE_2, item.name);
	}

	private void usePotionOfMastermind(final FightTempData data, final Item item) {
		drinkIntelligencePotion(data, FighterStatus.INTELLIGENCE_3, item.name);
	}

	private void useDoppelgangerPotion(final FightTempData data, final Item item) {
		data.activeFighter.statuses.addStatus(new FighterStatusData(FighterStatus.DOPPELGANGER).duration(5));
		data.fight.addTurnDescription(data.activeFighter.name + " drinks " + item.name + " and gains a doppelganger!");
	}

	private void useWeresheepWool(final FightTempData data, final Item item) {
		data.setUpTarget(fluffer10kFun, defaultEnemyTargetting);
		final int duration = max(2, 5 - roll(data.targetStats.intelligence / 5.0));
		data.target.statuses.addStatus(new FighterStatusData(FighterStatus.SLEEP).duration(duration));
		data.fight.addTurnDescription(data.activeFighter.name + " rubs a " + item.name + " against " + data.target.name
				+ " and makes them fall asleep!");
	}

}
