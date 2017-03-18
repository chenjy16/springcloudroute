package com.meiduimall.mzfrouter.hanler.Impl;

import java.util.Enumeration;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.meiduimall.BaseApiCode;
import com.meiduimall.ExceptionUtils;
import com.meiduimall.mzfrouter.ResponsePackUtil;
import com.meiduimall.mzfrouter.hanler.Handler;
import com.netflix.zuul.context.RequestContext;

public class ParamPraseHandler implements Handler{

	private static Logger log = LoggerFactory.getLogger(ParamPraseHandler.class);
	
	
	/**
	 * 功能描述:  解析封装kv请求的参数
	 * Author: 陈建宇
	 * Date:   2016年12月19日 上午10:14:50
	 * @param  ctx
	 * @return Boolean     
	 */
	@Override
	public Boolean process(RequestContext ctx) {
		HttpServletRequest request = ctx.getRequest();
		log.info("kv方式请求参数解析处理层,收到请求方式:{},url:{}:{}", request.getMethod(), request.getRequestURL().toString(),
				request.getQueryString());
		try {
			Map<String, String> param = new HashMap<String, String>();
			Enumeration<String> enumeration = ctx.getRequest().getParameterNames();
			while (enumeration.hasMoreElements()) {
				String key = enumeration.nextElement();
				String value = ctx.getRequest().getParameter(key);
				param.put(key, value);
			}
			ctx.set("param", param);
		} catch (Exception e) {
			log.error("kv方式请求参数解析处理层,收到请求方式:{},url:{},异常信息:{}", request.getMethod(),
					request.getRequestURL().toString(),  ExceptionUtils.getFullStackTrace(e));
			ResponsePackUtil.responseWrapper(ctx, BaseApiCode.GATEWAY_EXCEPTION);
			return false;
		}
		return true;
	}

}
