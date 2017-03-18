package com.meiduimall.mzfrouter.config;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import com.meiduimall.mzfrouter.filter.AccessFilter;
import com.meiduimall.mzfrouter.hanler.Impl.ParamPraseHandler;
import com.meiduimall.mzfrouter.hanler.Impl.HandlerChain;
import com.meiduimall.mzfrouter.hanler.Impl.PraseJsonHandler;
import com.meiduimall.mzfrouter.hanler.Impl.RequiredValidateHandler;
import com.meiduimall.mzfrouter.hanler.Impl.SignValidateHandler;
import com.meiduimall.mzfrouter.hanler.Impl.TimeValidateHandler;
import com.meiduimall.mzfrouter.hanler.Impl.TokenValidateHandler;

import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;


/**
 * 配置文件
 *
 */
@Configuration
@ImportResource("classpath:applicationContext-common.xml")
@ConfigurationProperties(prefix="config")
public class ApplicationConfig {
	
	
	@Scope("prototype")
	@Bean("chain")
    public HandlerChain HandlerChain() {
        return new HandlerChain();
    }
	
	/**
	 * 功能描述: 请求kv参数解析处理器
	 * Author: 陈建宇
	 * Date:   2016年12月19日 下午3:18:22
	 * return ParamPraseHandler   
	 */
	@Bean
    public ParamPraseHandler ParamPraseHandler() {
        return new ParamPraseHandler();
    }
	
	/**
	 * 功能描述: 必填参数验证处理器
	 * Author: 陈建宇
	 * Date:   2016年12月19日 下午3:18:42
	 * return RequiredValidateHandler   
	 */
  	@Bean
    public RequiredValidateHandler RequiredValidateHandler() {
        return new RequiredValidateHandler();
    }
  	
  	/**
  	 * 功能描述: token验证处理器
  	 * Author: 陈建宇
  	 * Date:   2016年12月19日 下午3:19:00
  	 * return TokenValidateHandler   
  	 */
 	/*@Bean
    public TokenValidateHandler TokenValidateHandler() {
        return new TokenValidateHandler();
    }*/
 	
 	
 	/**
 	 * 功能描述: 签名验证处理器
 	 * Author: 陈建宇
 	 * Date:   2016年12月19日 下午3:19:32
 	 * return SignValidateHandler   
 	 */
 	@Bean
    public SignValidateHandler SignValidateHandler() {
        return new SignValidateHandler();
    }
 	
 	/**
 	 * 功能描述: 时间戳验证处理器
 	 * Author: 陈建宇
 	 * Date:   2016年12月19日 下午3:19:50
 	 * return TimeValidateHandler   
 	 */
	@Bean
    public TimeValidateHandler TimeValidateHandler() {
        return new TimeValidateHandler();
    }
	
	
	/**
	 * 功能描述:  Json解析处理器
	 * Author: 陈建宇
	 * Date:   2016年12月20日 上午11:02:22   
	 * return  PraseJsonHandler
	 */
	@Bean
    public PraseJsonHandler PraseJsonHandler() {
        return new PraseJsonHandler();
    }
	
	
 	/**
 	 * 功能描述:  zuul过滤器
 	 * Author: 陈建宇
 	 * Date:   2016年12月20日 上午11:02:56   
 	 * return  AccessFilter
 	 */
 	@Bean
	public AccessFilter accessFilter(){
		return new AccessFilter();
	}

 	
 	
 	
 	
	@Bean(name ="shardedJedisPool")
	public ShardedJedisPool shardedJedisPool(@Qualifier("jedis.config")JedisPoolConfig config,
			@Qualifier("jedisShardInfo") JedisShardInfo jedisShardInfo) {
		List<JedisShardInfo> list=new ArrayList<JedisShardInfo>();
		list.add(jedisShardInfo);
		return new ShardedJedisPool(config,list);
	}

	
	@Bean(name="jedisShardInfo")
	public JedisShardInfo jedisShardInfo(@Value("${config.jedis.jedisShardInfo.host}") String host,
			@Value("${config.jedis.jedisShardInfo.port}") int port,
			@Value("${config.jedis.jedisShardInfo.password}") String password) {
		JedisShardInfo jedisShardInfo=new JedisShardInfo(host, port);
		jedisShardInfo.setPassword(password);
		return jedisShardInfo;
	}
	
	
	@Bean(name = "jedis.config")
	public JedisPoolConfig jedisPoolConfig(@Value("${config.jedis.maxTotal}") int maxTotal,
			@Value("${config.jedis.maxIdle}") int maxIdle,
			@Value("${config.jedis.maxWaitMillis}")int maxWaitMillis,
			@Value("${config.jedis.testOnBorrow}")boolean testOnBorrow,
			@Value("${config.jedis.testOnReturn}")boolean testOnReturn) {
		
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(maxTotal);
		config.setMaxIdle(maxIdle);
		config.setMaxWaitMillis(maxWaitMillis);
		return config;
	}
 	
 	
}
