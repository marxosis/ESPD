
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.MirrorSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;

import java.util.HashSet;

public class MirrorImage extends NPC {

	{
		name = Messages.get(MirrorImage.class, "name");
		spriteClass = MirrorSprite.class;

		state = HUNTING;

	}

	public int tier;

	private int attack;
	private int damage;

	private static final String TIER = "tier";
	private static final String ATTACK = "attack";
	private static final String DAMAGE = "damage";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TIER, tier);
		bundle.put(ATTACK, attack);
		bundle.put(DAMAGE, damage);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		tier = bundle.getInt(TIER);
		attack = bundle.getInt(ATTACK);
		damage = bundle.getInt(DAMAGE);
	}

	public void duplicate(Hero hero) {
		tier = hero.tier();
		attack = hero.attackSkill(hero);
		damage = hero.damageRoll();
	}

	@Override
	public int attackSkill(Char target) {
		return attack;
	}

	@Override
	public int damageRoll() {
		return damage;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		int dmg = super.attackProc(enemy, damage);

		destroy();
		sprite.die();

		return dmg;
	}

	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.hostile
						&& Level.fieldOfView[mob.pos]
						&& mob.state != mob.PASSIVE) {
					enemies.add(mob);
				}
			}

			//go for closest enemy
			Char closest = null;
			for (Char curr : enemies) {
				if (closest == null
						|| Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)) {
					closest = curr;
				}
			}
			return closest;
		}

		return enemy;
	}

	@Override
	public String description() {
		return Messages.get(MirrorImage.class, "desc");
	}

	@Override
	public CharSprite sprite() {
		CharSprite s = super.sprite();
		((MirrorSprite) s).updateArmor(tier);
		return s;
	}


	@Override
	public boolean interact() {

		if (this.buff(MagicalSleep.class) != null) {
			Buff.detach(this, MagicalSleep.class);
		}

		if (state == SLEEPING) {
			state = HUNTING;
		}
		if (buff(Paralysis.class) != null) {
			Buff.detach(this, Paralysis.class);
			GLog.i(Messages.get(MirrorImage.class, "shake"), name);
		}

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();

		return true;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Burning.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}