
package com.github.epd.sprout.items.weapon.missiles;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Assassin;
import com.github.epd.sprout.actors.mobs.Bat;
import com.github.epd.sprout.actors.mobs.Brute;
import com.github.epd.sprout.actors.mobs.Gnoll;
import com.github.epd.sprout.actors.mobs.GoldThief;
import com.github.epd.sprout.actors.mobs.PoisonGoo;
import com.github.epd.sprout.actors.mobs.Rat;
import com.github.epd.sprout.actors.mobs.RatBoss;
import com.github.epd.sprout.actors.mobs.Shaman;
import com.github.epd.sprout.actors.mobs.SpectralRat;
import com.github.epd.sprout.actors.mobs.Thief;
import com.github.epd.sprout.actors.mobs.npcs.Ghost.GnollArcher;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class ForestDart extends MissileWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.DART;

		MIN = 4;
		MAX = 10;

		bones = false; // Finding them in bones would be semi-frequent and
		// disappointing.
	}

	public ForestDart() {
		this(1);
	}

	public ForestDart(int number) {
		super();
		quantity = number;
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {


		if (defender instanceof Gnoll
				|| defender instanceof GnollArcher
				|| defender instanceof Shaman
				|| defender instanceof Brute
				|| defender instanceof Bat
				|| defender instanceof Rat
				|| defender instanceof RatBoss
				|| defender instanceof Assassin
				|| defender instanceof Thief
				|| defender instanceof GoldThief
				|| defender instanceof PoisonGoo
				|| defender instanceof SpectralRat
				) {
			defender.damage(Random.Int(damage * 2, damage * 5), this);
		} else {
			defender.damage(Random.Int(damage, damage * 2), this);
		}


	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public Item random() {
		quantity = Random.Int(5, 15);
		return this;
	}

	@Override
	public int price() {
		return quantity * 2;
	}

	private static final Glowing GREEN = new Glowing(0x00FF00);

	@Override
	public Glowing glowing() {
		return GREEN;
	}
}
