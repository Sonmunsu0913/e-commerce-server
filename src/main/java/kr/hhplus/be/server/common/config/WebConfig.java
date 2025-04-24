package kr.hhplus.be.server.common.config;

import kr.hhplus.be.server.common.interceptor.AuthInterceptor;
import kr.hhplus.be.server.common.interceptor.ProductAuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;
    private final ProductAuthInterceptor productAuthInterceptor;

    public WebConfig(AuthInterceptor authInterceptor,
        ProductAuthInterceptor productAuthInterceptor) {
        this.authInterceptor = authInterceptor;
        this.productAuthInterceptor = productAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/admin/**");

        registry.addInterceptor(productAuthInterceptor)
            .addPathPatterns("/api/product/register");
    }
}

