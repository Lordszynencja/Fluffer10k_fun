package bot.commands.rpg.fight.actions.physicalAttacks;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightTempData;
import bot.data.fight.FighterStatus;
import bot.data.fight.PlayerFighterData;
import bot.data.items.Item;
import bot.data.items.ItemClass;
import bot.data.quests.QuestType;
import bot.userData.ServerUserData;
import bot.userData.rpg.questData.QuestStep;

public class PhysicalAttackArmor {

	private final Fluffer10kFun fluffer10kFun;

	public PhysicalAttackArmor(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private boolean armorWorked(final FightTempData data, final Weapon weapon) {
		return data.targetStats.armor > 0//
				&& !data.target.statuses.isStatus(FighterStatus.CHARMED)//
				&& !weapon.classes.contains(ItemClass.ARMOR_PIERCING);
	}

	private void checkArmorWorkedQuests(final FightTempData data) {
		final PlayerFighterData playerTarget = data.target.player();
		if (playerTarget == null) {
			return;
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.fight.serverId,
				playerTarget.userId);

		if (userData.rpg.questIsOnStep(QuestType.HERO_ACADEMY_QUEST, QuestStep.PALADIN_2)) {
			boolean hasShield = false;
			for (final String itemId : userData.rpg.eq.values()) {
				final Item item = fluffer10kFun.items.getItem(itemId);
				if (item != null && item.classes.contains(ItemClass.SHIELD)) {
					hasShield = true;
					break;
				}
			}

			if (hasShield) {
				fluffer10kFun.questUtils.questHeroAcademy().continuePaladin2Step(data.channel, userData,
						fluffer10kFun.apiUtils.getUser(playerTarget.userId));
			}
		}
	}

	public int armorDmgReduction(final FightTempData data, final Weapon weapon) {
		if (!armorWorked(data, weapon)) {
			return 0;
		}

		final int armorRoll = data.targetStats.getArmorPower();
		data.target.addExp(armorRoll * 3);
		checkArmorWorkedQuests(data);

		return armorRoll;
	}
}
