package bot.commands.rpg.fight.enemies.data.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class Ghoul extends EnemiesOfRace {
	public static final String GHOUL_1 = "GHOUL_1";
	public static final String GHOUL_2 = "GHOUL_2";

	public Ghoul() {
		super(MonsterGirlRace.GHOUL);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		makeBuilder(GHOUL_1)//
				.strength(8).agility(8).intelligence(3)//
				.baseHp(10)//
				.classes(FighterClass.UNDEAD)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(4, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.LICK), //
						pair(1, RPGFightAction.FRENZY)))
				.build(rpgEnemies);
		makeBuilder(GHOUL_2)//
				.strength(13).agility(12).intelligence(4)//
				.baseHp(15)//
				.classes(FighterClass.UNDEAD)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(2, RPGFightAction.ATTACK_S_1), //
						pair(1, RPGFightAction.LICK), //
						pair(1, RPGFightAction.FRENZY)))
				.build(rpgEnemies);
	}
}
