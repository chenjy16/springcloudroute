package com.meiduimall.mzfrouter.hanler;
import com.netflix.zuul.context.RequestContext;

public interface Handler {
	
	
	Boolean process(RequestContext ctx);
}
