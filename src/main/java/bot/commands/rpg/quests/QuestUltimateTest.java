package bot.commands.rpg.quests;

import static bot.commands.rpg.fight.enemies.special.MonsterArmyOfficer.MONSTER_ARMY_OFFICER_0;
import static bot.commands.rpg.fight.enemies.special.MonsterArmyOfficer.MONSTER_ARMY_OFFICER_1;
import static bot.commands.rpg.fight.enemies.special.MonsterArmyOfficer.MONSTER_ARMY_OFFICER_2;
import static bot.commands.rpg.fight.enemies.special.MonsterArmyOfficer.MONSTER_ARMY_OFFICER_3;
import static bot.util.CollectionUtils.toMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.Utils.bold;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.interaction.MessageComponentInteraction;
import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.RPGStatUtils.RPGStatsData;
import bot.commands.rpg.fight.fightRewards.FightEndReward;
import bot.data.fight.EnemyFighterData;
import bot.data.fight.FightData.FightType;
import bot.data.fight.FighterData;
import bot.data.fight.PlayerFighterData;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;
import bot.userData.rpg.questData.UserQuestData;

public class QuestUltimateTest extends Quest {
	private abstract class Step {
		public final QuestStep step;
		private final String enemyId;
		private final FightEndReward reward;
		private final String startText;
		private final String startDescription;
		private final String fightStartText;

		public Step(final QuestStep step, final String enemyId, final FightEndReward reward, final String startText,
				final String startDescription, final String fightStartText) {
			this.step = step;
			this.enemyId = enemyId;
			this.reward = reward;
			this.startText = startText;
			this.startDescription = startDescription;
			this.fightStartText = fightStartText;
		}

		public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
			final UserQuestData questData = new UserQuestData(type, step, startDescription, true);
			userData.rpg.setQuest(questData);
			interaction.createImmediateResponder().addEmbed(newQuestMessage(startText)).respond();
		}

