package cn.trans.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import java.io.Serializable;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * 返回数据
 *
 * @author mxy
 * @date 2019年6月5日
 */
@Data
@ApiModel("执行结果")
@NoArgsConstructor
@Builder
public class R<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final int OK = 200;

    /**
     * 状态码
     */
    @ApiModelProperty("状态码")
	private int code = OK;

	/**
	 * 执行状态
	 */
    @ApiModelProperty("反馈信息")
	private String message;

    /**
     * 查询结果
     */
    @ApiModelProperty("数据")
	private T data;

	public R(int code) {
		this.code = code;
	}

	public R(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public R(int code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public static R ok() {
		return new R(0, "成功");
	}
	public static R ok(Object data) {
		return new R(OK, "成功", data);
	}


	public static R error() {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), null);
	}
	public static R error(int code) {
		return error(code, "系统错误");
	}
	public static R error(String msg) {
		return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
	}
	public static R error(int code, String msg) {
		return new R(code, msg);
	}


	public static R code(int code) {
		return new R(code);
	}

    @ApiOperation("放入数据")
	public R data(T data){
		this.data = data;
		return this;
	}

    @ApiOperation("获取数据")
	public T data(){
		return data;
	}

	@ApiOperation("放入信息")
	public R message(String message){
		this.message = message;
		return this;
	}

}
