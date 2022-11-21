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

import org.catacombae.hfsexplorer.Util;

public class HFSPlusAttributeLeafNode extends BTLeafNode {
	protected HFSPlusAttributeLeafRecord[] leafRecords;

	public HFSPlusAttributeLeafNode(byte[] data, int offset, int nodeSize) {
		this(data, offset, nodeSize, null);
	}
	protected HFSPlusAttributeLeafNode(byte[] data, int offset, int nodeSize, BTHeaderRec attributesHeaderRec) {
		super(data, offset, nodeSize);
		short[] offsets = new short[Util.unsign(nodeDescriptor.getNumRecords())+1];
		for(int i = 0; i < offsets.length; ++i) {
			offsets[i] = Util.readShortBE(data, offset+nodeSize-((i+1)*2));
		}
		leafRecords = new HFSPlusAttributeLeafRecord[offsets.length-1];
		// we loop offsets.length-1 times, since last offset is offset to free space
		for(int i = 0; i < leafRecords.length; ++i) {
			int currentOffset = Util.unsign(offsets[i]);
			leafRecords[i] = new HFSPlusAttributeLeafRecord(data, offset+currentOffset);
		}
	}

	public HFSPlusAttributeLeafRecord getLeafRecord(int index) { return leafRecords[index]; }
	public HFSPlusAttributeLeafRecord[] getLeafRecords() {
		HFSPlusAttributeLeafRecord[] copy = new HFSPlusAttributeLeafRecord[leafRecords.length];
		for(int i = 0; i < copy.length; ++i)
			copy[i] = leafRecords[i];
		return copy;
	}
}
