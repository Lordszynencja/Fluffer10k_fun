package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Banshee extends EnemiesOfRace {
	public static final String BANSHEE_1 = "BANSHEE_1";
	public static final String BANSHEE_2 = "BANSHEE_2";

	public Banshee() {
		super(MonsterGirlRace.BANSHEE);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(BANSHEE_1)//
				.strength(6).agility(8).intelligence(12)//
				.baseHp(15)//
				.diff(-3)//
				.classes(FighterClass.UNDEAD, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_7), //
						pair(1, RPGFightAction.BANSHEE_WAIL), //
						pair(1, RPGFightAction.SPELL_MAGIC_SHIELD)))//
				.build(rpgEnemies);
		makeBuilderOld(BANSHEE_2)//
				.strength(8).agility(10).intelligence(15)//
				.baseHp(20)//
				.diff(-3)//
				.classes(FighterClass.UNDEAD, FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(3, RPGFightAction.ATTACK_S_7), //
						pair(1, RPGFightAction.BANSHEE_WAIL), //
						pair(1, RPGFightAction.SPELL_MAGIC_SHIELD)))//
				.build(rpgEnemies);
	}
}
