/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwfQVwqh74rUY6n+OZ2pUrkn1TkkvO60rFu08DZa
JnQq9JM5z8U7ZX23Z0qRiNYWokvBFiTQ+A20Hocs6X8UYYst9kRBVSBds27/n38LbZQDmlpp
Zctsv/YUXMTVTzTBczhh0WZVpDx00a5cbSC2jW69EF1LRK9voo2+zCPhCcaHUA==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies India Pvt Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2013
 * =====================================
 *
 */
package com.huawei.unibi.molap.engine.scanner;

import com.huawei.unibi.molap.datastorage.store.FileHolder;
import com.huawei.unibi.molap.engine.datastorage.storeInterfaces.DataStore;
import com.huawei.unibi.molap.engine.datastorage.storeInterfaces.DataStoreBlock;
import com.huawei.unibi.molap.engine.datastorage.storeInterfaces.KeyValue;

/**
 * Scanner is the interface provide the way to scan the data source
 * 
 * @author R00900208
 * 
 */
public interface Scanner
{

    /**
     * It requires isDone to be called before calling this interface
     * 
     * @return
     * 
     */
    KeyValue getNext();

    /**
     * 
     * 
     * It ensures the next valid key is ready to be returned on call of getNext
     * 
     * @return true only when the scan is done
     * 
     */
    boolean isDone();

    /**
     * Set the data store and data store block information.  
     * 
     * @param dataStore
     * @param block
     * @param currIndex
     *
     */
    void setDataStore(DataStore dataStore, DataStoreBlock block, int currIndex);
    
    /**
     * Get file holder 
     * @return file holder
     */
    FileHolder getFileHolder();

}
