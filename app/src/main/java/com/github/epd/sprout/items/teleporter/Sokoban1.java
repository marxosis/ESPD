
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Sokoban1 extends JournalPage {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_1;
		room = 1;

		stackable = false;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
