package bot.commands.rpg.fight.fightRewards;

import static bot.commands.rpg.quests.Quest.description;
import static bot.commands.rpg.quests.Quest.dialogue;
import static bot.util.CollectionUtils.addToIntOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomInt;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightEnd.RewardCreator;
import bot.commands.rpg.fight.FightTempData;
import bot.data.MonsterGirls.MonsterGirlRace;
import bot.data.fight.PlayerFighterData;
import bot.data.items.FurnitureType;
import bot.data.quests.QuestType;
import bot.userData.HaremMemberData;
import bot.userData.HaremMemberData.HaremMemberInteraction;
import bot.userData.ServerUserData;

public class Blessing6Reward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public Blessing6Reward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final PlayerFighterData player = data.fight.fighters.get("PLAYER").player();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
		if (player.isOut()) {
			userData.rpg.quests.remove(QuestType.ULTIMATE_TEST);

			final String description = String.join("\n", //
					description("Helga wins once again, even though you were so close to defeating her."), //
					dialogue(
							"Haa... haa... You almost got me there! Maybe I underestimated your skills, but you still lost!"), //
					description(
							"She leaves as you try to catch your breath and decide to rest after this really tiring fight, and fall asleep for a bit."));
			userData.reduceStamina(ServerUserData.maxStamina);

			data.channel.sendMessage(makeEmbed("You lost the fight with Helga", description));
			return;
		}

		addToIntOnMap(userData.houseFurniture, FurnitureType.ADDDITIONAL_ROOM, 1);
		final HaremMemberData lilian = userData.addWife(MonsterGirlRace.MINOTAUR);
		lilian.name = "Helga";
		lilian.changeable = false;
		lilian.married = true;
		lilian.affection = 100;
		lilian.desiredInteraction = HaremMemberInteraction.FUCK_LIKE_CRAZY;
		fluffer10kFun.serverUserDataUtils.getUserData(player).bless();

		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(data.channel.getServer(), user,
				getRandomInt(200, 300), "your new wife Helga");

		final String description = String.join("\n", //
				description(
						"You fight as long as you can, and finally, the mighty minotaur falls on the ground, unable to fight anymore."), //
				dialogue("Haa... haa... How could this happen?! Haaa... I am so strong, how did I lose?"), //
				description("You slowly approach her."), //
				dialogue(
						"Haa... I guess you are to be my husband now then, huh? I can't say no to someone so powerful. Take me home and let's fuck!"), //
				description(
						"You are a bit surprised by the request, but you help her get up and go to your house, where you have lots of sex."), //
				description(
						"You get completely exhausted by that, but you are happy that you defeated such a strong opponent. You smile as Helga holds you in her arms."), //
				dialogue("You look kinda cute like this. Mmmm, I'm happy that you were the one to defeat me~"), //
				description("You fall asleep, where you dream about becoming the greatest hero of all time."), //
				description("You also get another blessing, getting you one step closer to that."));

		data.channel.sendMessage(loveEmbed, makeEmbed("You won the fight with Helga!", description));
	}

}
