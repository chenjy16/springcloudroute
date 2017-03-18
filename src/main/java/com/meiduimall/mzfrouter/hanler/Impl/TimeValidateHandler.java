package com.meiduimall.mzfrouter.hanler.Impl;

import java.util.Map;



import javax.servlet.http.HttpServletRequest;

import com.meiduimall.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.meiduimall.BaseApiCode;
import com.meiduimall.Constants;
import com.meiduimall.mzfrouter.ResponsePackUtil;
import com.meiduimall.mzfrouter.hanler.Handler;
import com.netflix.zuul.context.RequestContext;

public class TimeValidateHandler implements Handler {
	private static Logger log = LoggerFactory.getLogger(TimeValidateHandler.class);

	
	/**
	 * 功能描述:  验证时间戳
	 * Author: 陈建宇
	 * Date:   2016年12月20日 上午10:41:50
	 * @param  ctx
	 * @return Boolean   
	 */
	@Override
	public Boolean process(RequestContext ctx) {
		HttpServletRequest request = ctx.getRequest();
		Map<String, String> param = (Map<String, String>) ctx.get("param");
		log.info("时间戳是否超时验证处理层,收到请求方式:{},url:{},请求参数:{}", request.getMethod(), request.getRequestURL().toString(),
				JSON.toJSONString(param));
		try {
			long requestTime = Long.valueOf(param.get("timestamp"));
			long curTime = System.currentTimeMillis();
			if ((curTime - requestTime) > Constants.CONSTANT_FIVEMINUTE) {
				ResponsePackUtil.responseWrapper(ctx, BaseApiCode.ILLEGAL_CALL);
				return false;
			}
		} catch (Exception e) {
			log.error("时间戳是否超时验证处理层,收到请求方式:{},url:{},请求参数:{},异常:{}", request.getMethod(),
					request.getRequestURL().toString(), JSON.toJSONString(param),ExceptionUtils.getFullStackTrace(e));
			ResponsePackUtil.responseWrapper(ctx, BaseApiCode.TIMESTAMP_VALIDATE_EXCEPTION);
			return false;
		}
		return true;
	}

}
