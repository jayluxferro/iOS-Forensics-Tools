package org.catacombae.hfsexplorer.iphone;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESFastEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.catacombae.dmgextractor.Util;
import org.catacombae.hfsexplorer.io.ReadableRandomAccessSubstream;
import org.catacombae.hfsexplorer.io.SynchronizedReadableRandomAccess;
import org.catacombae.io.RuntimeIOException;

/**
 * blocksize is 0x1000 on iPad1,1 but 0x2000 on iPhone3,1
 * XXX: seeking will probably fail, this class is a hack
 *
 */
public class EMFRandomAccess extends ReadableRandomAccessSubstream {
	private byte[] fileKey;
	private byte[] ivKey;
	private int blockSize;
	private BufferedBlockCipher emf_encrypter;
	private BufferedBlockCipher filekey_decrypter;
	private KeyParameter fileKeyParam;
	private KeyParameter emfKeyParam;

	private int baseLBA;
	private int currentBlockOffset;
	private int cprotect_version;
	
	public EMFRandomAccess(SynchronizedReadableRandomAccess iSourceStream, byte[] key, long bSize, int xattr_major_version) {
		super(iSourceStream);
		fileKey = key;
		cprotect_version = xattr_major_version;
		ivKey = new byte[16];
		blockSize = (int) bSize;
		emf_encrypter = new BufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
		filekey_decrypter = new BufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
		currentBlockOffset = 0; //HAX, must be reset if objet is read twice
		emfKeyParam = new KeyParameter(EMF.getInstance().getEMFKey());
		fileKeyParam = new KeyParameter(fileKey);
		baseLBA = EMF.getInstance().getBaseLBA();
		
		if (xattr_major_version >= 4)
		{
			try {
				MessageDigest md = MessageDigest.getInstance("SHA-1");
				byte[] sha1hash = new byte[40];
			    md.update(fileKey);
			    sha1hash = md.digest();
			    System.arraycopy(sha1hash, 0, ivKey, 0, 16);
			    System.out.println("IV key = " + Util.byteArrayToHexString(ivKey));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
   
    @Override
    public int read(byte[] b, int pos, int len) throws RuntimeIOException {
    	int nblocks = len / blockSize;
    	int bytesRead = 0;
    	long lba = internalFP/blockSize;
    	int offset = (int) (internalFP % blockSize);
        long oldFP = getFilePointer();
    	long newFP = lba * blockSize;
    	seek(newFP);
    	int i;
    	for(i=0; i < nblocks; i++)
    	{
    		bytesRead += readBlock(lba, offset, b, bytesRead, blockSize - offset);
    		lba += 1;
    		offset = 0;
    	}
    	if ((len % blockSize) != 0)
    		bytesRead += readBlock(lba, 0, b, bytesRead, len % blockSize);
    	return bytesRead;
    }
    
    /**
     * Reencrypt data fork blocks with the EMF key and decrypt the result with 
     * the correct file key
     * @param lba
     * @param offset
     * @param b
     * @param boffset
     * @param len
     * @return
     */
    private int readBlock(long lba, int offset, byte[] b, int boffset, int len) {
        byte[] iv = EMF.generateAESIVforLBA((int) (baseLBA+lba));
        //System.out.println("fileBlockNumber=" + x + " iv2=" + Util.byteArrayToHexString(iv2));

        //System.out.println("lba ="+lba);
        byte[] nullIV = new byte[16];
        byte[] fileIV = new byte[16];

        for(int i=0; i<16; i++)
        	nullIV[i] = 0;
         
		try {
			emf_encrypter.init(true, new ParametersWithIV(emfKeyParam, iv));
			
        	byte[] block = new byte[blockSize];
        	byte[] block_ciphertext = new byte[blockSize];
			
	        int z = super.read(block, 0, blockSize);
	        emf_encrypter.processBytes(block, 0, blockSize, block_ciphertext, 0);
	        
	        if(cprotect_version == 2)
	        {
	        	filekey_decrypter.init(false, new ParametersWithIV(fileKeyParam, iv));
	        	filekey_decrypter.processBytes(block_ciphertext, 0, blockSize, block, 0);
	        }
	        else if (cprotect_version == 4)
	        {
		        for(int i=0; i < blockSize/0x1000; i++)
		        {
		            byte[] iv2 = EMF.generateAESIVforLBA((int) (this.currentBlockOffset));
		            this.currentBlockOffset += 0x1000;
					BufferedBlockCipher filekey_enc = new BufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()));
					filekey_enc.init(true, new ParametersWithIV(new KeyParameter(ivKey), nullIV));
					filekey_enc.processBytes(iv2, 0, 16, fileIV, 0);

					filekey_decrypter.init(false, new ParametersWithIV(fileKeyParam, fileIV));
		        	filekey_decrypter.processBytes(block_ciphertext, i*0x1000, 0x1000, block, i*0x1000);
		        }
	        }
	        /*//TODO inspect slack space
	        if (len != 4096)
	        	System.out.println(Util.byteArrayToHexString(block));
	        	
        	*/
	        System.arraycopy(block, offset, b, boffset, len);
	        return len;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new RuntimeException(e);
		} 
        
    }

}
