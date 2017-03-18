package com.meiduimall.mzfrouter.hanler.Impl;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.meiduimall.mzfrouter.hanler.Handler;
import com.netflix.zuul.context.RequestContext;

public class HandlerChain implements Handler{
	
	private List<Handler> handlers = new ArrayList<Handler>();
	
	public HandlerChain addProcesser(Handler handler){
	    this.handlers.add(handler);
	    return this;
	}
	
	/**
	 * 功能描述:  
	 * Author: 陈建宇
	 * Date:   2016年12月20日 上午10:47:59
	 */
	@Override
	public Boolean process(RequestContext ctx) {
		Iterator<Handler> it=handlers.iterator();
		while(it.hasNext()){
			Handler handler=it.next();
			Boolean res=handler.process(ctx);
			if(!res){
				break;
			}
		}
		handlers.clear();
		return null;
	}
	
}
