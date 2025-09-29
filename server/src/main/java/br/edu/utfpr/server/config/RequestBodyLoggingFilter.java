package br.edu.utfpr.server.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class RequestBodyLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        if (request instanceof HttpServletRequest httpRequest) {
            String method = httpRequest.getMethod();
            String uri = httpRequest.getRequestURI();
            
            if (shouldLogRequestBody(method, uri)) {
                CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(httpRequest);
                String body = cachedRequest.getCachedBody();
                
                if (body != null && !body.trim().isEmpty()) {
                    log.info("REQUEST BODY {} {}: {}", method, uri, body);
                }
                
                chain.doFilter(cachedRequest, response);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private boolean shouldLogRequestBody(String method, String uri) {
        return ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) &&
               !uri.contains("/actuator") && !uri.contains("/error");
    }

    private static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private final String cachedBody;

        public CachedBodyHttpServletRequest(HttpServletRequest request) throws IOException {
            super(request);
            InputStream requestInputStream = request.getInputStream();
            this.cachedBody = new String(requestInputStream.readAllBytes(), StandardCharsets.UTF_8);
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new CachedBodyServletInputStream(this.cachedBody.getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new StringReader(this.cachedBody));
        }

        public String getCachedBody() {
            return this.cachedBody;
        }
    }

    private static class CachedBodyServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream buffer;

        public CachedBodyServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }

        @Override
        public int read() throws IOException {
            return buffer.read();
        }

        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }
    }
}
