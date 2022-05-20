package bot.commands.rpg.fight.actions.physicalAttacks;

import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.Pair.pair;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Map;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightTempData;
import bot.commands.rpg.fight.RPGFightAction;
import bot.data.fight.FighterData.FighterType;
import bot.data.items.ItemClass;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;

public class WeaponsGetters {
	private static interface WeaponsGetter {
		List<Weapon> getWeapons(FightTempData data);
	}

	private static final Weapon fistsWeapon = new Weapon(WeaponType.STRENGTH, 0);
	private static final Weapon martialArtistFistsWeapon = new Weapon(WeaponType.STRENGTH, 3);

	private final Fluffer10kFun fluffer10kFun;

	public WeaponsGetters(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	private List<Weapon> getWeaponsAttack(final FightTempData data) {
		if (data.activeFighter.type != FighterType.PLAYER) {
			return getWeaponsAttackS(data);
		}

		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(data.activeFighter.player());
		final List<Weapon> weapons = userData.rpg.eq.values().stream()//
				.filter(itemId -> itemId != null)//
				.map(fluffer10kFun.items::getItem)//
				.filter(item -> item.classes.contains(ItemClass.WEAPON))//
				.map(Weapon::new)//
				.collect(toList());

		if (weapons.isEmpty()) {
			if (userData.blessings.blessingsObtained.contains(Blessing.MARTIAL_ARTIST)) {
				return asList(martialArtistFistsWeapon);
			}

			return asList(fistsWeapon);
		}

		return weapons;
	}

	private List<Weapon> getWeaponsAttackA(final FightTempData data) {
		return asList(new Weapon(WeaponType.AGILITY, data.activeFighterStats.agility / 4.0));
	}

	private List<Weapon> getWeaponsAttackDoublePunch(final FightTempData data) {
		return asList(new Weapon(WeaponType.STRENGTH, data.activeFighterStats.strength / 3.0), //
				new Weapon(WeaponType.STRENGTH, data.activeFighterStats.strength / 3.0));
	}

	private List<Weapon> getWeaponsAttackHammerSmash(final FightTempData data) {
		return asList(new Weapon(WeaponType.STRENGTH, data.activeFighterStats.strength));
	}

	private List<Weapon> getWeaponsAttackS(final FightTempData data) {
		return asList(new Weapon(WeaponType.STRENGTH, data.activeFighterStats.strength / 3.0));
	}

	// def makeGunSeriesMGWeapon(fighter, stats):
//	    singleShot = makeMonsterGirlWeapon(stats['agility'] / 5, ['AGILITY_BASED'])
//	    return [singleShot, singleShot, singleShot, singleShot]
	//
	// monsterGirlActionWeaponCreators = {
//	    'GUN_SERIES' : makeGunSeriesMGWeapon,
	// }

	private final Map<RPGFightAction, WeaponsGetter> weaponsGetters = toMap(//
			pair(RPGFightAction.ATTACK, this::getWeaponsAttack), //
			pair(RPGFightAction.ATTACK_A_1, this::getWeaponsAttackA), //
			pair(RPGFightAction.ATTACK_A_2, this::getWeaponsAttackA), //
			pair(RPGFightAction.ATTACK_A_3, this::getWeaponsAttackA), //
			pair(RPGFightAction.ATTACK_A_4, this::getWeaponsAttackA), //
			pair(RPGFightAction.ATTACK_DOUBLE_PUNCH, this::getWeaponsAttackDoublePunch), //
			pair(RPGFightAction.ATTACK_HAMMER_SMASH, this::getWeaponsAttackHammerSmash), //
			pair(RPGFightAction.ATTACK_S_1, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_2, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_3, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_4, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_5, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_6, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_7, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_8, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_9, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_10, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_11, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_12, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_13, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_14, this::getWeaponsAttackS), //
			pair(RPGFightAction.ATTACK_S_15, this::getWeaponsAttackS), //
			pair(RPGFightAction.SPECIAL_ATTACK_BASH, this::getWeaponsAttack), //
			pair(RPGFightAction.SPECIAL_ATTACK_DOUBLE_STRIKE, this::getWeaponsAttack), //
			pair(RPGFightAction.SPECIAL_ATTACK_GOUGE, this::getWeaponsAttack), //
			pair(RPGFightAction.SPECIAL_ATTACK_PRECISE_STRIKE, this::getWeaponsAttack));

	public List<Weapon> getWeapons(final FightTempData data, final RPGFightAction action) {
		return weaponsGetters.get(action).getWeapons(data);
	}
}
