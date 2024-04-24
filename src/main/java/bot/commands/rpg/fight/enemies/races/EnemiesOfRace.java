package bot.commands.rpg.fight.enemies.races;

import static bot.data.MonsterGirls.MonsterGirlRace.*;
import static bot.util.CollectionUtils.toMap;
import static bot.util.Utils.Pair.pair;

import java.util.Map;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.enemies.RPGEnemies;
import bot.commands.rpg.fight.enemies.RPGEnemyMonsterGirlDataBuilder;
import bot.data.MonsterGirls.MonsterGirlRace;

public abstract class EnemiesOfRace {
	private static final Map<MonsterGirlRace, EnemiesOfRace> enemyAdders = toMap(//
			pair(AKANAME, new Akaname()), //
			pair(ALICE, new Alice()), //
			pair(ALP, new Alp()), //
			pair(ALRAUNE, new Alraune()), //
			pair(ANGEL, new Angel()), //
			pair(ANT_ARACHNE, new AntArachne()), //
			pair(ANUBIS, new Anubis()), //
			pair(APOPHIS, new Apophis()), //
			pair(APSARA, new Apsara()), //
			pair(ARACHNE, new Arachne()), //
			pair(ATLACH_NACHA, new AtlachNacha()), //
			pair(AUTOMATON, new Automaton()), //
			pair(BANSHEE, new Banshee()), //
			pair(BAPHOMET, new Baphomet()), //
			pair(BAROMETZ, new Barometz()), //
			pair(BASILISK, new Basilisk()), //
			pair(BEELZEBUB, new Beelzebub()), //
			pair(BLACK_HARPY, new BlackHarpy()), //
			pair(BUBBLE_SLIME, new BubbleSlime()), //
			pair(BUNYIP, new Bunyip()), //
			pair(CAIT_SITH, new CaitSith()), //
			pair(CHARYBDIS, new Charybdis()), //
			pair(CHESHIRE_CAT, new CheshireCat()), //
			pair(CHIMAERA, new Chimaera()), //
			pair(COCKATRICE, new Cockatrice()), //
			pair(CROW_TENGU, new CrowTengu()), //
			pair(CU_SITH, new CuSith()), //
			pair(CURSED_SWORD, new CursedSword()), //
			pair(DARK_ELF, new DarkElf()), //
			pair(DARK_PRIEST, new DarkPriest()), //
			pair(DARK_SLIME, new DarkSlime()), //
			pair(DORMOUSE, new Dormouse()), //
			pair(DOROME, new Dorome()), //
			pair(DRAGON_ZOMBIE, new DragonZombie()), //
			pair(DWARF, new Dwarf()), //
			pair(FAIRY, new Fairy()), //
			pair(GHOUL, new Ghoul()), //
			pair(GIANT_SLUG, new GiantSlug()), //
			pair(GLACIES, new Glacies()), //
			pair(GOBLIN, new Goblin()), //
			pair(GRIFFON, new Griffon()), //
			pair(GRIZZLY, new Grizzly()), //
			pair(GYOUBU_DANUKI, new GyoubuDanuki()), //
			pair(HAKUTAKU, new Hakutaku()), //
			pair(HELLHOUND, new Hellhound()), //
			pair(HINEZUMI, new Hinezumi()), //
			pair(HOBGOBLIN, new Hobgoblin()), //
			pair(HOLSTAUR, new Holstaur()), //
			pair(HUMPTY_EGG, new HumptyEgg()), //
			pair(ICE_QUEEN, new IceQueen()), //
			pair(IMP, new Imp()), //
			pair(INARI, new Inari()), //
			pair(JINKO, new Jinko()), //
			pair(KAKUEN, new Kakuen()), //
			pair(KAMAITACHI, new Kamaitachi()), //
			pair(KAPPA, new Kappa()), //
			pair(KEJOUROU, new Kejourou()), //
			pair(KIKIMORA, new Kikimora()), //
			pair(KITSUNE_BI, new KitsuneBi()), //
			pair(KITSUNE_TSUKI, new KitsuneTsuki()), //
			pair(KOBOLD, new Kobold()), //
			pair(LARGE_MOUSE, new LargeMouse()), //
			pair(LESSER_SUCCUBUS, new LesserSuccubus()), //
			pair(LILIM, new Lilim()), //
			pair(LIVING_DOLL, new LivingDoll()), //
			pair(LIZARDMAN, new Lizardman()), //
			pair(MANTICORE, new Manticore()), //
			pair(MARCH_HARE, new MarchHare()), //
			pair(MERMAID, new Mermaid()), //
			pair(MERROW, new Merrow()), //
			pair(MINOTAUR, new Minotaur()), //
			pair(MOTHMAN, new Mothman()), //
			pair(MUMMY, new Mummy()), //
			pair(MYCONID, new Myconid()), //
			pair(NEKOMATA, new Nekomata()), //
			pair(NIGHTMARE, new Nightmare()), //
			pair(OCELOMEH, new Ocelomeh()), //
			pair(OGRE, new Ogre()), //
			pair(ORC, new Orc()), //
			pair(OWL_MAGE, new OwlMage()), //
			pair(PAPILLON, new Papillon()), //
			pair(PARASITE_SLIME_SLIME_CARRIER, new ParasiteSlimeSlimeCarrier()), //
			pair(PIXIE, new Pixie()), //
			pair(QUEEN_SLIME, new QueenSlime()), //
			pair(RAIJU, new Raiju()), //
			pair(RATATOSKR, new Ratatoskr()), //
			pair(REDCAP, new Redcap()), //
			pair(RED_ONI, new RedOni()), //
			pair(RED_SLIME, new RedSlime()), //
			pair(SANDWORM, new Sandworm()), //
			pair(SATYROS, new Satyros()), //
			pair(SEA_BISHOP, new SeaBishop()), //
			pair(SEA_SLIME, new SeaSlime()), //
			pair(SHOGGOTH, new Shoggoth()), //
			pair(SIREN, new Siren()), //
			pair(SKELETON, new Skeleton()), //
			pair(SLIME, new Slime()), //
			pair(SPHINX, new Sphinx()), //
			pair(SUCCUBUS, new Succubus()), //
			pair(THUNDERBIRD, new Thunderbird()), //
			pair(TRITONIA, new Tritonia()), //
			pair(WEREBAT, new Werebat()), //
			pair(WERECAT, new Werecat()), //
			pair(WERERABBIT, new Wererabbit()), //
			pair(WERESHEEP, new Weresheep()), //
			pair(WEREWOLF, new Werewolf()), //
			pair(WITCH, new Witch()), //
			pair(WURM, new Wurm()), //
			pair(YOUKO, new Youko()), //
			pair(ZOMBIE, new Zombie()));

