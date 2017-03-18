package com.meiduimall.mzfrouter.filter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.meiduimall.BaseApiCode;
import com.meiduimall.Constants;
import com.meiduimall.mzfrouter.ResponsePackUtil;
import com.meiduimall.mzfrouter.hanler.Impl.ParamPraseHandler;
import com.meiduimall.mzfrouter.hanler.Impl.HandlerChain;
import com.meiduimall.mzfrouter.hanler.Impl.PraseJsonHandler;
import com.meiduimall.mzfrouter.hanler.Impl.RequiredValidateHandler;
import com.meiduimall.mzfrouter.hanler.Impl.SignValidateHandler;
import com.meiduimall.mzfrouter.hanler.Impl.TimeValidateHandler;
import com.meiduimall.mzfrouter.hanler.Impl.TokenValidateHandler;
import com.meiduimall.redis.util.spring.AppContextLauncher;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter  {
    
	private static Logger log = LoggerFactory.getLogger(AccessFilter.class);
	
    @Autowired
    private ParamPraseHandler paramPraseHandler;
    
    @Autowired
    private PraseJsonHandler praseJsonHandler;
    
    @Autowired
    private RequiredValidateHandler requiredValidateHandler;
    
   /* @Autowired
    private TokenValidateHandler tokenValidateHandler;*/
    
    @Autowired
    private TimeValidateHandler timeValidateHandler;
    
    @Autowired
    private SignValidateHandler signValidateHandler;
    
  
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return Constants.CONSTANT_ZERO_INT;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }
    
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response=ctx.getResponse();
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        try {
            HandlerChain chain=AppContextLauncher.getBean("chain",HandlerChain.class);
        	request.setCharacterEncoding("utf-8");
    		String contentType = ctx.getRequest().getContentType();
			if(Constants.CONTENTTYPE_JSON.equalsIgnoreCase(contentType)){
				chain.addProcesser(praseJsonHandler);
			}else{
				chain.addProcesser(paramPraseHandler);
			}
		    chain.addProcesser(requiredValidateHandler);
		    //chain.addProcesser(tokenValidateHandler);
		    chain.addProcesser(timeValidateHandler);
		    chain.addProcesser(signValidateHandler);
		    chain.process(ctx);
		} catch (Exception e) {
			log.error("网关层过滤器异常,收到请求方式:{},url:{},异常信息:{}",
					request.getMethod(), request.getRequestURL().toString(),e);
		 	ResponsePackUtil.responseWrapper(ctx,BaseApiCode.GATEWAY_EXCEPTION);
			e.printStackTrace();
		}
        return null;
    }

}
