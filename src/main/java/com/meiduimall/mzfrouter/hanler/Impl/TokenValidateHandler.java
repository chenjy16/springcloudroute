package com.meiduimall.mzfrouter.hanler.Impl;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang.StringUtils;
import com.meiduimall.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.meiduimall.BaseApiCode;
import com.meiduimall.Constants;
import com.meiduimall.mzfrouter.ResponsePackUtil;
import com.meiduimall.mzfrouter.hanler.Handler;
import com.meiduimall.redis.util.JedisUtil;
import com.netflix.zuul.context.RequestContext;

public class TokenValidateHandler implements Handler {

	private static Logger log = LoggerFactory.getLogger(TokenValidateHandler.class);

	/**
	 * 功能描述: 验证token Author: 陈建宇 
	 * Date: 2016年12月19日 上午10:17:46
	 * @param ctx
	 * @return Boolean
	 */
	@Override
	public Boolean process(RequestContext ctx) {
		HttpServletRequest request = ctx.getRequest();
		Map<String, String> param = (Map<String, String>) ctx.get("param");
		try {
			String whiteListJson = JedisUtil.getJedisInstance().execGetFromCache(Constants.WHITE_LIST_STR);
			log.info("用户是否登录验证处理层,收到请求方式:{},url:{},请求参数:{},白名单:{}", request.getMethod(),
					request.getRequestURL().toString(), JSON.toJSONString(param), whiteListJson);
			// 判断服务是不是不在白名单,不在需要进行token验证
			if (!isWhiteList(request.getRequestURL().toString(), whiteListJson)) {
				String token = param.get("token");
				if (StringUtils.isEmpty(token)) {// 判断请求中token参数是否存在
					ResponsePackUtil.responseWrapper(ctx, BaseApiCode.TOKEN_NOT_EXISTS);
					return false;
				}
				Boolean LoggedIn = JedisUtil.getJedisInstance().execExistsFromCache(token);
				if (!LoggedIn) {// 判断是否登录
					ResponsePackUtil.responseWrapper(ctx, BaseApiCode.USER_NOT_LOGGED_IN);
					return false;
				}
			}
		} catch (Throwable e) {
			log.error("用户是否登录验证处理层异常,收到请求方式:{},url:{},请求参数:{},异常:{}", request.getMethod(),
					request.getRequestURL().toString(), JSON.toJSONString(param),ExceptionUtils.getFullStackTrace(e));
			ResponsePackUtil.responseWrapper(ctx, BaseApiCode.TOKEN_VALIDATE_EXCEPTION);
			return false;
		}
		return true;
	}

	private Boolean isWhiteList(String url, String whiteListJson) {
		if (StringUtils.isNotEmpty(whiteListJson)) {
			String[] whites = whiteListJson.split(";");
			for (String key : whites) {
				if (url.contains(key)) {
					return true;
				}
			}
		}
		return false;
	}

}
