package com.meiduimall.mzfrouter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;

public class BodyRequestWrapper extends HttpServletRequestWrapper{
	
	private final byte[] body;  
	
	public BodyRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		body=IOUtils.toByteArray(request.getInputStream());
	}
	
	@Override  
    public BufferedReader getReader() throws IOException {  
        return new BufferedReader(new InputStreamReader(getInputStream()));  
    }  
	  
    @Override  
    public ServletInputStream getInputStream() throws IOException {  
        final ByteArrayInputStream bis = new ByteArrayInputStream(body);  
        return new ServletInputStream() {  
            @Override  
            public int read() throws IOException {  
                return bis.read();  
            }

			@Override
			public boolean isFinished() {
				return false;
			}

			@Override
			public boolean isReady() {
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				
			}
        };  
    }  

}
