package com.motorbike.anqi.interfaces;

/**
 * 异步任务监听返回接口
 * 
 * @author LS
 * 
 */
public interface HttpRequestTag {

	/**
	 * @param tag
	 *            === 请求标签
	 * @param result
	 *            === 返回结果 xml或json
	 * @param complete
	 *            === 请求成功或失败
	 */

	 void requestComplete(Integer tag, Object result, String msg, boolean complete);
}
