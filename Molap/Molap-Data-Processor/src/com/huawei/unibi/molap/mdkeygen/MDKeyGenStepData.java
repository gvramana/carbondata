/*--------------------------------------------------------------------------------------------------------------------------*/
/*!!Warning: This is a key information asset of Huawei Tech Co.,Ltd                                                         */
/*CODEMARK:kOyQZYzjDpyGdBAEC2GaWmnksNUG9RKxzMKuuAYTdbJ5ajFrCnCGALet/FDi0nQqbEkSZoTs
2wdXgejaKCr1dP3uE3wfvLHF9gW8+IdXbwddts1/q4bCGDA4M3dH8C2PEEMnfDqqdF4ZhcSc
1BeEnKw7UZ7OdnQu2JVp4kJCm5fnB7CdPFSFB+GbH8WJk7mLWEi3B+GCYdOlRcbOC1HxaW4b
X5r6vYvh1iMGpNHgPsc8foFwIvTuujwDwMA/xxPIt0lMjXukJHlkSnCfT1mThw==*/
/*--------------------------------------------------------------------------------------------------------------------------*/
/**
 *
 * Copyright Notice
 * =====================================
 * This file contains proprietary information of
 * Huawei Technologies Ltd.
 * Copying or reproduction without prior written approval is prohibited.
 * Copyright (c) 2011
 * =====================================
 *
 */
package com.huawei.unibi.molap.mdkeygen;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

import com.huawei.unibi.molap.keygenerator.KeyGenerator;

/**
 * Project Name 	: Carbon 
 * Module Name 		: MOLAP Data Processor
 * Author 			: Suprith T 72079 
 * Created Date 	: 25-Aug-2015
 * FileName 		: MDKeyGenStep.java
 * Description 		: Kettle step data to generate MD Key
 * Class Version 	: 1.0
 */
public class MDKeyGenStepData extends BaseStepData implements
        StepDataInterface
{
    /**
     * outputRowMeta
     */
    protected RowMetaInterface outputRowMeta;
    
    /**
     * rowMeta
     */
  //  protected RowMetaInterface rowMeta;
    
    /**
     * generator for each column independently
     */
    protected KeyGenerator[] generator;
    
    /**
     * precomputed default objects
     */
    // public Object[] defaultObjects;

    /**
     * the size of the input rows
     */
    // public int inputSize;

    /**
     * where the key field indexes are
     */
    // public int[] keyFieldIndex;

    /**
     * meta info for a string conversion
     */
    // public ValueMetaInterface[] conversionMeta;
    
}
