package bot.commands.rpg.fight.enemies.races;

import static bot.util.Utils.Pair.pair;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class LesserSuccubus extends EnemiesOfRace {
	public static final String LESSER_SUCCUBUS_1 = "LESSER_SUCCUBUS_1";

	public LesserSuccubus() {
		super(MonsterGirlRace.LESSER_SUCCUBUS);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		rpgEnemies.add(makeBuilderOld(LESSER_SUCCUBUS_1)//
				.strength(2).agility(3).intelligence(5)//
				.baseHp(5)//
				.diff(1)//
				.classes(FighterClass.USES_MAGIC)//
				.actionSelector(fluffer10kFun.rpgEnemyActionSelectorUtils.actionsFrom(//
						pair(5, RPGFightAction.ATTACK_A_2), //
						pair(1, RPGFightAction.CHARM))));
	}
}