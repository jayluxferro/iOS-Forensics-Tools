/*-
 * Copyright (C) 2006 Erik Larsson
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catacombae.hfsexplorer.types.hfsplus;

import java.io.PrintStream;

public class HFSPlusAttributeLeafRecord {
	protected final HFSPlusAttributeKey key;
	protected final HFSPlusAttributeData recordData;

	public HFSPlusAttributeLeafRecord(byte[] data, int offset) {
		this(data, offset, null);
	}
	protected HFSPlusAttributeLeafRecord(byte[] data, int offset, BTHeaderRec catalogHeaderRec) {
		key = new HFSPlusAttributeKey(data, offset);
		recordData = new HFSPlusAttributeData(data, offset+key.length());
	}

	public HFSPlusAttributeKey getKey() { return key; }
	public HFSPlusAttributeData getData() { return recordData; }

	public void printFields(PrintStream ps, String prefix) {
		ps.println(prefix + " key:");
		key.printFields(ps, prefix + "  ");
		ps.println(prefix + " recordData:");
		recordData.printFields(ps, prefix + "  ");
	}
	public void print(PrintStream ps, String prefix) {
		ps.println(prefix + "HFSPlusCatalogLeafRecord:");
		printFields(ps, prefix);
	}
}
