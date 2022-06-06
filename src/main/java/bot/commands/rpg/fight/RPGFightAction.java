package bot.commands.rpg.fight;

import static bot.util.RandomUtils.getRandom;
import static java.util.Arrays.asList;

import java.util.List;

public enum RPGFightAction {
	ATLACH_NACHA_VENOM("%1$s bites %2$s and injects her venom!", //
			"%1$s injects venom into %2$s veins!"), //
	ATTACK("%1$s hits %2$s, dealing %3$d damage.", //
			"%1$s struck %2$s for %3$d damage."), //
	ATTACK_A_1("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s claws %2$s for %3$d damage.", //
			"%1$s punches %2$s for %3$d damage.", //
			"%1$s scratches %2$s for %3$d damage."), //
	ATTACK_A_2("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s whips %2$s with her tail for %3$d damage.", //
			"%1$s hits %2$s with her tail for %3$d damage.", //
			"%1$s punches %2$s for %3$d damage."), //
	ATTACK_A_3("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s punches %2$s for %3$d damage."), //
	ATTACK_A_4("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s punches %2$s, dealing %3$d damage.", //
			"%1$s punches %2$s with fiery fist for %3$d damage."), //
	ATTACK_DOUBLE_PUNCH("%1$s punches %2$s for %3$d damage."), //
	ATTACK_HAMMER_SMASH("%1$s powerfully smashes %2$s with her hammer for %3$d damage!"), //
	ATTACK_S_1("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s kicks %2$s for %3$d damage.", //
			"%1$s punches %2$s for %3$d damage."), //
	ATTACK_S_2("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s claws %2$s for %3$d damage.", //
			"%1$s punches %2$s for %3$d damage.", //
			"%1$s hits %2$s with sharp claws for %3$d damage."), //
	ATTACK_S_3("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s hits %2$s with slimy tentacle for %3$d damage.", //
			"%1$s slaps %2$s with slimy tentacle for %3$d damage."), //
	ATTACK_S_4("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s claws %2$s for %3$d damage.", //
			"%1$s punches %2$s for %3$d damage.", //
			"%1$s kicks %2$s for %3$d damage.", //
			"%1$s hits %2$s with her tail for %3$d damage."), //
	ATTACK_S_5("%1$s scratches %2$s for %3$d damage."), //
	ATTACK_S_6("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s slashes %2$s for %3$d damage.", //
			"%1$s cleaves %2$s for %3$d damage."), //
	ATTACK_S_7("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s punches %2$s for %3$d damage."), //
	ATTACK_S_8("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s punches %2$s for %3$d damage.", //
			"%1$s swings her hammer and deals %3$d damage to %2$s."), //
	ATTACK_S_9("%1$s hits %2$s with her hair and deals %3$d damage.", //
			"%1$s slaps %2$s with her hair for %3$d damage."), //
	ATTACK_S_10("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s whips %2$s for %3$d damage.", //
			"%1$s punches %2$s for %3$d damage."), //
	ATTACK_S_11("%1$s hits %2$s with her spider leg for %3$d damage.", //
			"%1$s stabs %2$s with her leg, dealing %3$d damage."), //
	ATTACK_S_12("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s claws %2$s for %3$d damage.", //
			"%1$s punches %2$s, dealing %3$d damage.", //
			"%1$s's goat arm bites %2$s for %3$d damage.", //
			"%1$s nails %2$s with her dragon arm's horns for %3$d damage."), //
	ATTACK_S_13("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s punches %2$s for %3$d damage.", //
			"%1$s smashes %2$s with her weapon for %3$d damage."), //
	ATTACK_S_14("%1$s hits %2$s with her sword for %3$d damage.", //
			"%1$s slashes %2$s with her sword for %3$d damage.", //
			"%1$s stabs %2$s with her sword, dealing %3$d damage."), //
	ATTACK_S_15("%1$s hits %2$s and deals %3$d damage.", //
			"%1$s punches %2$s for %3$d damage.", //
			"%1$s swings her sword and deals %3$d damage to %2$s."), //
	BANSHEE_WAIL("%1$s wails at %2$s!"), //
	BAROMETZ_COTTON("%1$s releases bits of scented cotton onto %2$s!"), //
	BASILISK_EYES("%1$s looks at %2$s with her evil eyes!"), //
	BUBBLES("%1$s started releasing bubbles!"), //
	BUNSHIN_NO_JUTSU("%1$s creates a clone!"), //
	CHARM("%1$s charms %2$s!"), //
	CHARM_CUTE("%1$s charms %2$s with her cuteness!", //
			"%1$s charms %2$s with her adorableness!"), //
	CHARM_FEATHERS("%1$s charms %2$s with her beautiful feathers!"), //
	CHARM_FINS("%1$s charms %2$s with her beautiful fins!"), //
	CHARM_KITSUNE("%1$s charms %2$s with her fluffy tails!"), //
	CHARM_MUSIC("%1$s charms %2$s with her music!"), //
	CHARM_PURE("%1$s charms %2$s with her purity!"), //
	CHARM_RATATOSKR("%1$s charms %2$s with her huge, fluffy tail!"), //
	CHARM_ROTTEN_BREATH("%1$s charms %2$s using her rotten breath!"), //
	CHARM_SPORES("%1$s releases spores at %2$s!"), //
	CHARM_SWEET_VOICE("%1$s charms %2$s with her sweet voice!"), //
	CHARM_WINGS("%1$s charms %2$s with her beautiful wings!"), //
	CURSE("%1$s curses %2$s!"), //
	ESCAPE("%1$s escapes!", //
			"%1$s runs away!"), //
	EQUIP_ITEM("%1$s equips %2$s."), //
	EQUIPPED_ITEM("%1$s equips %2$s."), //
	DORMOUSE_SLEEP("%1$s falls asleep."), //
	DORMOUSE_SLEEP_WAKE("%1$s wakes up."), //
	EGG_SHELL("%1$s constructs an eggshell!"), //
	FIND_WEAKNESS("%1$s found %2$s's weakness!"), //
	FLUFF("%1$s fluffs the %2$s."), //
	FRENZY("%1$s goes into frenzy!"), //
	FRENZY_GRIZZLY("%1$s licks honey off of her paw and goes into a frenzy!"), //
	GET_FREE("%1$s gets free from %2$s's hold!", //
			"%1$s gets free from %2$s's grasp!"), //
	GRAB("%1$s grabs %2$s!", //
			"%1$s tightly grabs %2$s!"), //
	GRAB_CLING("%1$s clings to %2$s!"), //
	GRAB_COIL("%1$s coils around %2$s!", //
			"%1$s coils tightly around %2$s!", //
			"%1$s wraps herself around %2$s!"), //
	GRAB_JUMP("%1$s bounces off the ground and attaches to %2$s!"), //
	GRAB_SLIME("%1$s grabs %2$s with slimy tentacles!", //
			"%1$s's tentacles wrap around %2$s!"), //
	GRAB_TOP("%1$s gets on top of %2$s!"), //
	GROW_UP("%1$s grows!"), //
	INTOXICATE("%1$s makes %2$s drink her wine and intoxicates them!"), //
	KEJOUROU_HAIR("%1$s wraps her hair around %2$s!"), //
	LEVEL_DRAIN_0("You feel weaker as the manticore drains your power!"), //
	LEVEL_DRAIN_1("You feel weaker as the minotaur drains your power!"), //
	LICK("%1$s licks all over %2$s's body!"), //
	MANTICORE_VENOM("%1$s stings %2$s with her tail and reduces their charm resistance!", //
			"%1$s injects her venom into %2$s and reduces their charm resistance!"), //
	MONSTER_LORD_RESTRICTION_0("The mysterious manticore wraps her tail around you and pins you down!"), //
	MOTHMAN_POWDER("%1$s flaps her wings and showers %2$s with her powder!"), //
	MUMMY_CURSE("%1$s curses %2$s with Mummy Curse!"), //
	PARALYZE("%1$s paralyzes %2$s with her tentacle!"), //
	PARALYZE_CHIMAERA("%1$s tail bites and paralyzes %2$s!"), //
	SAKE("%1$s drinks sake!"), //
	SALT("%1$s salts %2$s!"), //
	SHRINK_DOWN("%1$s shrinks %2$s!"), //
	SIREN_SONG("%1$s sings song for %2$s!"), //
	SPECIAL_ACTION_SHADOW_CLONE("%1$s creates a shadow clone!"), //
	SPECIAL_ATTACK_BASH("%1$s bashes %2$s, stunning them and dealing %3$d damage!"), //
	SPECIAL_ATTACK_DOUBLE_STRIKE("%1$s strikes %2$s quickly and deals %3$d damage.", //
			"%1$s does a quick attack on %2$s and deals %3$d damage."), //
	SPECIAL_ATTACK_GOUGE("%1$s gouges %2$s and deals %3$d damage!"), //
	SPECIAL_ATTACK_PRECISE_STRIKE("%1$s strikes %2$s in a weak spot and deals %3$d damage."), //
	SPELL("%1$s opens spellbook."), //
	SPELL_BLIZZARD("%1$s causes snow and ice to hail on %2$s, dealing %3$d damage.", //
			"%1$s creates snowstorm around %2$s, dealing %3$d damage."), //
	SPELL_FIERY_WEAPON("%1$s coats their weapon in flames.", //
			"%1$s casts flames on their weapon."), //
	SPELL_FIREBALL("%1$s throws a fireball at %2$s, dealing %3$d damage.", //
			"%1$s launches flaming ball at %2$s, dealing %3$d damage."), //
	SPELL_FORCE_HIT("%1$s hits %2$s with magic force, dealing %3$d damage."), //
	SPELL_FREEZE("%1$s freezes %2$s!"), //
	SPELL_FROST_ARMOR("%1$s conjures armor made of ice!", //
			"%1$s hides inside icy armor!"), //
	SPELL_HEAL("%1$s heals %2$s for %3$d hp."), //
	SPELL_HEAL_SISTERS("%1$s heals her sisters for %3$d hp."), //
	SPELL_HOLY_AURA("%1$s casts Holy Aura!"), //
	SPELL_ICE_BOLT("%1$s throws ice cone at %2$s, dealing %3$d damage.", //
			"%1$s throws ice bolt at %2$s, dealing %3$d damage.", //
			"%1$s launches icy projectile at %2$s, dealing %3$d damage."), //
	SPELL_LIFE_DRAIN("%1$s drains %2$s's vitality, dealing %3$d damage.", //
			"%1$s drains %2$s's life, dealing %3$d damage."), //
	SPELL_LIGHTNING("%1$s conjures lightning to hit %2$s, dealing %3$d damage.", //
			"%1$s makes thunder strike %2$s, dealing %3$d damage."), //
	SPELL_MAGIC_SHIELD("%1$s creates magic shield for protection.", //
			"%1$s conjures magic shield for protection."), //
	SPELL_METEORITE("%1$s conjures meteorite to hit %2$s, dealing %3$d damage.", //
			"%1$s makes huge rock fall on %2$s, dealing %3$d damage."), //
	SPELL_PARALYZING_THUNDER("%1$s conjures lightning to hit %2$s, dealing %3$d damage and paralyzing them.", //
			"%1$s makes thunder strike %2$s, dealing %3$d damage and paralyzing them."), //
	SPELL_PETRIFY("%1$s petrifies %2$s!", //
			"%1$s turns %2$s into stone!"), //
	SPELL_QUADRUPLE_FLAME("%1$s casts a flaming missile at %2$s, dealing %3$d damage.", //
			"%1$s throws a small fireball at %2$s, dealing %3$d damage.", //
			"%1$s conjures flame, burning %2$s and dealing %3$d damage.", //
			"%1$s burns %2$s, dealing %3$d damage.", //
			"%1$s casts flame on %2$s, dealing %3$d damage.", //
			"%1$s fires up %2$s, dealing %3$d damage."), //
	SPELL_RAGE("%1$s goes into rage!", //
			"%1$s gets enraged!"), //
	SPELL_RAIJU_LIGHTNING("%1$s conjures shocking lightning to hit %2$s, dealing %3$d damage.", //
			"%1$s makes thunder strike %2$s, dealing %3$d damage."), //
	SPELL_SLEEP("%1$s puts %2$s to sleep!"), //
	SPELL_SPEED_OF_WIND("%1$s gets swift like the wind!"), //
	SPELL_STONE_SKIN("%1$s's skin hardens!"), //
	SPELL_WHIRLPOOL("%1$s conjures whirlpool around %2$s, dealing %3$d damage.", //
			"%1$s swirls water around %2$s, dealing %3$d damage.", //
			"%1$s conjures whirlpool, dealing %3$d damage to %2$s."), //
	SUCCUBUS_NOSTRUM("%1$s drinks Succubus Nostrum."), //
	SURRENDER("%1$s surrenders!"), //
	USE_ITEM("%1$s uses %2$s."), //
	USED_ITEM("%1$s uses %2$s."), //
	WAIT("%1$s waits."), //
	WERESHEEP_WOOL("%1$s rubs her body against %2$s and makes them fall asleep!"), //
	WRAP_IN_WEB("%1$s wraps %2$s in her silk!", //
			"%1$s wraps %2$s in her web!");

	public final List<String> descriptions;

	private RPGFightAction() {
		descriptions = null;
	}

	private RPGFightAction(final String... descriptions) {
		this.descriptions = asList(descriptions);
	}

	public String description(final Object... args) {
		return String.format(getRandom(descriptions), args);
	}
}
