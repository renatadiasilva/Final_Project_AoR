package pt.uc.dei.aor.pf.credentials;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;


@WebFilter(urlPatterns = "*")
public class CredentialsCatchFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain next) throws IOException, ServletException {

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			if (httpServletRequest.getSession().getAttribute("principal") == null
					&& httpServletRequest.getUserPrincipal() != null) {
				httpServletRequest.getSession().setAttribute("principal", httpServletRequest.getUserPrincipal());
			}
		}
		next.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
	}

}

