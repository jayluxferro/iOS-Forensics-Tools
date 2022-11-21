/*-
 * Copyright (C) 2008 Erik Larsson
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

package org.catacombae.hfsexplorer.types.hfscommon;

import java.io.PrintStream;
import org.catacombae.csjc.StructElements;
import org.catacombae.csjc.structelements.Dictionary;
import org.catacombae.hfsexplorer.FastUnicodeCompare;
import org.catacombae.hfsexplorer.Util;
import org.catacombae.hfsexplorer.types.hfsplus.HFSPlusAttributeKey;
import org.catacombae.hfsexplorer.types.hfsplus.HFSPlusCatalogKey;
import org.catacombae.hfsexplorer.types.hfsx.HFSXKeyCompareType;
import org.catacombae.hfsexplorer.types.hfs.CatKeyRec;

/**
 *
 * @author erik
 */
public abstract class CommonHFSAttributeKey extends CommonBTKey<CommonHFSAttributeKey> implements StructElements {
	@Override
	public void print(PrintStream ps, String prefix) {
		// TODO Auto-generated method stub
		
	}   

    public static CommonHFSAttributeKey create(HFSPlusAttributeKey key) {
        return new HFSPlusImplementation(key);
    }
    
    public static class HFSPlusImplementation extends CommonHFSAttributeKey {
        private final HFSPlusAttributeKey key;
        
        public HFSPlusImplementation(HFSPlusAttributeKey key) {
            this.key = key;
        }

        @Override
        public byte[] getBytes() {
            return key.getBytes();
        }
        
        @Override
        public int compareTo(CommonHFSAttributeKey o) {
        
            HFSPlusImplementation k = (HFSPlusImplementation) o;
            return key.compareTo(k.key);
        }

        @Override
        public void printFields(PrintStream ps, String prefix) {
            ps.println(prefix + "key:");
            key.print(ps, prefix + " ");
        }

		@Override
		public Dictionary getStructElements() {
			return key.getStructElements();
		}

		@Override
		public int maxSize() {
			return key.maxSize();
		}

		@Override
		public int occupiedSize() {
			return key.occupiedSize();
		}

    }
}
