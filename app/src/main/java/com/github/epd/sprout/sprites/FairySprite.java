
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.pets.Fairy;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.HealthBar;
import com.watabou.noosa.TextureFilm;

public class FairySprite extends MobSprite {

	public HealthBar hpBar;

	//Frames 0,2 are idle, 0,1,2 are moving, 0,3,4,1 are attack and 5,6,7 are for death

	public FairySprite() {
		super();

		texture(Assets.FAIRY);

		TextureFilm frames = new TextureFilm(texture, 15, 15);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 2, 3, 0);

		run = new Animation(8, true);
		run.frames(frames, 0, 1, 2, 0);

		attack = new Animation(8, false);
		attack.frames(frames, 0, 3, 4, 1);

		zap = attack.clone();

		die = new Animation(8, false);
		die.frames(frames, 5, 6, 7, 7);

		play(idle);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		if (ch instanceof Fairy) {
			final Char finalCH = ch;
			hpBar = new HealthBar() {
				@Override
				public synchronized void update() {
					super.update();
					hpBar.setRect(finalCH.sprite.x, finalCH.sprite.y - 3, finalCH.sprite.width, hpBar.height());
					hpBar.level(finalCH);
					visible = finalCH.sprite.visible;
				}
			};
			((GameScene) ShatteredPixelDungeon.scene()).ghostHP.add(hpBar);
		}
	}

	@Override
	public void zap(int pos) {

		parent.add(new Lightning(ch.pos, pos, (Fairy) ch));

		turnTo(ch.pos, pos);
		play(zap);
	}

	@Override
	public int blood() {
		return 0xFFcdcdb7;
	}

	@Override
	public void die() {
		super.die();

		if (hpBar != null) {
			hpBar.killAndErase();
		}
	}

	@Override
	public void killAndErase(){

		if (hpBar != null) {
			hpBar.killAndErase();
		}

		super.killAndErase();
	}
}
