package com.meiduimall.mzfrouter.hanler.Impl;
import java.util.Map;



import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import com.meiduimall.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import com.alibaba.fastjson.JSON;
import com.meiduimall.BaseApiCode;
import com.meiduimall.Constants;
import com.meiduimall.FastJsonUtil;
import com.meiduimall.mzfrouter.ResponsePackUtil;
import com.meiduimall.mzfrouter.hanler.Handler;
import com.meiduimall.password.GatewaySignUtil;
import com.meiduimall.redis.util.JedisUtil;
import com.netflix.zuul.context.RequestContext;


@RefreshScope
public class SignValidateHandler implements Handler {

	private static Logger log = LoggerFactory.getLogger(SignValidateHandler.class);
	
	

	/**
	 * 功能描述: 验证签名 
	 * Author: 陈建宇 
	 * Date: 2016年12月19日 上午10:16:53
	 * @param ctx
	 * @return Boolean
	 */
	@Override
	public Boolean process(RequestContext ctx) {
		HttpServletRequest request = ctx.getRequest();
		Map<String, String> param = (Map<String, String>) ctx.get("param");
		String sign = param.get("sign");
		String clientID = param.get("clientID");
		try {
			String appSecretJson=JedisUtil.getJedisInstance().execGetFromCache(Constants.APP_SECRET_JSON);
			String appSecret = FastJsonUtil.getJsonValue(appSecretJson, clientID);
			if (StringUtils.isEmpty(appSecret)) {
				ResponsePackUtil.responseWrapper(ctx, BaseApiCode.NOT_FOUND_SECRET);
				return false;
			}
			String encodeSign = GatewaySignUtil.sign(appSecret, param);
			if (!sign.equals(encodeSign)) {
				log.info("签名验证处理层,收到请求方式:{},url:{},请求参数:{},服务端签名:{}", request.getMethod(),
						request.getRequestURL().toString(), JSON.toJSONString(param), encodeSign);
				ResponsePackUtil.responseWrapper(ctx, BaseApiCode.SIGN_INVALID);
				return false;
			}
		} catch (Exception e) {
			log.error("签名验证处理层异常,收到请求方式:{},url:{},请求参数:{},异常:{}", request.getMethod(),
					request.getRequestURL().toString(), JSON.toJSONString(param),ExceptionUtils.getFullStackTrace(e));
			ResponsePackUtil.responseWrapper(ctx, BaseApiCode.SIGN_VALIDATE_EXCEPTION);
			return false;
		}
		return true;
	}

}
