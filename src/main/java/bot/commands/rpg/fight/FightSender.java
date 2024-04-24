package bot.commands.rpg.fight;

import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.joinNames;
import static bot.util.Utils.Pair.pair;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.emoji.KnownCustomEmoji;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageUpdater;
import org.javacord.api.entity.message.component.ActionRow;
import org.javacord.api.entity.message.component.Button;
import org.javacord.api.entity.message.component.LowLevelComponent;
import org.javacord.api.entity.message.embed.EmbedBuilder;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightActionsHandler.ActionData;
import bot.commands.rpg.fight.Targetting.TargetCheck;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.commands.rpg.spells.ActiveSkill;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FightData;
import bot.data.fight.FighterClass;
import bot.data.fight.FighterData;
import bot.data.fight.FighterData.FighterType;
import bot.data.fight.FighterStatus;
import bot.data.fight.PlayerFighterData;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserFluffLoverQuestFluffingStepData;
import bot.userData.rpg.questData.UserQuestData;
import bot.util.Utils.Pair;

public class FightSender {
	private final Targetting saltable;

	private final Fluffer10kFun fluffer10kFun;

	private final Map<RPGFightAction, KnownCustomEmoji> actionEmojis = new HashMap<>();

	private final List<RPGFightAction> fighterChoosingRow = asList(RPGFightAction.FIGHTER_PREVIOUS,
			RPGFightAction.FIGHTER_NEXT);

	public FightSender(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		saltable = new Targetting(
				TargetCheck.ENEMY.alive().with(fluffer10kFun, FighterClass.SALTABLE).without(FighterStatus.SALTED));

		actionEmojis.put(RPGFightAction.FLUFF, fluffer10kFun.apiUtils.getEmojiByNameFromMyServer("fluffytail"));
	}

	private static final List<Pair<FighterClass, String>> fighterClassDescriptions = asList(//
			pair(FighterClass.MECHANICAL, "mechanical"), //
			pair(FighterClass.UNDEAD, "undead"), //

			pair(FighterClass.FIERY, "fiery"), //
			pair(FighterClass.ICY, "icy"), //
			pair(FighterClass.WET, "wet"), //

			pair(FighterClass.CLEVER, "very clever"), //
			pair(FighterClass.FLYING, "flying"), //
			pair(FighterClass.CLIMBING, "climbing"), //
			pair(FighterClass.CANT_BE_FROZEN, "invulnerable to freeze"));

	private String getDescription(final RPGEnemyData enemyData) {
		final List<String> enemyClassesDescriptions = new ArrayList<>();

		for (final Pair<FighterClass, String> classDescription : fighterClassDescriptions) {
			if (enemyData.classes.contains(classDescription.a)) {
				enemyClassesDescriptions.add(classDescription.b);
			}
		}

		return enemyClassesDescriptions.isEmpty() ? null
				: enemyData.name + " is " + joinNames(enemyClassesDescriptions);
	}

	private String getAvatar(final FighterData fighter) {
		if (fighter.enemy() != null) {
			final RPGEnemyData data = fighter.enemy().enemyData(fluffer10kFun);
			switch (data.type) {
			case MONSTER_GIRL:
				return data.mg().race.avatarLink == null ? data.mg().race.imageLink : data.mg().race.avatarLink;
			case OTHER:
			default:
				return data.imgUrl();
			}
		}

		if (fighter.player() != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(fighter.fight.serverId,
					fighter.player().userId);
			return userData.rpg.avatar;
		}

		return null;
	}

	private String getImage(final FighterData fighter) {
		if (fighter.enemy() != null) {
			final RPGEnemyData data = fighter.enemy().enemyData(fluffer10kFun);
			switch (data.type) {
			case MONSTER_GIRL:
				return data.mg().race.imageLink;
			case OTHER:
			default:
				return data.imgUrl();
			}
		}

		if (fighter.player() != null) {
			final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(fighter.fight.serverId,
					fighter.player().userId);
			return userData.rpg.avatar;
		}

		return null;
	}

	private void addFighterData(final EmbedBuilder embed, final FighterData fighter) {
		if (fighter.enemy() != null) {
			final String description = getDescription(fighter.enemy().enemyData(fluffer10kFun));
			if (description != null) {
				embed.addField("Description", description);
			}
		}
		embed.addField(fighter.name + "'s HP", fighter.hp + "/" + fighter.maxHp);

		if (fighter.player() != null) {
			final PlayerFighterData player = fighter.player();
			embed.addField(fighter.name + "'s mana", player.mana + "/" + player.maxMana);
		}

		embed.addField(fighter.name + "'s status", fighter.statuses.getDescription());
	}

	private EmbedBuilder getEmbed(final FightData fight) {
		final FighterData currentFighter = fight.getCurrentFighter();
		final FighterData otherFighter = fight.fighters.get(fight.targetFighter);

		final int currentFighterNumber = fight.fightersOrder.indexOf(fight.targetFighter) + 1;

		final EmbedBuilder embed = makeEmbed(
				"Fight! (" + currentFighterNumber + "/" + fight.fightersOrder.size() + ")");

		embed.addField("Fighter", otherFighter.name + " (" + otherFighter.level + ")");

		addFighterData(embed, otherFighter);

		embed.addField("Last actions", fight.getTurnDescriptions())//
				.addField("Current turn", currentFighter.name);

		addFighterData(embed, currentFighter);

		embed.setThumbnail(getAvatar(currentFighter))//
				.setImage(getImage(otherFighter))//
				.setFooter("Use \"/fight refresh\" to send new embed");

		return embed;
	}

