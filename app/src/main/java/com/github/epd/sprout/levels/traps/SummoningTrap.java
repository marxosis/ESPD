/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SummoningTrap {

	private static final float DELAY = 2f;

	private static final Mob DUMMY = new Mob() {
	};

	// 0x770088

	public static void trigger(int pos, Char c) {

		if (Dungeon.bossLevel()) {
			return;
		}

		if (c != null) {
			Actor.occupyCell(c);
		}

		int nMobs = 1;
		if (Random.Int(2) == 0) {
			nMobs++;
			if (Random.Int(2) == 0) {
				nMobs++;
			}
		}

		// It's complicated here, because these traps can be activated in chain

		ArrayList<Integer> candidates = new ArrayList<Integer>();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar(p) == null
					&& (Level.passable[p] || Level.avoid[p])) {
				candidates.add(p);
			}
		}

		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index(candidates);

			DUMMY.pos = candidates.get(index);
			Actor.occupyCell(DUMMY);

			respawnPoints.add(candidates.remove(index));
			nMobs--;
		}

		for (Integer point : respawnPoints) {
			Mob mob = Bestiary.mob(Dungeon.depth);
			mob.state = mob.WANDERING;
			GameScene.add(mob, DELAY);
			WandOfBlink.appear(mob, point);
		}
		
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {heap.summon();}
	}
}
