package bot.commands.rpg.exploration;

import static bot.commands.rpg.fight.enemies.data.races.Akaname.AKANAME_0;
import static bot.commands.rpg.fight.enemies.data.races.Akaname.AKANAME_1;
import static bot.commands.rpg.fight.enemies.data.races.Akaname.AKANAME_2;
import static bot.commands.rpg.fight.enemies.data.races.Akaname.AKANAME_3;
import static bot.commands.rpg.fight.enemies.data.races.Akaname.AKANAME_4;
import static bot.commands.rpg.fight.enemies.data.races.Akaname.AKANAME_5;
import static bot.commands.rpg.fight.enemies.data.races.Alice.ALICE_0;
import static bot.commands.rpg.fight.enemies.data.races.Alice.ALICE_1;
import static bot.commands.rpg.fight.enemies.data.races.Alice.ALICE_2;
import static bot.commands.rpg.fight.enemies.data.races.Alice.ALICE_3;
import static bot.commands.rpg.fight.enemies.data.races.Alice.ALICE_4;
import static bot.commands.rpg.fight.enemies.data.races.Alice.ALICE_5;
import static bot.commands.rpg.fight.enemies.data.races.Apsara.APSARA_2;
import static bot.commands.rpg.fight.enemies.data.races.Arachne.ARACHNE_0;
import static bot.commands.rpg.fight.enemies.data.races.Arachne.ARACHNE_1;
import static bot.commands.rpg.fight.enemies.data.races.Arachne.ARACHNE_2;
import static bot.commands.rpg.fight.enemies.data.races.Arachne.ARACHNE_3;
import static bot.commands.rpg.fight.enemies.data.races.Arachne.ARACHNE_4;
import static bot.commands.rpg.fight.enemies.data.races.Arachne.ARACHNE_5;
import static bot.commands.rpg.fight.enemies.data.races.AtlachNacha.ATLACH_NACHA_0;
import static bot.commands.rpg.fight.enemies.data.races.AtlachNacha.ATLACH_NACHA_1;
import static bot.commands.rpg.fight.enemies.data.races.AtlachNacha.ATLACH_NACHA_2;
import static bot.commands.rpg.fight.enemies.data.races.AtlachNacha.ATLACH_NACHA_3;
import static bot.commands.rpg.fight.enemies.data.races.AtlachNacha.ATLACH_NACHA_4;
import static bot.commands.rpg.fight.enemies.data.races.AtlachNacha.ATLACH_NACHA_5;
import static bot.commands.rpg.fight.enemies.data.races.Baphomet.BAPHOMET_0;
import static bot.commands.rpg.fight.enemies.data.races.Baphomet.BAPHOMET_1;
import static bot.commands.rpg.fight.enemies.data.races.Baphomet.BAPHOMET_2;
import static bot.commands.rpg.fight.enemies.data.races.Baphomet.BAPHOMET_3;
import static bot.commands.rpg.fight.enemies.data.races.Baphomet.BAPHOMET_4;
import static bot.commands.rpg.fight.enemies.data.races.Baphomet.BAPHOMET_5;
import static bot.commands.rpg.fight.enemies.data.races.Bunyip.BUNYIP_0;
import static bot.commands.rpg.fight.enemies.data.races.Bunyip.BUNYIP_1;
import static bot.commands.rpg.fight.enemies.data.races.Bunyip.BUNYIP_2;
import static bot.commands.rpg.fight.enemies.data.races.Bunyip.BUNYIP_3;
import static bot.commands.rpg.fight.enemies.data.races.Bunyip.BUNYIP_4;
import static bot.commands.rpg.fight.enemies.data.races.Bunyip.BUNYIP_5;
import static bot.commands.rpg.fight.enemies.data.races.CheshireCat.CHESHIRE_CAT_0;
import static bot.commands.rpg.fight.enemies.data.races.CheshireCat.CHESHIRE_CAT_1;
import static bot.commands.rpg.fight.enemies.data.races.CheshireCat.CHESHIRE_CAT_2;
import static bot.commands.rpg.fight.enemies.data.races.CheshireCat.CHESHIRE_CAT_3;
import static bot.commands.rpg.fight.enemies.data.races.CheshireCat.CHESHIRE_CAT_4;
import static bot.commands.rpg.fight.enemies.data.races.CheshireCat.CHESHIRE_CAT_5;
import static bot.commands.rpg.fight.enemies.data.races.Chimaera.CHIMAERA_0;
import static bot.commands.rpg.fight.enemies.data.races.Chimaera.CHIMAERA_1;
import static bot.commands.rpg.fight.enemies.data.races.Chimaera.CHIMAERA_2;
import static bot.commands.rpg.fight.enemies.data.races.Chimaera.CHIMAERA_3;
import static bot.commands.rpg.fight.enemies.data.races.Chimaera.CHIMAERA_4;
import static bot.commands.rpg.fight.enemies.data.races.Chimaera.CHIMAERA_5;
import static bot.commands.rpg.fight.enemies.data.races.CrowTengu.CROW_TENGU_0;
import static bot.commands.rpg.fight.enemies.data.races.CrowTengu.CROW_TENGU_1;
import static bot.commands.rpg.fight.enemies.data.races.CrowTengu.CROW_TENGU_2;
import static bot.commands.rpg.fight.enemies.data.races.CrowTengu.CROW_TENGU_3;
import static bot.commands.rpg.fight.enemies.data.races.CrowTengu.CROW_TENGU_4;
import static bot.commands.rpg.fight.enemies.data.races.CrowTengu.CROW_TENGU_5;
import static bot.commands.rpg.fight.enemies.data.races.DragonZombie.DRAGON_ZOMBIE_0;
import static bot.commands.rpg.fight.enemies.data.races.DragonZombie.DRAGON_ZOMBIE_1;
import static bot.commands.rpg.fight.enemies.data.races.DragonZombie.DRAGON_ZOMBIE_2;
import static bot.commands.rpg.fight.enemies.data.races.DragonZombie.DRAGON_ZOMBIE_3;
import static bot.commands.rpg.fight.enemies.data.races.DragonZombie.DRAGON_ZOMBIE_4;
import static bot.commands.rpg.fight.enemies.data.races.DragonZombie.DRAGON_ZOMBIE_5;
import static bot.commands.rpg.fight.enemies.data.races.Fairy.FAIRY_0;
import static bot.commands.rpg.fight.enemies.data.races.Fairy.FAIRY_1;
import static bot.commands.rpg.fight.enemies.data.races.Fairy.FAIRY_2;
import static bot.commands.rpg.fight.enemies.data.races.Fairy.FAIRY_3;
import static bot.commands.rpg.fight.enemies.data.races.Fairy.FAIRY_4;
import static bot.commands.rpg.fight.enemies.data.races.Fairy.FAIRY_5;
import static bot.commands.rpg.fight.enemies.data.races.Glacies.GLACIES_0;
import static bot.commands.rpg.fight.enemies.data.races.Glacies.GLACIES_1;
import static bot.commands.rpg.fight.enemies.data.races.Glacies.GLACIES_2;
import static bot.commands.rpg.fight.enemies.data.races.Glacies.GLACIES_3;
import static bot.commands.rpg.fight.enemies.data.races.Glacies.GLACIES_4;
import static bot.commands.rpg.fight.enemies.data.races.Glacies.GLACIES_5;
import static bot.commands.rpg.fight.enemies.data.races.Goblin.GOBLIN_0;
import static bot.commands.rpg.fight.enemies.data.races.Goblin.GOBLIN_1;
import static bot.commands.rpg.fight.enemies.data.races.Goblin.GOBLIN_2;
import static bot.commands.rpg.fight.enemies.data.races.Goblin.GOBLIN_3;
import static bot.commands.rpg.fight.enemies.data.races.Goblin.GOBLIN_4;
import static bot.commands.rpg.fight.enemies.data.races.Goblin.GOBLIN_5;
import static bot.commands.rpg.fight.enemies.data.races.Hellhound.HELLHOUND_0;
import static bot.commands.rpg.fight.enemies.data.races.Hellhound.HELLHOUND_1;
import static bot.commands.rpg.fight.enemies.data.races.Hellhound.HELLHOUND_2;
import static bot.commands.rpg.fight.enemies.data.races.Hellhound.HELLHOUND_3;
import static bot.commands.rpg.fight.enemies.data.races.Hellhound.HELLHOUND_4;
import static bot.commands.rpg.fight.enemies.data.races.Hellhound.HELLHOUND_5;
import static bot.commands.rpg.fight.enemies.data.races.IceQueen.ICE_QUEEN_0;
import static bot.commands.rpg.fight.enemies.data.races.IceQueen.ICE_QUEEN_1;
import static bot.commands.rpg.fight.enemies.data.races.IceQueen.ICE_QUEEN_2;
import static bot.commands.rpg.fight.enemies.data.races.IceQueen.ICE_QUEEN_3;
import static bot.commands.rpg.fight.enemies.data.races.IceQueen.ICE_QUEEN_4;
import static bot.commands.rpg.fight.enemies.data.races.IceQueen.ICE_QUEEN_5;
import static bot.commands.rpg.fight.enemies.data.races.Jinko.JINKO_0;
import static bot.commands.rpg.fight.enemies.data.races.Jinko.JINKO_1;
import static bot.commands.rpg.fight.enemies.data.races.Jinko.JINKO_2;
import static bot.commands.rpg.fight.enemies.data.races.Jinko.JINKO_3;
import static bot.commands.rpg.fight.enemies.data.races.Jinko.JINKO_4;
import static bot.commands.rpg.fight.enemies.data.races.Jinko.JINKO_5;
import static bot.commands.rpg.fight.enemies.data.races.Kamaitachi.KAMAITACHI_0;
import static bot.commands.rpg.fight.enemies.data.races.Kamaitachi.KAMAITACHI_1;
import static bot.commands.rpg.fight.enemies.data.races.Kamaitachi.KAMAITACHI_2;
import static bot.commands.rpg.fight.enemies.data.races.Kamaitachi.KAMAITACHI_3;
import static bot.commands.rpg.fight.enemies.data.races.Kamaitachi.KAMAITACHI_4;
import static bot.commands.rpg.fight.enemies.data.races.Kamaitachi.KAMAITACHI_5;
import static bot.commands.rpg.fight.enemies.data.races.Kobold.KOBOLD_0;
import static bot.commands.rpg.fight.enemies.data.races.Kobold.KOBOLD_1;
import static bot.commands.rpg.fight.enemies.data.races.Kobold.KOBOLD_2;
import static bot.commands.rpg.fight.enemies.data.races.Kobold.KOBOLD_3;
import static bot.commands.rpg.fight.enemies.data.races.Kobold.KOBOLD_4;
import static bot.commands.rpg.fight.enemies.data.races.Kobold.KOBOLD_5;
import static bot.commands.rpg.fight.enemies.data.races.Lilim.LILIM_0;
import static bot.commands.rpg.fight.enemies.data.races.Lilim.LILIM_1;
import static bot.commands.rpg.fight.enemies.data.races.Lilim.LILIM_2;
import static bot.commands.rpg.fight.enemies.data.races.Lilim.LILIM_3;
import static bot.commands.rpg.fight.enemies.data.races.Lilim.LILIM_4;
import static bot.commands.rpg.fight.enemies.data.races.Lilim.LILIM_5;
import static bot.commands.rpg.fight.enemies.data.races.Lizardman.LIZARDMAN_0;
import static bot.commands.rpg.fight.enemies.data.races.Lizardman.LIZARDMAN_1;
import static bot.commands.rpg.fight.enemies.data.races.Lizardman.LIZARDMAN_2;
import static bot.commands.rpg.fight.enemies.data.races.Lizardman.LIZARDMAN_3;
import static bot.commands.rpg.fight.enemies.data.races.Lizardman.LIZARDMAN_4;
import static bot.commands.rpg.fight.enemies.data.races.Lizardman.LIZARDMAN_5;
import static bot.commands.rpg.fight.enemies.data.races.Manticore.MANTICORE_0;
import static bot.commands.rpg.fight.enemies.data.races.Manticore.MANTICORE_1;
import static bot.commands.rpg.fight.enemies.data.races.Manticore.MANTICORE_2;
import static bot.commands.rpg.fight.enemies.data.races.Manticore.MANTICORE_3;
import static bot.commands.rpg.fight.enemies.data.races.Manticore.MANTICORE_4;
import static bot.commands.rpg.fight.enemies.data.races.Manticore.MANTICORE_5;
import static bot.commands.rpg.fight.enemies.data.races.Mermaid.MERMAID_2;
import static bot.commands.rpg.fight.enemies.data.races.Minotaur.MINOTAUR_2;
import static bot.commands.rpg.fight.enemies.data.races.Mummy.MUMMY_0;
import static bot.commands.rpg.fight.enemies.data.races.Mummy.MUMMY_1;
import static bot.commands.rpg.fight.enemies.data.races.Mummy.MUMMY_2;
import static bot.commands.rpg.fight.enemies.data.races.Mummy.MUMMY_3;
import static bot.commands.rpg.fight.enemies.data.races.Mummy.MUMMY_4;
import static bot.commands.rpg.fight.enemies.data.races.Mummy.MUMMY_5;
import static bot.commands.rpg.fight.enemies.data.races.Myconid.MYCONID_2;
import static bot.commands.rpg.fight.enemies.data.races.Nightmare.NIGHTMARE_2;
import static bot.commands.rpg.fight.enemies.data.races.Ocelomeh.OCELOMEH_0;
import static bot.commands.rpg.fight.enemies.data.races.Ocelomeh.OCELOMEH_1;
import static bot.commands.rpg.fight.enemies.data.races.Ocelomeh.OCELOMEH_2;
import static bot.commands.rpg.fight.enemies.data.races.Ocelomeh.OCELOMEH_3;
import static bot.commands.rpg.fight.enemies.data.races.Ocelomeh.OCELOMEH_4;
import static bot.commands.rpg.fight.enemies.data.races.Ocelomeh.OCELOMEH_5;
import static bot.commands.rpg.fight.enemies.data.races.Ogre.OGRE_2;
import static bot.commands.rpg.fight.enemies.data.races.Orc.ORC_2;
import static bot.commands.rpg.fight.enemies.data.races.Orc.ORC_3;
import static bot.commands.rpg.fight.enemies.data.races.OwlMage.OWL_MAGE_0;
import static bot.commands.rpg.fight.enemies.data.races.OwlMage.OWL_MAGE_1;
import static bot.commands.rpg.fight.enemies.data.races.OwlMage.OWL_MAGE_2;
import static bot.commands.rpg.fight.enemies.data.races.OwlMage.OWL_MAGE_3;
import static bot.commands.rpg.fight.enemies.data.races.OwlMage.OWL_MAGE_4;
import static bot.commands.rpg.fight.enemies.data.races.OwlMage.OWL_MAGE_5;
import static bot.commands.rpg.fight.enemies.data.races.Papillon.PAPILLON_2;
import static bot.commands.rpg.fight.enemies.data.races.ParasiteSlimeSlimeCarrier.PARASITE_SLIME_SLIME_CARRIER_2;
import static bot.commands.rpg.fight.enemies.data.races.Pixie.PIXIE_0;
import static bot.commands.rpg.fight.enemies.data.races.Pixie.PIXIE_1;
import static bot.commands.rpg.fight.enemies.data.races.Pixie.PIXIE_2;
import static bot.commands.rpg.fight.enemies.data.races.Pixie.PIXIE_3;
import static bot.commands.rpg.fight.enemies.data.races.Pixie.PIXIE_4;
import static bot.commands.rpg.fight.enemies.data.races.Pixie.PIXIE_5;
import static bot.commands.rpg.fight.enemies.data.races.QueenSlime.QUEEN_SLIME_2;
import static bot.commands.rpg.fight.enemies.data.races.QueenSlime.QUEEN_SLIME_3;
import static bot.commands.rpg.fight.enemies.data.races.Raiju.RAIJU_2;
import static bot.commands.rpg.fight.enemies.data.races.Ratatoskr.RATATOSKR_0;
import static bot.commands.rpg.fight.enemies.data.races.Ratatoskr.RATATOSKR_1;
import static bot.commands.rpg.fight.enemies.data.races.Ratatoskr.RATATOSKR_2;
import static bot.commands.rpg.fight.enemies.data.races.Ratatoskr.RATATOSKR_3;
import static bot.commands.rpg.fight.enemies.data.races.Ratatoskr.RATATOSKR_4;
import static bot.commands.rpg.fight.enemies.data.races.Ratatoskr.RATATOSKR_5;
import static bot.commands.rpg.fight.enemies.data.races.RedOni.RED_ONI_2;
import static bot.commands.rpg.fight.enemies.data.races.RedOni.RED_ONI_3;
import static bot.commands.rpg.fight.enemies.data.races.Redcap.REDCAP_2;
import static bot.commands.rpg.fight.enemies.data.races.SeaBishop.SEA_BISHOP_2;
import static bot.commands.rpg.fight.enemies.data.races.Siren.SIREN_2;
import static bot.commands.rpg.fight.enemies.data.races.Siren.SIREN_3;
import static bot.commands.rpg.fight.enemies.data.races.Siren.SIREN_4;
import static bot.commands.rpg.fight.enemies.data.races.Skeleton.SKELETON_2;
import static bot.commands.rpg.fight.enemies.data.races.Slime.SLIME_2;
import static bot.commands.rpg.fight.enemies.data.races.Sphinx.SPHINX_2;
import static bot.commands.rpg.fight.enemies.data.races.Succubus.SUCCUBUS_0;
import static bot.commands.rpg.fight.enemies.data.races.Succubus.SUCCUBUS_1;
import static bot.commands.rpg.fight.enemies.data.races.Succubus.SUCCUBUS_2;
import static bot.commands.rpg.fight.enemies.data.races.Succubus.SUCCUBUS_3;
import static bot.commands.rpg.fight.enemies.data.races.Succubus.SUCCUBUS_4;
import static bot.commands.rpg.fight.enemies.data.races.Succubus.SUCCUBUS_5;
import static bot.commands.rpg.fight.enemies.data.races.Thunderbird.THUNDERBIRD_0;
import static bot.commands.rpg.fight.enemies.data.races.Thunderbird.THUNDERBIRD_1;
import static bot.commands.rpg.fight.enemies.data.races.Thunderbird.THUNDERBIRD_2;
import static bot.commands.rpg.fight.enemies.data.races.Thunderbird.THUNDERBIRD_3;
import static bot.commands.rpg.fight.enemies.data.races.Thunderbird.THUNDERBIRD_4;
import static bot.commands.rpg.fight.enemies.data.races.Thunderbird.THUNDERBIRD_5;
import static bot.commands.rpg.fight.enemies.data.races.Werecat.WERECAT_2;
import static bot.commands.rpg.fight.enemies.data.races.Werewolf.WEREWOLF_2;
import static bot.commands.rpg.fight.enemies.data.races.Werewolf.WEREWOLF_3;
import static bot.commands.rpg.fight.enemies.data.races.Witch.WITCH_0;
import static bot.commands.rpg.fight.enemies.data.races.Witch.WITCH_1;
import static bot.commands.rpg.fight.enemies.data.races.Witch.WITCH_2;
import static bot.commands.rpg.fight.enemies.data.races.Witch.WITCH_3;
import static bot.commands.rpg.fight.enemies.data.races.Witch.WITCH_4;
import static bot.commands.rpg.fight.enemies.data.races.Witch.WITCH_5;
import static bot.commands.rpg.fight.enemies.data.races.Wurm.WURM_0;
import static bot.commands.rpg.fight.enemies.data.races.Wurm.WURM_1;
import static bot.commands.rpg.fight.enemies.data.races.Wurm.WURM_2;
import static bot.commands.rpg.fight.enemies.data.races.Wurm.WURM_3;
import static bot.commands.rpg.fight.enemies.data.races.Wurm.WURM_4;
import static bot.commands.rpg.fight.enemies.data.races.Wurm.WURM_5;
import static bot.commands.rpg.fight.enemies.data.races.Youko.YOUKO_2;
import static bot.commands.rpg.fight.enemies.data.races.Zombie.ZOMBIE_0;
import static bot.commands.rpg.fight.enemies.data.races.Zombie.ZOMBIE_1;
import static bot.commands.rpg.fight.enemies.data.races.Zombie.ZOMBIE_2;
import static bot.commands.rpg.fight.enemies.data.races.Zombie.ZOMBIE_3;
import static bot.commands.rpg.fight.enemies.data.races.Zombie.ZOMBIE_4;
import static bot.commands.rpg.fight.enemies.data.races.Zombie.ZOMBIE_5;
import static bot.data.items.loot.LootTable.weightedD;
import static bot.util.CollectionUtils.addToListOnMap;
import static bot.util.CollectionUtils.toSet;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandom;
import static bot.util.RandomUtils.getRandomBoolean;
import static bot.util.Utils.fixString;
import static bot.util.Utils.Pair.pair;
import static bot.util.apis.MessageUtils.sendEphemeralMessage;
import static java.lang.Math.abs;
import static java.lang.Math.max;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.javacord.api.interaction.SlashCommandInteraction;

