
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Journal;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.Fire;
import com.github.epd.sprout.actors.blobs.StenchGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Ooze;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.actors.mobs.Crab;
import com.github.epd.sprout.actors.mobs.Gnoll;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.Rat;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.teleporter.TenguKey;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.teleporter.SafeSpotPage;
import com.github.epd.sprout.items.teleporter.SewersKey;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.missiles.CurareDart;
import com.github.epd.sprout.items.weapon.missiles.ForestDart;
import com.github.epd.sprout.items.weapon.missiles.MissileWeapon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.SewerLevel;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.FetidRatSprite;
import com.github.epd.sprout.sprites.GhostSprite;
import com.github.epd.sprout.sprites.GnollArcherSprite;
import com.github.epd.sprout.sprites.GnollTricksterSprite;
import com.github.epd.sprout.sprites.GreatCrabSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndQuest;
import com.github.epd.sprout.windows.WndSadGhost;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Ghost extends NPC {

	{
		name = Messages.get(Ghost.class, "name");
		spriteClass = GhostSprite.class;

		flying = true;

		state = WANDERING;
	}

	private static final String TXT_RAT1 = Messages.get(Ghost.class, "ratone", Dungeon.hero.givenName());

	private static final String TXT_RAT2 = Messages.get(Ghost.class, "rattwo");

	private static final String TXT_GNOLL1 = Messages.get(Ghost.class, "gnollone", Dungeon.hero.givenName());

	private static final String TXT_GNOLL2 = Messages.get(Ghost.class, "gnolltwo");

	private static final String TXT_CRAB1 = Messages.get(Ghost.class, "crabone", Dungeon.hero.givenName());

	private static final String TXT_CRAB2 = Messages.get(Ghost.class, "crabtwo");

	public Ghost() {
		super();

		Sample.INSTANCE.load(Assets.SND_GHOST);
	}

	@Override
	protected boolean act() {
		if (Quest.completed())
			target = Dungeon.hero.pos;
		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(Ghost.class, "defenseverb");
	}

	@Override
	public float speed() {
		return Quest.completed() ? 2f : 0.5f;
	}

	@Override
	protected Char chooseEnemy() {
		return null;
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

		Sample.INSTANCE.play(Assets.SND_GHOST);

		if (Quest.given) {
			if (Quest.weapon != null) {
				if (Quest.processed) {
					GameScene.show(new WndSadGhost(this, Quest.type));
				} else {
					switch (Quest.type) {
						case 1:
						default:
							GameScene.show(new WndQuest(this, TXT_RAT2));
							break;
						case 2:
							GameScene.show(new WndQuest(this, TXT_GNOLL2));
							break;
						case 3:
							GameScene.show(new WndQuest(this, TXT_CRAB2));
							break;
					}

					int newPos = -1;
					for (int i = 0; i < 10; i++) {
						newPos = Dungeon.level.randomRespawnCell();
						if (newPos != -1) {
							break;
						}
					}
					if (newPos != -1) {

						CellEmitter.get(pos).start(Speck.factory(Speck.LIGHT),
								0.2f, 3);
						pos = newPos;
						sprite.place(pos);
						sprite.visible = Dungeon.visible[pos];
					}
				}
			}
		} else {
			Mob questBoss;
			String txt_quest;

			switch (Quest.type) {
				case 1:
				default:
					questBoss = new FetidRat();
					txt_quest = Utils.format(TXT_RAT1, Dungeon.hero.givenName());
					break;
				case 2:
					questBoss = new GnollTrickster();
					txt_quest = Utils.format(TXT_GNOLL1, Dungeon.hero.givenName());
					break;
				case 3:
					questBoss = new GreatCrab();
					txt_quest = Utils.format(TXT_CRAB1, Dungeon.hero.givenName());
					break;
			}

			questBoss.pos = Dungeon.level.randomRespawnCell();

			if (questBoss.pos != -1) {
				GameScene.add(questBoss);
				GameScene.show(new WndQuest(this, txt_quest));
				Quest.given = true;
				Journal.add(Journal.Feature.GHOST);
			}

		}
		return false;
	}

	@Override
	public String description() {
		return Messages.get(Ghost.class, "desc");
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Paralysis.class);
		IMMUNITIES.add(Roots.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}

	public static class Quest {

		private static boolean spawned;

		private static int type;

		private static boolean given;
		private static boolean processed;

		private static int depth;

		public static Weapon weapon;
		public static Armor armor;

		public static void reset() {
			spawned = false;

			weapon = null;
			armor = null;
		}

		private static final String NODE = "sadGhost";

		private static final String SPAWNED = "spawned";
		private static final String TYPE = "type";
		private static final String GIVEN = "given";
		private static final String PROCESSED = "processed";
		private static final String DEPTH = "depth";
		private static final String WEAPON = "weapon";
		private static final String ARMOR = "armor";

		public static void storeInBundle(Bundle bundle) {

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if (spawned) {

				node.put(TYPE, type);

				node.put(GIVEN, given);
				node.put(DEPTH, depth);
				node.put(PROCESSED, processed);

				node.put(WEAPON, weapon);
				node.put(ARMOR, armor);
			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle) {

			Bundle node = bundle.getBundle(NODE);

			if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {

				type = node.getInt(TYPE);
				given = node.getBoolean(GIVEN);
				processed = node.getBoolean(PROCESSED);

				depth = node.getInt(DEPTH);

				weapon = (Weapon) node.get(WEAPON);
				armor = (Armor) node.get(ARMOR);
			} else {
				reset();
			}
		}

		public static void spawn(SewerLevel level) {
			if (!spawned && Dungeon.depth > 1
					&& Random.Int(5 - Dungeon.depth) == 0) {

				Ghost ghost = new Ghost();
				do {
					ghost.pos = level.randomRespawnCell();
				} while (ghost.pos == -1);
				level.mobs.add(ghost);

				spawned = true;
				// dungeon depth determines type of quest.
				// depth2=fetid rat, 3=gnoll trickster, 4=great crab
				type = Dungeon.depth - 1;

				given = false;
				processed = false;
				depth = Dungeon.depth;

				do {
					weapon = Generator.randomWeapon(Dungeon.questTweaks ? 18 : 10);
				} while (weapon instanceof MissileWeapon);
				armor = Generator.randomArmor(Dungeon.questTweaks ? 18 : 10);

				for (int i = 1; i <= 3; i++) {
					Item another;
					do {
						another = Generator.randomWeapon(Dungeon.questTweaks ? 18 : (10 + i));
					} while (another instanceof MissileWeapon);
					if (another.level >= weapon.level) {
						weapon = (Weapon) another;
					}
					another = Generator.randomArmor(Dungeon.questTweaks ? 18 : (10 + i));
					if (another.level >= armor.level) {
						armor = (Armor) another;
					}
				}

				weapon.identify();
				armor.identify();

				if (Dungeon.questTweaks) {
					weapon.upgrade(30);
					armor.upgrade(30);
				}
			}
		}

		public static void process() {
			if (spawned && given && !processed && (depth == Dungeon.depth)) {
				GLog.n(Messages.get(Ghost.class, "process"));
				Sample.INSTANCE.play(Assets.SND_GHOST);
				processed = true;
				Generator.Category.ARTIFACT.probs[10] = 1; // flags the dried
				// rose as
				// spawnable.
			}
		}

		public static void complete() {
			weapon = null;
			armor = null;

			Journal.remove(Journal.Feature.GHOST);
		}

		public static boolean completed() {
			return spawned && processed;
		}
	}

	public static class FetidRat extends Rat {

		{
			name = Messages.get(Ghost.class, "ratname");
			spriteClass = FetidRatSprite.class;

			HP = HT = 20;
			defenseSkill = 5;

			EXP = 4;

			state = WANDERING;
		}

		@Override
		public int attackSkill(Char target) {
			return 12;
		}

		@Override
		public int dr() {
			return 2;
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			if (Random.Int(3) == 0) {
				Buff.affect(enemy, Ooze.class);
			}

			return damage;
		}

		@Override
		public int defenseProc(Char enemy, int damage) {

			GameScene.add(Blob.seed(pos, 20, StenchGas.class));

			return super.defenseProc(enemy, damage);
		}

		@Override
		public void die(Object cause) {
			super.die(cause);

			Quest.process();
		}

		@Override
		public String description() {
			return Messages.get(Ghost.class, "ratdesc");
		}

		private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

		static {
			IMMUNITIES.add(StenchGas.class);
		}

		@Override
		public HashSet<Class<?>> immunities() {
			return IMMUNITIES;
		}
	}

	public static class GnollTrickster extends Gnoll {
		{
			name = Messages.get(Ghost.class, "gnollname");
			spriteClass = GnollTricksterSprite.class;

			HP = HT = 40;
			defenseSkill = 5;

			EXP = 5;

			state = WANDERING;

			loot = Generator.random(CurareDart.class);
			lootChance = 1f;
		}

		private int combo = 0;

		@Override
		public int attackSkill(Char target) {
			return 16;
		}

		@Override
		protected boolean canAttack(Char enemy) {
			Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);
			if (!Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos) {
				combo++;
				return true;
			} else {
				return false;
			}
		}

		@Override
		public int attackProc(Char enemy, int damage) {
			// The gnoll's attacks get more severe the more the player lets it
			// hit them
			int effect = Random.Int(4) + combo;

			if (effect > 2) {

				if (effect >= 6 && enemy.buff(Burning.class) == null) {

					if (Level.flamable[enemy.pos])
						GameScene.add(Blob.seed(enemy.pos, 4, Fire.class));
					Buff.affect(enemy, Burning.class).reignite(enemy);

				} else
					Buff.affect(enemy, Poison.class).set(
							(effect - 2) * Poison.durationFactor(enemy));

			}
			return damage;
		}

		@Override
		protected boolean getCloser(int target) {
			combo = 0; // if he's moving, he isn't attacking, reset combo.
			if (Dungeon.level.adjacent(pos, enemy.pos)) {
				return getFurther(target);
			} else {
				return super.getCloser(target);
			}
		}

		@Override
		public void die(Object cause) {
			super.die(cause);

			Quest.process();
		}

		@Override
		public String description() {
			return Messages.get(Ghost.class, "gnolldesc");
		}

		private static final String COMBO = "combo";

		@Override
		public void storeInBundle(Bundle bundle) {
			super.storeInBundle(bundle);
			bundle.put(COMBO, combo);
		}

		@Override
		public void restoreFromBundle(Bundle bundle) {
			super.restoreFromBundle(bundle);
			combo = bundle.getInt(COMBO);
		}

	}

	public static class GreatCrab extends Crab {
		{
			name = Messages.get(Ghost.class, "crabname");
			spriteClass = GreatCrabSprite.class;

			HP = HT = 50;
			defenseSkill = 0; // see damage()
			baseSpeed = 1f;

			EXP = 6;

			state = WANDERING;
		}

		private int moving = 0;

		@Override
		protected boolean getCloser(int target) {
			// this is used so that the crab remains slower, but still detects
			// the player at the expected rate.
			moving++;
			if (moving < 3) {
				return super.getCloser(target);
			} else {
				moving = 0;
				return true;
			}

		}

		@Override
		public void damage(int dmg, Object src) {
			// crab blocks all attacks originating from the hero or enemy
			// characters or traps if it is alerted.
			// All direct damage from these sources is negated, no exceptions.
			// blob/debuff effects go through as normal.
			if (enemySeen
					&& (src instanceof Wand
					|| src instanceof LightningTrap.Electricity || src instanceof Char)) {
				GLog.n(Messages.get(Ghost.class, "crabblock"));
				sprite.showStatus(CharSprite.NEUTRAL, Messages.get(Ghost.class, "crabdef"));
			} else {
				super.damage(dmg, src);
			}
		}

		@Override
		public void die(Object cause) {
			super.die(cause);

			Quest.process();

			Item.autocollect(new MysteryMeat(), pos);
		}

		@Override
		public String description() {
			return Messages.get(Ghost.class, "crabdesc");
		}
	}

	public static class GnollArcher extends Gnoll {

		private static final String TXT_KILLCOUNT = Messages.get(Ghost.class, "killcount");

		{
			name = Messages.get(Ghost.class, "archername");
			spriteClass = GnollArcherSprite.class;

			HP = HT = 30;
			defenseSkill = 5;

			EXP = 1;

			baseSpeed = (1.5f - (Dungeon.depth / 27));

			state = WANDERING;

			loot = Generator.random(Generator.Category.SEED);
			lootChance = 0.01f;

			lootOther = Generator.random(Generator.Category.MUSHROOM);
			lootChanceOther = 0.01f;

		}


		@Override
		public int attackSkill(Char target) {
			return 26;
		}

		@Override
		protected boolean canAttack(Char enemy) {
			Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);
			return !Dungeon.level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
		}

		@Override
		public int damageRoll() {
			return Random.NormalIntRange(1 + Math.round(Statistics.archersKilled / 10), 8 + Math.round(Statistics.archersKilled / 5));
		}


		@Override
		protected boolean getCloser(int target) {
			if (Dungeon.level.adjacent(pos, enemy.pos)) {
				return getFurther(target);
			} else {
				return super.getCloser(target);
			}
		}

		@Override
		public void die(Object cause) {

			if (Dungeon.depth > 25) {
				Item.autocollect(new ForestDart(3), pos);
			}

			Statistics.archersKilled++;
			GLog.h(TXT_KILLCOUNT, Statistics.archersKilled);

			super.die(cause);
			if (Dungeon.depth < 27) {
				Item.autocollect(new SewersKey(), pos);
				explodeDew(pos);
			} else {
				explodeDew(pos);
			}

			if (!Dungeon.limitedDrops.tengukey.dropped() && Dungeon.tengukilled && Statistics.archersKilled > 30 && Random.Int(10) == 0) {
				Dungeon.limitedDrops.tengukey.drop();
				Item.autocollect(new TenguKey(), pos);
			}

			if (!Dungeon.limitedDrops.tengukey.dropped() && Dungeon.tengukilled && Statistics.archersKilled > 50) {
				Dungeon.limitedDrops.tengukey.drop();
				Item.autocollect(new TenguKey(), pos);
			}

			if (!Dungeon.limitedDrops.safespotpage.dropped() && Statistics.archersKilled > 15 && Random.Int(10) == 0) {
				Dungeon.limitedDrops.safespotpage.drop();
				Item.autocollect(new SafeSpotPage(), pos);
			}

			if (!Dungeon.limitedDrops.safespotpage.dropped() && Statistics.archersKilled > 50) {
				Dungeon.limitedDrops.safespotpage.drop();
				Item.autocollect(new SafeSpotPage(), pos);
			}
		}


		@Override
		public String description() {
			return Messages.get(Ghost.class, "archerdesc");
		}


	}
}
