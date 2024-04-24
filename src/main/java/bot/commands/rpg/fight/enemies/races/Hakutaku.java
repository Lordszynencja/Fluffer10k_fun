package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Hakutaku extends EnemiesOfRace {
	public static final String HAKUTAKU_1 = "HAKUTAKU_1";
	public static final String HAKUTAKU_2 = "HAKUTAKU_2";
	public static final String HAKUTAKU_3 = "HAKUTAKU_3";

	public Hakutaku() {
		super(MonsterGirlRace.HAKUTAKU);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(HAKUTAKU_1)//
				.strength(5).agility(5).intelligence(10)//
				.baseHp(10)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING)))//
				.build(rpgEnemies);
		makeBuilderOld(HAKUTAKU_2)//
				.strength(5).agility(5).intelligence(14)//
				.baseHp(15)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.FIND_WEAKNESS)))//
				.build(rpgEnemies);
		makeBuilderOld(HAKUTAKU_3)//
				.strength(7).agility(7).intelligence(17)//
				.baseHp(20)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom( //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.FIND_WEAKNESS)))//
				.build(rpgEnemies);
	}
}