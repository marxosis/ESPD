
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.DewVial;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.quest.Mushroom;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.TinkererSprite;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndQuest;
import com.github.epd.sprout.windows.WndTinkerer3;

public class Tinkerer3 extends NPC {

	{
		name = Messages.get(Tinkerer1.class, "name");
		spriteClass = TinkererSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	private static final String TXT_DUNGEON = Messages.get(Tinkerer1.class, "dungeon3");


	private static final String TXT_DUNGEON2 = Messages.get(Tinkerer1.class, "dungeon2");

	private static final String TXT_MUSH = Messages.get(Tinkerer1.class, "mush");

	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(Tinkerer1.class, "def");
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);
		Item item = Dungeon.hero.belongings.getItem(Mushroom.class);
		Item vial = Dungeon.hero.belongings.getItem(DewVial.class);
		if (item != null && vial != null) {
			GameScene.show(new WndTinkerer3(this, item));
		} else if (item == null && vial != null) {
			tell(TXT_DUNGEON);
		} else {
			tell(TXT_DUNGEON2);
		}
		return false;
	}

	private void tell(String format, Object... args) {
		GameScene.show(new WndQuest(this, Utils.format(format, args)));
	}

	@Override
	public String description() {
		return Messages.get(Tinkerer1.class, "desc2");
	}

}