import bot.Fluffer10kFun;
import bot.commands.rpg.exploration.CommandExplore.ExplorationEventHandler;
import bot.commands.rpg.fight.enemies.RPGEnemyData;
import bot.commands.rpg.fight.enemies.data.races.Angel;
import bot.commands.rpg.fight.enemies.data.races.AntArachne;
import bot.commands.rpg.fight.enemies.data.races.Anubis;
import bot.commands.rpg.fight.enemies.data.races.Apsara;
import bot.commands.rpg.fight.enemies.data.races.Banshee;
import bot.commands.rpg.fight.enemies.data.races.Barometz;
import bot.commands.rpg.fight.enemies.data.races.Basilisk;
import bot.commands.rpg.fight.enemies.data.races.Beelzebub;
import bot.commands.rpg.fight.enemies.data.races.BubbleSlime;
import bot.commands.rpg.fight.enemies.data.races.CaitSith;
import bot.commands.rpg.fight.enemies.data.races.Charybdis;
import bot.commands.rpg.fight.enemies.data.races.Cockatrice;
import bot.commands.rpg.fight.enemies.data.races.CuSith;
import bot.commands.rpg.fight.enemies.data.races.CursedSword;
import bot.commands.rpg.fight.enemies.data.races.DarkElf;
import bot.commands.rpg.fight.enemies.data.races.DarkPriest;
import bot.commands.rpg.fight.enemies.data.races.DarkSlime;
import bot.commands.rpg.fight.enemies.data.races.Dormouse;
import bot.commands.rpg.fight.enemies.data.races.Dorome;
import bot.commands.rpg.fight.enemies.data.races.Dwarf;
import bot.commands.rpg.fight.enemies.data.races.Ghoul;
import bot.commands.rpg.fight.enemies.data.races.GiantSlug;
import bot.commands.rpg.fight.enemies.data.races.Griffon;
import bot.commands.rpg.fight.enemies.data.races.Grizzly;
import bot.commands.rpg.fight.enemies.data.races.GyoubuDanuki;
import bot.commands.rpg.fight.enemies.data.races.Hakutaku;
import bot.commands.rpg.fight.enemies.data.races.Hinezumi;
import bot.commands.rpg.fight.enemies.data.races.Hobgoblin;
import bot.commands.rpg.fight.enemies.data.races.Holstaur;
import bot.commands.rpg.fight.enemies.data.races.HumptyEgg;
import bot.commands.rpg.fight.enemies.data.races.Imp;
import bot.commands.rpg.fight.enemies.data.races.Inari;
import bot.commands.rpg.fight.enemies.data.races.Kakuen;
import bot.commands.rpg.fight.enemies.data.races.Kappa;
import bot.commands.rpg.fight.enemies.data.races.Kejourou;
import bot.commands.rpg.fight.enemies.data.races.Kikimora;
import bot.commands.rpg.fight.enemies.data.races.KitsuneTsuki;
import bot.commands.rpg.fight.enemies.data.races.LargeMouse;
import bot.commands.rpg.fight.enemies.data.races.LesserSuccubus;
import bot.commands.rpg.fight.enemies.data.races.LivingDoll;
import bot.commands.rpg.fight.enemies.data.races.MarchHare;
import bot.commands.rpg.fight.enemies.data.races.Mermaid;
import bot.commands.rpg.fight.enemies.data.races.Merrow;
import bot.commands.rpg.fight.enemies.data.races.Minotaur;
import bot.commands.rpg.fight.enemies.data.races.Mothman;
import bot.commands.rpg.fight.enemies.data.races.Myconid;
import bot.commands.rpg.fight.enemies.data.races.Nekomata;
import bot.commands.rpg.fight.enemies.data.races.Nightmare;
import bot.commands.rpg.fight.enemies.data.races.Ogre;
import bot.commands.rpg.fight.enemies.data.races.Orc;
import bot.commands.rpg.fight.enemies.data.races.Papillon;
import bot.commands.rpg.fight.enemies.data.races.ParasiteSlimeSlimeCarrier;
import bot.commands.rpg.fight.enemies.data.races.QueenSlime;
import bot.commands.rpg.fight.enemies.data.races.Raiju;
import bot.commands.rpg.fight.enemies.data.races.RedOni;
import bot.commands.rpg.fight.enemies.data.races.RedSlime;
import bot.commands.rpg.fight.enemies.data.races.Redcap;
import bot.commands.rpg.fight.enemies.data.races.Satyros;
import bot.commands.rpg.fight.enemies.data.races.SeaBishop;
import bot.commands.rpg.fight.enemies.data.races.SeaSlime;
import bot.commands.rpg.fight.enemies.data.races.Siren;
import bot.commands.rpg.fight.enemies.data.races.Skeleton;
import bot.commands.rpg.fight.enemies.data.races.Slime;
import bot.commands.rpg.fight.enemies.data.races.Sphinx;
import bot.commands.rpg.fight.enemies.data.races.Tritonia;
import bot.commands.rpg.fight.enemies.data.races.Werebat;
import bot.commands.rpg.fight.enemies.data.races.Werecat;
import bot.commands.rpg.fight.enemies.data.races.Wererabbit;
import bot.commands.rpg.fight.enemies.data.races.Weresheep;
import bot.commands.rpg.fight.enemies.data.races.Werewolf;
import bot.commands.rpg.fight.enemies.data.races.Youko;
import bot.data.items.loot.LootTable;
import bot.userData.ServerUserData;
import bot.userData.UserBlessingData.Blessing;
import bot.util.CollectionUtils.ValueFrom;
import bot.util.Utils.Pair;

