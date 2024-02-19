package com.iglens.nc.ncUtils;

import ucar.ma2.DataType;
import ucar.nc2.Dimension;

/**
 * nc文件维度实体类
 */
public class NcDimension {

    private Dimension dimension;  //维度变量
    private DataType dataType;  //维度变量数据类型
    private int dimensionIndex; //维度的下标
    private int startIndex;  //维度变量的起始下标
    private int section;    //维度变量从起始下标位置，获取的长度

    public NcDimension() {
    }

    public NcDimension(Dimension dimension, int startIndex, int section) {
        this.dimension = dimension;
        this.startIndex = startIndex;
        this.section = section;
    }

    public Dimension getDimension() {
        return dimension;
    }

    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public int getDimensionIndex() {
        return dimensionIndex;
    }

    public void setDimensionIndex(int dimensionIndex) {
        this.dimensionIndex = dimensionIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }
}