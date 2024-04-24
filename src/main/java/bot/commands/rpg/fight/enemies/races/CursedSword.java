package bot.commands.rpg.fight.enemies.races;

import static bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.action;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.RPGFightAction;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyActionSelectorUtils.ActionSelector;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.FighterClass;

public class CursedSword extends EnemiesOfRace {
	public CursedSword() {
		super(MonsterGirlRace.CURSED_SWORD);
	}

	@Override
	protected void addEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		final ActionSelector actionSelectorGentle = action(RPGFightAction.ATTACK_S_14).gentle();
		final ActionSelector actionSelectorNormal = action(RPGFightAction.ATTACK_S_14);

		for (int i = 0; i <= 5; i++) {
			makeStandardBuilder(i, 7, 4, 8, 1, 5, 0, 10, 2)//
					.armor(2 + i * 8 / 5)//
					.classes(FighterClass.CURSED_SWORD)//
					.actionSelector(i < 2 ? actionSelectorGentle : actionSelectorNormal)//
					.build(rpgEnemies);
		}
	}
}
