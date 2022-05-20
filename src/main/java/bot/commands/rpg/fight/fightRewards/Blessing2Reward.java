package bot.commands.rpg.fight.fightRewards;

import static bot.commands.rpg.quests.Quest.description;
import static bot.commands.rpg.quests.Quest.dialogue;
import static bot.util.EmbedUtils.makeEmbed;
import static bot.util.RandomUtils.getRandomInt;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;

import bot.Fluffer10kFun;
import bot.commands.rpg.fight.FightEnd.RewardCreator;
import bot.commands.rpg.fight.FightTempData;
import bot.data.fight.PlayerFighterData;

public class Blessing2Reward implements RewardCreator {

	private final Fluffer10kFun fluffer10kFun;

	public Blessing2Reward(final Fluffer10kFun fluffer10kFun) {
		this.fluffer10kFun = fluffer10kFun;
	}

	@Override
	public void giveRewards(final FightTempData data) {
		final PlayerFighterData player = data.fight.fighters.get("PLAYER").player();
		fluffer10kFun.serverUserDataUtils.getUserData(player).bless();

		final User user = fluffer10kFun.apiUtils.getUser(player.userId);
		final EmbedBuilder loveEmbed = fluffer10kFun.commandMgLove.makeLovedByEmbed(data.channel.getServer(), user,
				getRandomInt(50, 100), "Lilian");

		final String description = String.join("\n", //
				description("Lilian wins another time, but now she's visibly tired as she humps against you."), //
				dialogue("Haa~ haa~ my, you got blessed again? No matter, I won anyway~ give me all your semen, hero!"), //
				dialogue("Now, maybe next time you will have a chance to win against me, but for now..."), //
				description("She uses her tight tail pussy to squeeze out last bits of your power out of you."), //
				dialogue("Sleep well~"), //
				description("When you wake up later, you are in your house once again, recovering from the fight."), //
				description("This time though, you feel that your next meeting's outcome will be different."), //
				description(
						"With another blessing from the goddess, you feel that as soon as you find her, you will make her submit."));

		data.channel.sendMessage(loveEmbed, makeEmbed("You lost the fight with Lilian", description));
	}

}