	private static void validateAndAddEnemiesRace(final MonsterGirlRace race, final EnemiesOfRace enemiesOfRace,
			final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		if (enemiesOfRace.race != race) {
			throw new RuntimeException("wrong monster girl race! is " + enemiesOfRace.race + " and should be " + race
					+ "! " + enemiesOfRace.getClass().getName());
		}

		enemiesOfRace.addEnemies(fluffer10kFun, rpgEnemies);
	}

	public static void addAllEnemies(final Fluffer10kFun fluffer10kFun, final RPGEnemies rpgEnemies) {
		for (final MonsterGirlRace race : MonsterGirlRace.values()) {
			final EnemiesOfRace enemiesOfRace = enemyAdders.get(race);
			if (enemiesOfRace != null) {
				validateAndAddEnemiesRace(race, enemiesOfRace, fluffer10kFun, rpgEnemies);
			} else if (fluffer10kFun.apiUtils.config.getBoolean("debug")) {
				System.out.println("no enemy of race " + race.name());
			}
		}
	}

	public final MonsterGirlRace race;

	public EnemiesOfRace(final MonsterGirlRace race) {
		this.race = race;
	}

	protected abstract void addEnemies(Fluffer10kFun fluffer10kFun, RPGEnemies rpgEnemies);

	protected final RPGEnemyMonsterGirlDataBuilder makeBuilderOld(final String id) {
		if (!id.startsWith(race.name())) {
			throw new RuntimeException("wrong monster girl id! is " + id + " for race " + race);
		}
		return new RPGEnemyMonsterGirlDataBuilder(id, race);
	}

	protected final RPGEnemyMonsterGirlDataBuilder makeStandardBuilder(final int id, //
			final int strength, final int strengthMultiplier, //
			final int agility, final int agilityMultiplier, //
			final int intelligence, final int intelligenceMultiplier, //
			final int hp, final int hpMultiplier) {
		return makeStandardBuilder(id)//
				.strength(strength + id * strengthMultiplier)//
				.agility(agility + id * agilityMultiplier)//
				.intelligence(intelligence + id * intelligenceMultiplier)//
				.baseHp(hp + id * hpMultiplier);
	}

	protected final RPGEnemyMonsterGirlDataBuilder makeStandardBuilder(final int id) {
		return makeBuilder(id).standard();
	}

	private final RPGEnemyMonsterGirlDataBuilder makeBuilder(final int id) {
		return makeBuilder(race.name() + "_" + Integer.toString(id));
	}

	protected final RPGEnemyMonsterGirlDataBuilder makeBuilder(final String id) {
		return new RPGEnemyMonsterGirlDataBuilder(id, race);
	}
}
