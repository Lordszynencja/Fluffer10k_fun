package bot.commands.rpg.fight.actions.physicalAttacks;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.List;

import bot.data.items.Item;
import bot.data.items.ItemClass;

public class Weapon {
	public WeaponType type;
	public double dmgRoll;
	public int magicDamage;
	public int criticalStrikeBonus;
	public List<ItemClass> classes;

	public Weapon(final Item item) {
		if (item.classes.contains(ItemClass.STRENGTH_BASED)) {
			type = WeaponType.STRENGTH;
		}
		if (item.classes.contains(ItemClass.AGILITY_BASED)) {
			type = WeaponType.AGILITY;
		}

		dmgRoll = item.damageRollBonus;
		magicDamage = item.magicOnHitDamageBonus;
		criticalStrikeBonus = item.criticalStrikeBonus;
		classes = new ArrayList<>(item.classes);
	}

	public Weapon(final WeaponType type, final double dmgRoll) {
		this.type = type;
		this.dmgRoll = dmgRoll;
		magicDamage = 0;
		criticalStrikeBonus = 0;
		classes = asList();
	}

	public Weapon type(final WeaponType type) {
		this.type = type;
		return this;
	}

	public Weapon dmgRoll(final double dmgRoll) {
		this.dmgRoll = dmgRoll;
		return this;
	}

	public Weapon magicDamage(final int magicDamage) {
		this.magicDamage = magicDamage;
		return this;
	}

	public Weapon criticalStrikeBonus(final int criticalStrikeBonus) {
		this.criticalStrikeBonus = criticalStrikeBonus;
		return this;
	}

	public Weapon classes(final ItemClass... classes) {
		this.classes = asList(classes);
		return this;
	}
}