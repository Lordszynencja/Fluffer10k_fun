package bot.commands.mgLove;

import org.javacord.api.entity.user.User;

public class MgLoveData {
	public final User target;
	public final int cums;
	public final boolean protectedFromLove;
	public final boolean savedFromLove;

	public MgLoveData(final User target, final int cums) {
		this.target = target;
		this.cums = cums;
		protectedFromLove = false;
		savedFromLove = false;
	}

	public MgLoveData(final User target, final boolean protectedFromLove, final boolean savedFromLove) {
		this.target = target;
		cums = 0;
		this.protectedFromLove = protectedFromLove;
		this.savedFromLove = savedFromLove;
	}
}