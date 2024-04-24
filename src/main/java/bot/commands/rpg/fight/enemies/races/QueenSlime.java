package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class QueenSlime extends EnemiesOfRace {
	public static final String QUEEN_SLIME_1 = "QUEEN_SLIME_1";
	public static final String QUEEN_SLIME_2 = "QUEEN_SLIME_2";
	public static final String QUEEN_SLIME_3 = "QUEEN_SLIME_3";

	public QueenSlime() {
		super(MonsterGirlRace.QUEEN_SLIME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(QUEEN_SLIME_1)//
				.strength(4).agility(2).intelligence(3)//
				.baseHp(10)//
				.diff(2)//
				.classes(FighterClass.SLIME_REGEN, FighterClass.WEAK_TO_FIRE, FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
		makeBuilderOld(QUEEN_SLIME_2)//
				.strength(7).agility(3).intelligence(6)//
				.baseHp(20)//
				.diff(4)//
				.classes(FighterClass.DOUBLE_ATTACK, FighterClass.SLIME_REGEN, FighterClass.WEAK_TO_FIRE,
						FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
		makeBuilderOld(QUEEN_SLIME_3)//
				.strength(11).agility(5).intelligence(17)//
				.baseHp(40)//
				.diff(10)//
				.classes(FighterClass.TRIPLE_ATTACK, FighterClass.SLIME_REGEN, FighterClass.WEAK_TO_FIRE,
						FighterClass.WET)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);
	}
}