public class ExplorationFight implements ExplorationEventHandler {
	private static final Set<String> enemyIds = toSet(//
			AKANAME_0, AKANAME_1, AKANAME_2, AKANAME_3, AKANAME_4, AKANAME_5, //
			ALICE_0, ALICE_1, ALICE_2, ALICE_3, ALICE_4, ALICE_5, //
			Angel.ANGEL_1, //
			AntArachne.ANT_ARACHNE_1, //
			Anubis.ANUBIS_1, //
			Apsara.APSARA_1, APSARA_2, //
			ARACHNE_0, ARACHNE_1, ARACHNE_2, ARACHNE_3, ARACHNE_4, ARACHNE_5, //
			ATLACH_NACHA_0, ATLACH_NACHA_1, ATLACH_NACHA_2, ATLACH_NACHA_3, ATLACH_NACHA_4, ATLACH_NACHA_5, //
			Banshee.BANSHEE_1, Banshee.BANSHEE_2, //
			BAPHOMET_0, BAPHOMET_1, BAPHOMET_2, BAPHOMET_3, BAPHOMET_4, BAPHOMET_5, //
			Barometz.BAROMETZ_1, //
			Basilisk.BASILISK_1, //
			Beelzebub.BEELZEBUB_1, //
			BubbleSlime.BUBBLE_SLIME_1, //
			BUNYIP_0, BUNYIP_1, BUNYIP_2, BUNYIP_3, BUNYIP_4, BUNYIP_5, //
			CaitSith.CAIT_SITH_1, CaitSith.CAIT_SITH_2, //
			Charybdis.CHARYBDIS_1, //
			CHESHIRE_CAT_0, CHESHIRE_CAT_1, CHESHIRE_CAT_2, CHESHIRE_CAT_3, CHESHIRE_CAT_4, CHESHIRE_CAT_5, //
			CHIMAERA_0, CHIMAERA_1, CHIMAERA_2, CHIMAERA_3, CHIMAERA_4, CHIMAERA_5, //
			Cockatrice.COCKATRICE_1, //
			CROW_TENGU_0, CROW_TENGU_1, CROW_TENGU_2, CROW_TENGU_3, CROW_TENGU_4, CROW_TENGU_5, //
			CuSith.CU_SITH_1, CuSith.CU_SITH_2, CuSith.CU_SITH_3, //
			CursedSword.CURSED_SWORD_1, CursedSword.CURSED_SWORD_2, CursedSword.CURSED_SWORD_3,
			CursedSword.CURSED_SWORD_4, //
			DarkElf.DARK_ELF_1, DarkElf.DARK_ELF_2, DarkElf.DARK_ELF_3, //
			DarkPriest.DARK_PRIEST_1, DarkPriest.DARK_PRIEST_2, //
			DarkSlime.DARK_SLIME_1, //
			Dormouse.DORMOUSE_1, Dormouse.DORMOUSE_2, Dormouse.DORMOUSE_3, //
			Dorome.DOROME_1, Dorome.DOROME_2, Dorome.DOROME_3, //
			DRAGON_ZOMBIE_0, DRAGON_ZOMBIE_1, DRAGON_ZOMBIE_2, DRAGON_ZOMBIE_3, DRAGON_ZOMBIE_4, DRAGON_ZOMBIE_5, //
			Dwarf.DWARF_1, Dwarf.DWARF_2, Dwarf.DWARF_3, Dwarf.DWARF_4, //
			FAIRY_0, FAIRY_1, FAIRY_2, FAIRY_3, FAIRY_4, FAIRY_5, //
			Ghoul.GHOUL_1, Ghoul.GHOUL_2, //
			GiantSlug.GIANT_SLUG_1, //
			GLACIES_0, GLACIES_1, GLACIES_2, GLACIES_3, GLACIES_4, GLACIES_5, //
			GOBLIN_0, GOBLIN_1, GOBLIN_2, GOBLIN_3, GOBLIN_4, GOBLIN_5, //
			Griffon.GRIFFON_1, Griffon.GRIFFON_2, Griffon.GRIFFON_3, Griffon.GRIFFON_4, //
			Grizzly.GRIZZLY_1, Grizzly.GRIZZLY_2, Grizzly.GRIZZLY_3, Grizzly.GRIZZLY_4, //
			GyoubuDanuki.GYOUBU_DANUKI_1, GyoubuDanuki.GYOUBU_DANUKI_2, //
			Hakutaku.HAKUTAKU_1, Hakutaku.HAKUTAKU_2, Hakutaku.HAKUTAKU_3, //
			HELLHOUND_0, HELLHOUND_1, HELLHOUND_2, HELLHOUND_3, HELLHOUND_4, HELLHOUND_5, //
			Hinezumi.HINEZUMI_1, //
			Hobgoblin.HOBGOBLIN_1, Hobgoblin.HOBGOBLIN_2, //
			Holstaur.HOLSTAUR_1, Holstaur.HOLSTAUR_2, //
			HumptyEgg.HUMPTY_EGG_1, //
			ICE_QUEEN_0, ICE_QUEEN_1, ICE_QUEEN_2, ICE_QUEEN_3, ICE_QUEEN_4, ICE_QUEEN_5, //
			Imp.IMP_1, Imp.IMP_2, //
			Inari.INARI_1, Inari.INARI_2, //
			JINKO_0, JINKO_1, JINKO_2, JINKO_3, JINKO_4, JINKO_5, //
			Kakuen.KAKUEN_1, Kakuen.KAKUEN_2, Kakuen.KAKUEN_3, //
			KAMAITACHI_0, KAMAITACHI_1, KAMAITACHI_2, KAMAITACHI_3, KAMAITACHI_4, KAMAITACHI_5, //
			Kappa.KAPPA_1, ///
			Kejourou.KEJOUROU_1, Kejourou.KEJOUROU_2, //
			Kikimora.KIKIMORA_1, //
			KitsuneTsuki.KITSUNE_TSUKI_1, KitsuneTsuki.KITSUNE_TSUKI_2, //
			KOBOLD_0, KOBOLD_1, KOBOLD_2, KOBOLD_3, KOBOLD_4, KOBOLD_5, //
			LargeMouse.LARGE_MOUSE_1, ///
			LesserSuccubus.LESSER_SUCCUBUS_1, //
			LILIM_0, LILIM_1, LILIM_2, LILIM_3, LILIM_4, LILIM_5, //
			LivingDoll.LIVING_DOLL_1, //
			LIZARDMAN_0, LIZARDMAN_1, LIZARDMAN_2, LIZARDMAN_3, LIZARDMAN_4, LIZARDMAN_5, //
			MANTICORE_0, MANTICORE_1, MANTICORE_2, MANTICORE_3, MANTICORE_4, MANTICORE_5, //
			MarchHare.MARCH_HARE_1, //
			Mermaid.MERMAID_1, MERMAID_2, //
			Merrow.MERROW_1, //
			Minotaur.MINOTAUR_1, MINOTAUR_2, //
			Mothman.MOTHMAN_1, //
			MUMMY_0, MUMMY_1, MUMMY_2, MUMMY_3, MUMMY_4, MUMMY_5, //
			Myconid.MYCONID_1, MYCONID_2, //
			Nekomata.NEKOMATA_1, //
			Nightmare.NIGHTMARE_1, NIGHTMARE_2, //
			OCELOMEH_0, OCELOMEH_1, OCELOMEH_2, OCELOMEH_3, OCELOMEH_4, OCELOMEH_5, //
			Ogre.OGRE_1, OGRE_2, //
			Orc.ORC_1, ORC_2, ORC_3, //
			OWL_MAGE_0, OWL_MAGE_1, OWL_MAGE_2, OWL_MAGE_3, OWL_MAGE_4, OWL_MAGE_5, //
			Papillon.PAPILLON_1, PAPILLON_2, //
			ParasiteSlimeSlimeCarrier.PARASITE_SLIME_SLIME_CARRIER_1, PARASITE_SLIME_SLIME_CARRIER_2, //
			PIXIE_0, PIXIE_1, PIXIE_2, PIXIE_3, PIXIE_4, PIXIE_5, //
			QueenSlime.QUEEN_SLIME_1, QUEEN_SLIME_2, QUEEN_SLIME_3, //
			Raiju.RAIJU_1, RAIJU_2, //
			RATATOSKR_0, RATATOSKR_1, RATATOSKR_2, RATATOSKR_3, RATATOSKR_4, RATATOSKR_5, //
			Redcap.REDCAP_1, REDCAP_2, //
			RedOni.RED_ONI_1, RED_ONI_2, RED_ONI_3, //
			RedSlime.RED_SLIME_1, //
			Satyros.SATYROS_1, //
			SeaBishop.SEA_BISHOP_1, SEA_BISHOP_2, //
			SeaSlime.SEA_SLIME_1, //
			Siren.SIREN_1, SIREN_2, SIREN_3, SIREN_4, //
			Skeleton.SKELETON_1, SKELETON_2, //
			Slime.SLIME_1, SLIME_2, //
			Sphinx.SPHINX_1, SPHINX_2, //
			SUCCUBUS_0, SUCCUBUS_1, SUCCUBUS_2, SUCCUBUS_3, SUCCUBUS_4, SUCCUBUS_5, //
			THUNDERBIRD_0, THUNDERBIRD_1, THUNDERBIRD_2, THUNDERBIRD_3, THUNDERBIRD_4, THUNDERBIRD_5, //
			Tritonia.TRITONIA_1, //
			Werebat.WEREBAT_1, //
			Werecat.WERECAT_1, WERECAT_2, //
			Wererabbit.WERERABBIT_1, //
			Weresheep.WERESHEEP_1, //
			Werewolf.WEREWOLF_1, WEREWOLF_2, WEREWOLF_3, //
			WITCH_0, WITCH_1, WITCH_2, WITCH_3, WITCH_4, WITCH_5, //
			WURM_0, WURM_1, WURM_2, WURM_3, WURM_4, WURM_5, //
			Youko.YOUKO_1, YOUKO_2, //
			ZOMBIE_0, ZOMBIE_1, ZOMBIE_2, ZOMBIE_3, ZOMBIE_4, ZOMBIE_5);

