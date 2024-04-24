package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class DarkSlime extends EnemiesOfRace {
	public static final String DARK_SLIME_1 = "DARK_SLIME_1";

	public DarkSlime() {
		super(MonsterGirlRace.DARK_SLIME);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilderOld(DARK_SLIME_1)//
				.strength(8).agility(7).intelligence(13)//
				.baseHp(30)//
				.diff(5)//
				.armor(2)//
				.classes(FighterClass.SLIME_REGEN, FighterClass.WET, FighterClass.WEAK_TO_FIRE)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(1, RPGFightAction.ATTACK_S_3), //
						pair(1, RPGFightAction.SPELL_FIREBALL), //
						pair(1, RPGFightAction.SPELL_ICE_BOLT), //
						pair(1, RPGFightAction.SPELL_LIGHTNING), //
						pair(1, RPGFightAction.CHARM), //
						pair(1, RPGFightAction.GRAB_SLIME)))//
				.build(rpgEnemies);

	}
}
