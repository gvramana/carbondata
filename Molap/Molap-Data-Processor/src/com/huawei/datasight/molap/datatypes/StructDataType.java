package com.huawei.datasight.molap.datatypes;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.pentaho.di.core.exception.KettleException;

import com.huawei.unibi.molap.keygenerator.KeyGenException;
import com.huawei.unibi.molap.keygenerator.KeyGenerator;
import com.huawei.unibi.molap.surrogatekeysgenerator.csvbased.MolapCSVBasedDimSurrogateKeyGen;

public class StructDataType implements GenericDataType {
	
	private List<GenericDataType> children = new ArrayList<GenericDataType>();
	private String name;
	private String parentname;
	private int outputArrayIndex;
	private int dataCounter;
	
	@Override
	public void addChildren(GenericDataType newChild) {
		if(this.getName().equals(newChild.getParentname()))
		{
			this.children.add(newChild);
		}
		else
		{
			for(GenericDataType child : this.children)
			{
				child.addChildren(newChild);
			}
		}
		
	}
	
	public StructDataType(String name, String parentname)
	{
		this.name = name;
		this.parentname = parentname;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setParentname(String parentname) {
		this.parentname = parentname;
		
	}

	@Override
	public String getParentname() {
		return parentname;
	}
	
	@Override
	public void getAllPrimitiveChildren(List<GenericDataType> primitiveChild) {
		for(int i=0;i<children.size();i++)
		{
			GenericDataType child = children.get(i);
			if (child instanceof PrimitiveDataType) 
			{
				primitiveChild.add(child);
			}
			else
			{
				child.getAllPrimitiveChildren(primitiveChild);
			}
		}
	}
	
	@Override
	public int getSurrogateIndex() {
		return 0;
	}

	@Override
	public void setSurrogateIndex(int surrIndex) {
		
	}

	@Override
	public void parseStringAndWriteByteArray(String tableName, String inputString,
			String[] delimiter, int delimiterIndex,
			DataOutputStream dataOutputStream,
			MolapCSVBasedDimSurrogateKeyGen surrogateKeyGen)
			throws KettleException, IOException {
		if(inputString == null || inputString.equals("null"))
		{
			//Indicates null array
			dataOutputStream.writeInt(0);
		}
		else
		{
			String[] splitInput = inputString.split(delimiter[delimiterIndex]);
			dataOutputStream.writeInt(children.size());
			delimiterIndex = (delimiter.length - 1) == delimiterIndex ? delimiterIndex : delimiterIndex + 1;
			for(int i=0;i<children.size();i++)
			{
				children.get(i).parseStringAndWriteByteArray(tableName, splitInput[i], delimiter, delimiterIndex, dataOutputStream, surrogateKeyGen);
			}
		}
	}
	
	@Override
	public void parseAndBitPack(ByteBuffer byteArrayInput, DataOutputStream dataOutputStream, KeyGenerator[] generator) throws IOException, KeyGenException
	{
		int childElement = byteArrayInput.getInt();
		dataOutputStream.writeInt(childElement);
		for(int i=0;i<childElement;i++)
		{
			if(children.get(i) instanceof PrimitiveDataType)
			{
				dataOutputStream.writeInt(generator[children.get(i).getSurrogateIndex()].getKeySizeInBytes());
			}
			children.get(i).parseAndBitPack(byteArrayInput, dataOutputStream, generator);
		}
	}
	
	@Override
	public int getColsCount() {
		int colsCount = 1;
		for(int i=0;i<children.size();i++)
		{
			colsCount += children.get(i).getColsCount();
		}
		return colsCount;
	}
	
	@Override
	public void setOutputArrayIndex(int outputArrayIndex) {
		this.outputArrayIndex = outputArrayIndex++;
		for(int i=0;i<children.size();i++)
		{
			if(children.get(i) instanceof PrimitiveDataType)
			{
				children.get(i).setOutputArrayIndex(outputArrayIndex++);
			}
			else
			{
				children.get(i).setOutputArrayIndex(outputArrayIndex++);
				outputArrayIndex = getMaxOutputArrayIndex() + 1;
			}
		}
	}
	
	@Override
	public int getMaxOutputArrayIndex()
	{
		int currentMax = outputArrayIndex;
		for(int i=0;i<children.size();i++)
		{
			int childMax = children.get(i).getMaxOutputArrayIndex();
			if(childMax > currentMax)
			{
				currentMax = childMax;
			}
		}
		return currentMax;
	}
	@Override
	public void getColumnarDataForComplexType(
			List<ArrayList<byte[]>> columnsArray, ByteBuffer inputArray) {
		
		ByteBuffer b = ByteBuffer.allocate(8);
		int childElement = inputArray.getInt();
		b.putInt(childElement);
		if(childElement == 0)
		{
			b.putInt(0);
		}
		else
		{
			b.putInt(children.get(0).getDataCounter());
		}
		columnsArray.get(this.outputArrayIndex).add(b.array());

		for(int i=0;i<childElement;i++)
		{
			if(children.get(i) instanceof PrimitiveDataType)
			{
				((PrimitiveDataType) children.get(i)).setKeySize(inputArray.getInt());
			}
			children.get(i).getColumnarDataForComplexType(columnsArray, inputArray);
		}
		this.dataCounter++;
	}
	
	@Override
	public int getDataCounter()
	{
		return this.dataCounter;
	}
	
	@Override
	public void fillAggKeyBlock(List<Boolean> aggKeyBlockWithComplex, boolean[] aggKeyBlock)
	{
		aggKeyBlockWithComplex.add(false);
		for(int i=0;i<children.size();i++)
		{
			children.get(i).fillAggKeyBlock(aggKeyBlockWithComplex,aggKeyBlock);
		}
	}
	
	@Override
	public void fillBlockKeySize(List<Integer> blockKeySizeWithComplex, int[] primitiveBlockKeySize)
	{
		blockKeySizeWithComplex.add(8);
		for(int i=0;i<children.size();i++)
		{
			children.get(i).fillBlockKeySize(blockKeySizeWithComplex, primitiveBlockKeySize);
		}
	}
	
	@Override
	public void fillCardinalityAfterDataLoad(List<Integer> dimCardWithComplex, int[] maxSurrogateKeyArray)
	{
		dimCardWithComplex.add(0);
		for(int i=0;i<children.size();i++)
		{
			children.get(i).fillCardinalityAfterDataLoad(dimCardWithComplex, maxSurrogateKeyArray);
		}
	}
}