	private static final double weightOffset = 0.8;

	private static double levelWeight(final int playerLevel, final int enemyLevel) {
		final int distance = abs(playerLevel - enemyLevel);
		return max(0, min(1, (1 + weightOffset) / (weightOffset + distance * distance)));
	}

	private List<Pair<Double, Integer>> getWeightsForAllLevels(final int playerLevel) {
		final List<Pair<Double, Integer>> weights = new ArrayList<>();
		for (final int enemyLevel : enemyLevels.keySet()) {
			weights.add(pair(levelWeight(playerLevel, enemyLevel), enemyLevel));
		}

		return weights;
	}

	private final Map<String, List<String>> targetsByRace = new HashMap<>();
	private final Map<Integer, List<String>> enemyLevels = new HashMap<>();
	private final Map<Integer, LootTable<Integer>> enemyLevelSelectors = new HashMap<>();

	private final Fluffer10kFun fluffer10kFun;

	private void printLevels(final boolean print) {
		if (print) {
			enemyLevels.entrySet().stream()//
					.sorted(new ValueFrom<>(entry -> entry.getKey()))//
					.peek(entry -> entry.getValue().sort(null))//
					.forEach(System.out::println);
		}
	}

	private void printDistributions(final int... levels) {
		for (final int level : levels) {
			final List<Pair<Double, Integer>> weights = getWeightsForAllLevels(level);
			double weightsSum = 0;
			for (int j = 0; j < weights.size(); j++) {
				weightsSum += weights.get(j).a;
			}
			String s = (level) + ":";
			for (int j = 0; j < weights.size(); j++) {
				s += ", " + weights.get(j).b + ": " + String.format("%1.1f%%", weights.get(j).a / weightsSum * 100);
			}
			System.out.println(s);
		}
	}

