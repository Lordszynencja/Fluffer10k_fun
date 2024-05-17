package bot.commands.rpg.fight.fightRewards;

import static bot.commands.rpg.quests.Quest.description;
import static bot.commands.rpg.quests.Quest.dialogue;
import static bot.util.CollectionUtils.addToIntOnMap;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomInt;
import static bot.util.apis.MessageUtils.getServer;

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

public class Blessing3Reward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public Blessing3Reward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final PlayerFighterData player = data.fight.fighters.get("PLAYER").player();
		final ServerUserData userData = fluffer10kFun.serverUserDataUtils.getUserData(player);
		if (player.isOut()) {
			userData.rpg.quests.remove(QuestType.ULTIMATE_TEST);

			final String description = String.join("\n", //
					description("Lilian wins once again, even though you were so close to defeating her."), //
					dialogue(
							"Haa~ haa~ O-oh my, that was something! That stupid blessing protected you though... I feel really tired now..."), //
					dialogue("Was still fun to do a little sparing, I hope you will find me again~"), //
					description(
							"She leaves as you try to catch your breath and decide to rest after this really tiring fight, and fall asleep for a bit."));
			userData.reduceStamina(ServerUserData.maxStamina);

			data.channel.sendMessage(makeEmbed("You lost the fight with Lilian", description));
			return;
		}

		addToIntOnMap(userData.houseFurniture, FurnitureType.ADDDITIONAL_ROOM, 1);
		final HaremMemberData lilian = userData.addWife(MonsterGirlRace.MANTICORE);
		lilian.name = "Lilian";
		lilian.changeable = false;
		lilian.married = true;
		lilian.affection = 100;
		lilian.desiredInteraction = HaremMemberInteraction.FUCK_LIKE_CRAZY;
		fluffer10kFun.serverUserDataUtils.getUserData(player).bless();

		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(getServer(data.channel), user,
				getRandomInt(200, 300), "your new wife Lilian");

		final String description = String.join("\n", //
				description("After a long, tiring fight, Lilian is finally too tired to continue and yields!"), //
				dialogue(
						"Haa~ haa~ Y-you really got strong. I give that to you. Now then, if you won, you have to be my husband~"), //
				description(
						"You are surprised by her words but she just crawls to you and starts nuzzling you, so you accept."), //
				description(
						"After some rest you get married and head home, where you have crazy amounts of sex for the whole week, making you completely exhausted and unable to even move."), //
				description(
						"For defeating the officer of the Monster Lord's army and saving villagers that were yet to be attacked, you are blessed by the goddess once again."), //
				description(
						"You know that you did a good thing, and smile while your new wife cuddles you and tells you of her superior, one of the Monster Generals."), //
				description("You plan her to be your next target, but for now you have to go back to strength."));

		data.channel.sendMessage(loveEmbed, makeEmbed("You won the fight with Lilian!", description));
	}

}