	private List<List<RPGFightAction>> actions(final FightData fight, final PlayerFighterData fighter) {
		if (fighter.statuses.cantDoActions()) {
			return asList(asList(RPGFightAction.WAIT), fighterChoosingRow);
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(fight.serverId, fighter.userId);

		final List<List<RPGFightAction>> rows = new ArrayList<>();

		final List<RPGFightAction> row0 = new ArrayList<>();
		row0.add(RPGFightAction.ATTACK);
		row0.add(RPGFightAction.SPELL);

		if (!fighter.statuses.cantUseItems()) {
			row0.add(RPGFightAction.USE_ITEM);
			row0.add(RPGFightAction.EQUIP_ITEM);
		}
		if (fighter.statuses.canGetFree()) {
			row0.add(RPGFightAction.GET_FREE);
		}
		rows.add(row0);

		List<RPGFightAction> row1 = new ArrayList<>();
		for (final ActiveSkill spell : userData.rpg.spellHotbar.spells) {
			if (spell != null) {
				if (!userData.rpg.spells().contains(spell)) {
					continue;
				}

				if (row1.size() >= 5) {
					rows.add(row1);
					row1 = new ArrayList<>();
				}
				row1.add(spell.action);
			}
		}
		if (!row1.isEmpty()) {
			rows.add(row1);
		}

		final List<RPGFightAction> row2 = new ArrayList<>();
		row2.add(RPGFightAction.WAIT);
		if (!fighter.statuses.canGetFree() && userData.blessings.blessingsObtained.contains(Blessing.FAST_RUNNER)) {
			row2.add(RPGFightAction.ESCAPE);
		}

		final UserQuestData quest = userData.rpg.quests.get(QuestType.FLUFF_LOVER_QUEST);
		if (quest != null && quest.step == QuestStep.FLUFFING) {
			final List<MonsterGirlRace> girlsLeft = ((UserFluffLoverQuestFluffingStepData) quest).girlsLeft;
			final Targetting targetting = new Targetting(
					TargetCheck.ENEMY.monsterGirlOfRaceFrom(fluffer10kFun, girlsLeft));
			if (targetting.getFirst(fight, fighter) != null) {
				row2.add(RPGFightAction.FLUFF);
			}
		}
		if (saltable.getFirst(fight, fighter) != null) {
			row2.add(RPGFightAction.SALT);
		}
		rows.add(row2);

		rows.add(fighterChoosingRow);

		return rows;
	}

	private static final Map<RPGFightAction, String> actionDescriptions = toMap(//
			pair(RPGFightAction.ATTACK, "Attack"), //
			pair(RPGFightAction.ESCAPE, "Escape"), //
			pair(RPGFightAction.EQUIP_ITEM, "Equip"), //
			pair(RPGFightAction.FLUFF, "Fluff"), //
			pair(RPGFightAction.GET_FREE, "Get free"), //
			pair(RPGFightAction.SALT, "Salt"), //
			pair(RPGFightAction.SPELL, "Spell"), //
			pair(RPGFightAction.SURRENDER, "Surrender"), //
			pair(RPGFightAction.FIGHTER_NEXT, "Next fighter"), //
			pair(RPGFightAction.FIGHTER_PREVIOUS, "Previous fighter"), //
			pair(RPGFightAction.USE_ITEM, "Use"), //
			pair(RPGFightAction.WAIT, "Wait"));

	static {
		for (final ActiveSkill spell : ActiveSkill.values()) {
			actionDescriptions.put(spell.action, spell.name);
		}
	}

	private List<ActionRow> getActions(final FightData fight) {
		final FighterData fighter = fight.getCurrentFighter();
		if (fighter.type == FighterType.ENEMY) {
			return new ArrayList<>();
		}

		final PlayerFighterData playerFighter = (PlayerFighterData) fighter;
		final List<List<RPGFightAction>> actions = actions(fight, playerFighter);
		final List<ActionRow> rows = new ArrayList<>();

		for (final List<RPGFightAction> rowActions : actions) {
			final List<LowLevelComponent> rowComponents = new ArrayList<>();
			for (final RPGFightAction action : rowActions) {
				final String actionId = fluffer10kFun.fightActionsHandler
						.addActiveAction(new ActionData(action, playerFighter.userId, fighter.id, fight));
				final KnownCustomEmoji emoji = actionEmojis.get(action);
				if (emoji != null) {
					rowComponents.add(Button.secondary("fight_action " + actionId, emoji));
				} else {
					rowComponents.add(Button.secondary("fight_action " + actionId,
							actionDescriptions.getOrDefault(action, action.name())));
				}
			}

			rows.add(ActionRow.of(rowComponents));
		}

		return rows;
	}

	public void addFight(final MessageUpdater updater, final FightData fight) {
		updater.addEmbed(getEmbed(fight));

		if (!fight.ended) {
			for (final ActionRow actionRow : getActions(fight)) {
				updater.addComponents(actionRow);
			}
		}
	}

	public Message addFight(final TextChannel channel, final FightData fight) {
		return channel.sendMessage(getEmbed(fight)).join();
	}
}