		public void continueStep(final MessageComponentInteraction interaction, final ServerUserData userData) {
			if (!userData.canStartOtherFight()) {
				String msg;
				if (userData.rpg.fightId != null) {
					msg = "You can't start another fight now, finish current one first.";
				} else {
					msg = "You are too tired to fight.";
				}
				interaction.createOriginalMessageUpdater().addEmbed(makeEmbed(msg)).update();
				return;
			}
			userData.startOtherFight();

			interaction.createImmediateResponder().addEmbed(makeEmbed(type.name, fightStartText)).respond().join();

			final ServerTextChannel channel = interaction.getChannel().get().asServerTextChannel().get();
			final User user = interaction.getUser();
			final Server server = interaction.getServer().get();
			final String name = userData.rpg.getName(user, server);
			final RPGStatsData playerStats = fluffer10kFun.rpgStatUtils.getTotalStats(userData);
			final List<FighterData> fightersList = asList(//
					PlayerFighterData.fromStats("PLAYER", user.getId(), "PLAYER", name, playerStats), //
					EnemyFighterData.fromStats("BOSS", fluffer10kFun.rpgEnemies.get(enemyId), "BOSS"));
			final Map<String, FighterData> fighters = toMap(fighter -> fighter.id, fightersList);
			fluffer10kFun.fightStart.startFight(channel, FightType.NO_NORMAL_REWARDS, fighters, reward);
		}
	}

	private class Step0 extends Step {
		public Step0() {
			super(QuestStep.STEP_0, //
					MONSTER_ARMY_OFFICER_0, //
					FightEndReward.BLESSING_0, //
					String.join("\n", //
							description("You approach a small village, when you see someone running towards you."), //
							dialogue("Help us! We are under attack! It's the monsters!"), //
							description("You prepare to engage and run into the village."), //
							description(
									"Monsters are indeed running rampant everywhere, but you spot one manticore that just looks at it all with a smile."), //
							description("Sure that she must be the leader, you approach her.")), //
					String.join("\n", //
							"The village is under attack! Continue the quest to approach the enemy.", //
							"Use " + bold("/quest continue") + " to start the fight."), //
					String.join("\n", //
							description(
									"You approach the manticore that gives off a powerful aura. She turns her head towards you."), //
							dialogue(
									"A hero? Hora, that is a target for me~ I will enjoy draining someone stronger than mere villagers."), //
							description("She licks her lips and gets closer to you.")));
		}
	}

	private class Step1 extends Step {
		public Step1() {
			super(QuestStep.STEP_1, //
					MONSTER_ARMY_OFFICER_1, //
					FightEndReward.BLESSING_1, //
					String.join("\n", //
							description(
									"As you roam the forest, you suddenly start to feel the powerful aura that you felt once before."), //
							description(
									"Then you see a village getting attacked, and you know that it means a powerful enemy."), //
							description(
									"You carefully approach the village and spot Lilian, then think on how to approach her this time.")), //
					String.join("\n", //
							"Another village is under attack! Continue the quest to approach Lilian.", //
							"Use " + bold("/quest continue") + " to start the fight."), //
					String.join("\n", //
							description("You approach Lilian. She turns her head towards you and smiles."), //
							dialogue(
									"Who do we have here? I know you, I defeated you once before~ Came to try your chances again?"), //
							description("Then she laughs as she takes the fighting stance."), //
							dialogue("Come at me then~ I'll enjoy draining you once more~")));
		}
	}

	private class Step2 extends Step {
		public Step2() {
			super(QuestStep.STEP_2, //
					MONSTER_ARMY_OFFICER_2, //
					FightEndReward.BLESSING_2, //
					String.join("\n", //
							description(
									"You feel the known powerful aura once again, and head into the direction you feel it from."), //
							description("Lilian is camping there, not far from the girls she controls."), //
							description("You carefully approach her, then plan another way to approach her.")), //
					String.join("\n", //
							"Continue the quest to approach Lilian once again.", //
							"Use " + bold("/quest continue") + " to start the fight."), //
					String.join("\n", //
							description(
									"You approach Lilian from behind as she eats a piece of meat. Her ears twitch and she turns her head towards you before you can get in range of attack."), //
							dialogue(
									"Oh my, you really do want to continue this, hmm? Not gonna let me finish my meal, huh? Fine, let's go straight for dessert~"), //
							description("She stands up and fight begins!")));
		}
	}

	private class Step3 extends Step {
		public Step3() {
			super(QuestStep.STEP_3, //
					MONSTER_ARMY_OFFICER_3, //
					FightEndReward.BLESSING_3, //
					String.join("\n", //
							description("You find Lilian in the forest, on her way to attack another village."), //
							description(
									"You decide that it's time to stop her right now, and prepare to intercept her.")), //
					String.join("\n", //
							"Continue the quest to intercept Lilian.", //
							"Use " + bold("/quest continue") + " to start the fight."), //
					String.join("\n", //
							description("You come out of the forest on the road, in front of Lilian."), //
							dialogue("It's you~ Mmmm, I guess you are really ready this time, hmm~? Let's do it!"), //
							description("She waves her tail at you and prepares to fight.")));
		}
	}

	private final Fluffer10kFun fluffer10kFun;
	private final List<Step> steps = asList(//
			new Step0(), //
			new Step1(), //
			new Step2(), //
			new Step3());

	public QuestUltimateTest(final Fluffer10kFun fluffer10kFun) {
		super(QuestType.ULTIMATE_TEST, 30);

		this.fluffer10kFun = fluffer10kFun;

		for (final Step step : steps) {
			continueQuestHandlers.put(step.step, step::continueStep);
		}
	}

	private int minQuestLevel(final int blessings) {
		return 30 + blessings * 10 / 3;
	}

	@Override
	protected boolean fitsCriteria(final ServerUserData userData) {
		if (userData.rpg.level >= 100 && userData.blessings.blessings >= 21) {
			return true;
		}

		if (userData.rpg.level < minQuestLevel(userData.blessings.blessings)) {
			return false;
		}
		if (userData.saves.values().stream()
				.anyMatch(userRPGData -> userRPGData.quests.get(QuestType.ULTIMATE_TEST) != null)) {
			return false;
		}

		return true;
	}

	@Override
	public void start(final SlashCommandInteraction interaction, final ServerUserData userData) {
		if (userData.blessings.blessings >= steps.size()) {
			interaction.createImmediateResponder()
					.addEmbed(makeEmbed("Quest part not implemented, please wait for update")).respond();
			return;
		}

		steps.get(userData.blessings.blessings).start(interaction, userData);
	}
}
