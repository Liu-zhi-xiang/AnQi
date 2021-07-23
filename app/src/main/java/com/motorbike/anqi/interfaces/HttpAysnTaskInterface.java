package com.motorbike.anqi.interfaces;

/**
 * 异步任务监听返回接口
 * 
 * @author LS
 * 
 */
public interface HttpAysnTaskInterface {
	public final static int DISMISS_CODE = -100;

	/**
	 * @param tag
	 *            === 请求标签
	 * @param result
	 *            === 返回结果 xml或json
	 * @param complete
	 *            === 请求成功或失败
	 */
//	public void requestComplete(Object tag, int statusCode, Object header,
//								Object result, boolean complete);
	public void requestComplete(Object tag,
                                Object result, boolean complete);
}
