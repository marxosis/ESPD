
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;


public class RatSkull extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SKULL;

		unique = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return 100;
	}
}
