package webA.webA.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;


public class Myfilter implements Filter {

	@Autowired
	private static StringRedisTemplate template;
	
	private static String SSOUrl = "";//System.getProperty("SSO.url");

	public Myfilter(StringRedisTemplate template2) {
		template = template2;
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) arg0;
		Cookie mycookies[] = httpServletRequest.getCookies();
		boolean flag = true;
		if (mycookies != null) {
			for (int i = 0; i < mycookies.length; i++) {
				if ("ticket".equalsIgnoreCase(mycookies[i].getName())) {
					String username = template.opsForValue().get(mycookies[i].getValue());
					if (StringUtils.isNotEmpty(username)) {
						template.expire(mycookies[i].getValue(),10,TimeUnit.SECONDS);
						arg0.setAttribute("username", username);
						flag = false;
						arg2.doFilter(arg0, arg1);
					}
				}
			}
		}
		if(flag){
			if(StringUtils.isEmpty(SSOUrl)){
				SSOUrl = System.getProperty("SSO.url");
			}
			arg1.setContentType("text/html");
			PrintWriter out = arg1.getWriter();
			out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
			out.println("<HTML>");
			out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
			out.println("  <BODY>");
			out.print("<input id='SSOurl' style='display:none' value='"+SSOUrl+"'/>");
			out.print("<script type='text/javascript' src='" + SSOUrl + "/js/jquery.js'></script>");
			out.print("<script type='text/javascript' src='" + SSOUrl + "/js/verify.js'></script>");
			out.println("  </BODY>");
			out.println("</HTML>");
			out.flush();
			out.close();
		}
		

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
