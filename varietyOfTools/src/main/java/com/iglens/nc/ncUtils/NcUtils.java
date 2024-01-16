package com.iglens.nc.ncUtils;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.Variable;

public interface NcUtils {

    /**
     * 读取变量中的全部数据，对于 3 维及以上的数据，是切片的数据
     * 即某一变量的某一层数据
     *
     * @return 如果是 3 维数据，取 0 维 0 层切片；如果是 4 维数据，取 0 维 0 层、 1 维 0 层切片；
     * @throws IOException           变量读取数据{@code variable.read()}时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     * @param variable
     */
    NcData getDataOfSlice(Variable variable) throws IOException, InvalidRangeException;

    /**
     * 读取变量中的全部数据，对于 3 维及以上的数据，是切片的数据
     * 即某一变量的某一层数据
     *
     * @param variable    具体的某一变量
     * @param positionOf0 如果是 3 维数据，该参数代表第 0 维切片层数；如果是 4 维数据，该参数代表第 1 维切片层数
     * @return 如果是 3 维数据，取 0 维 positionOf0 层切片；如果是 4 维数据，取 0 维 0 层、1 维 positionOf0 层切片
     * @throws IOException           变量读取数据{@code variable.read()}时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     */
    NcData getDataOfSlice(Variable variable, @Nullable Integer positionOf0) throws IOException, InvalidRangeException;

    /**
     * 读取变量中的全部数据，对于 3 维及以上的数据，是切片的数据
     * 即某一变量的某一层数据
     *
     * @param variable    具体的某一变量
     * @param positionOf0 如果是 3 维数据，该参数代表第 0 维切片层数；如果是 4 维数据，该参数代表第 1 维切片层数
     * @param positionOf1 如果是 3 维数据，不会使用该参数；如果是 4 维数据，该参数代表第 0 维切片层数
     * @return 如果是 3 维数据，取 0 维 positionOf0 层切片；如果是 4 维数据，取 0 维 positionOf1 层、1 维 positionOf0 层切片
     * @throws IOException           变量读取数据{@code variable.read()}时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     */
    NcData getDataOfSlice(Variable variable, @Nullable Integer positionOf0, @Nullable Integer positionOf1) throws IOException, InvalidRangeException;

    /**
     * 读取变量集合中的全部数据，对于 3 维及以上的数据，是切片的数据
     *
     * @param variables 变量列表
     * @return 如果是 3 维数据，取 0 维 0 层切片；如果是 4 维数据，取 0 维 0 层、 1 维 0 层切片；
     * @throws IOException           变量读取数据 {@code variable.read()} 时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     */
    Map<String, NcData> getAllDataOfSlice(List<Variable> variables) throws IOException, InvalidRangeException;

    /**
     * 读取变量集合中的全部数据，对于 3 维及以上的数据，是切片的数据
     *
     * @param variables   变量列表
     * @param positionOf0 如果是 3 维数据，该参数代表第 0 维切片层数；如果是 4 维数据，该参数代表第 1 维切片层数
     * @return 如果是 3 维数据，取 0 维 positionOf0 层切片；如果是 4 维数据，取 0 维 0 层、1 维 positionOf0 层切片
     * @throws IOException           变量读取数据 {@code variable.read()} 时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     */
    Map<String, NcData> getAllDataOfSlice(List<Variable> variables, @Nullable Integer positionOf0) throws IOException, InvalidRangeException;

    /**
     * 读取变量集合中的全部数据，对于 3 维及以上的数据，是切片的数据
     *
     * @param variables   变量列表
     * @param positionOf0 如果是 3 维数据，该参数代表第 0 维切片层数；如果是 4 维数据，该参数代表第 1 维切片层数
     * @param positionOf1 如果是 3 维数据，不会使用该参数；如果是 4 维数据，该参数代表第 0 维切片层数
     * @return 如果是 3 维数据，取 0 维 positionOf0 层切片；如果是 4 维数据，取 0 维 positionOf1 层、1 维 positionOf0 层切片
     * @throws IOException           变量读取数据 {@code variable.read()} 时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     */
    Map<String, NcData> getAllDataOfSlice(List<Variable> variables, @Nullable Integer positionOf0, @Nullable Integer positionOf1) throws IOException, InvalidRangeException;

    /**
     * 设置维度条件，读取变量中的数据
     * 该方法至多设置3个维度条件
     *
     * @param variable     具体的某一变量
     * @param ncDimensions 维度实体类
     * @return  返回带有限制参数的维度对应变量nc数据，以及变量nc数据
     * @throws IOException           变量读取数据{@code variable.read()}时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     */
    NcData getDataWithLimit(@NotNull Variable variable,@NotNull NcDimension... ncDimensions) throws IOException, InvalidRangeException;

    /**
     * 设置维度条件，读取变量集合中的所有数据
     * 该方法至多设置3个维度条件
     *
     * @param variables     变量列表
     * @param ncDimensions 维度实体类
     * @return  返回带有限制参数的维度对应变量nc数据，以及变量列表nc数据
     * @throws IOException           变量读取数据{@code variable.read()}时抛出的异常
     * @throws InvalidRangeException 切片过程中，重新生成 Range 对象时抛出的范围异常，已做保护措施，应该不会抛出该异常
     */
    Map<String,NcData> getAllDataWithLimit(@NotNull List<Variable> variables,@NotNull NcDimension... ncDimensions) throws IOException, InvalidRangeException;

}