	public ExplorationFight(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;

		for (final String enemyId : enemyIds) {
			try {
				final RPGEnemyData enemy = fluffer10kFun.rpgEnemies.get(enemyId);
				addToListOnMap(targetsByRace, fixString(enemy.name), enemy.id);
				addToListOnMap(enemyLevels, enemy.level, enemy.id);
			} catch (final Exception e) {
				System.err.println(enemyId);
				e.printStackTrace();
				throw e;
			}
		}

		for (int i = 0; i < 100; i++) {
			enemyLevelSelectors.put(i, weightedD(getWeightsForAllLevels(i)));
		}

		if (fluffer10kFun.apiUtils.config.getBoolean("debug")) {
			printLevels(false);
			printDistributions();
		}
	}

	public String getTrackingTarget(final SlashCommandInteraction interaction) {
		return fixString(interaction.getOptionStringValueByName("tracking_target").orElse(null));
	}

	public boolean isValidTrackingTarget(final String trackingTarget) {
		return trackingTarget == null || targetsByRace.get(trackingTarget) != null;
	}

	private RPGEnemyData getTarget(final String trackingTarget, final int playerLevel) {
		if (trackingTarget != null) {
			final List<String> targets = targetsByRace.get(trackingTarget);

			for (int i = 0; i < 5; i++) {
				final String target = getRandom(targets);
				final RPGEnemyData enemyData = fluffer10kFun.rpgEnemies.get(target);
				if (getRandomBoolean(0.5 + 0.1 * (playerLevel - enemyData.level))) {
					return enemyData;
				}
			}
		}

		final LootTable<Integer> enemyLevelSelector = enemyLevelSelectors.get(playerLevel);
		final int enemyLevel = enemyLevelSelector.getItem();
		final List<String> targets = enemyLevels.get(enemyLevel);
		return fluffer10kFun.rpgEnemies.get(getRandom(targets));
	}

	@Override
	public boolean handle(final SlashCommandInteraction interaction, final ServerUserData userData) {
		final String trackingTarget = getTrackingTarget(interaction);
		RPGEnemyData enemy = getTarget(trackingTarget, userData.rpg.level);
		if (userData.blessings.blessingsObtained.contains(Blessing.THE_STORM_THAT_IS_APPROACHING)) {
			final RPGEnemyData enemy2 = getTarget(trackingTarget, userData.rpg.level);
			if (enemy2.level > enemy.level) {
				enemy = enemy2;
			}
		}

		interaction.createImmediateResponder()
				.addEmbed(makeEmbed("Fight", "You find " + enemy.name + " roaming the area!")).respond().join();

		sendEphemeralMessage(interaction, "Fight started!");
		fluffer10kFun.fightStart.startFightPvE(interaction.getChannel().get().asServerTextChannel().get(),
				interaction.getUser(), enemy);

		return true;
	}
}
