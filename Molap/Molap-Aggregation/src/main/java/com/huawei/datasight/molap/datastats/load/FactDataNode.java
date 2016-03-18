package com.huawei.datasight.molap.datastats.load;

import com.huawei.unibi.molap.datastorage.store.FileHolder;
import com.huawei.unibi.molap.datastorage.store.columnar.ColumnarKeyStore;
import com.huawei.unibi.molap.datastorage.store.columnar.ColumnarKeyStoreDataHolder;
import com.huawei.unibi.molap.datastorage.store.columnar.ColumnarKeyStoreInfo;
import com.huawei.unibi.molap.datastorage.store.compression.ValueCompressionModel;
import com.huawei.unibi.molap.datastorage.util.StoreFactory;
import com.huawei.unibi.molap.metadata.LeafNodeInfoColumnar;
import com.huawei.unibi.molap.util.MolapUtil;

/**
 * This class contains compressed Columnkey block of store and Measure value
 * 
 * @author A00902717
 *
 */
public class FactDataNode
{

	/**
	 * Compressed keyblocks
	 */
	private ColumnarKeyStore keyStore;


	private int maxKeys;


	public FactDataNode(int maxKeys, int[] eachBlockSize, boolean isFileStore,
			FileHolder fileHolder, LeafNodeInfoColumnar leafNodeInfo,
			ValueCompressionModel compressionModel)
	{

		this.maxKeys = maxKeys;
		
		ColumnarKeyStoreInfo columnarStoreInfo = MolapUtil
				.getColumnarKeyStoreInfo(leafNodeInfo, eachBlockSize,null);
		keyStore = StoreFactory.createColumnarKeyStore(columnarStoreInfo,
				fileHolder, isFileStore);

	}

	public ColumnarKeyStoreDataHolder[] getColumnData(FileHolder fileHolder,
			int[] dimensions, boolean[] needCompression)
	{
		
	   	ColumnarKeyStoreDataHolder[] keyDataHolderUncompressed = keyStore
				.getUnCompressedKeyArray(fileHolder, dimensions,
						needCompression);

		return keyDataHolderUncompressed;

	}

	public int getMaxKeys()
	{
		return maxKeys;
	}

}
