package org.rateLimiter;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.rateLimiter.config.Configurations;
import org.rateLimiter.policy.FixedCounterWindowAlgorithm;
import org.rateLimiter.policy.RateLimiter;
import org.rateLimiter.policy.SlidingCounterWindowAlgorithm;
import org.rateLimiter.policy.TokenBucketAlgorithm;
import org.rateLimiter.resource.RateLimiterResource;
import org.rateLimiter.service.IRateLimiterService;
import org.rateLimiter.service.RateLimiterService;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

public class Main extends Application<Configurations> {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        try {
            main.run(args);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void configureCors(Environment environment) {
        FilterRegistration.Dynamic filter = environment.servlets().addFilter("CORS", CrossOriginFilter.class);
        filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,PUT,POST,DELETE,OPTIONS");
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER, "*");
        filter.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER, "*");
        filter.setInitParameter("allowedHeaders", "Content-Type,Authorization,X-Requested-With,Content-Length,Accept,Origin,x-access-token");
        filter.setInitParameter("allowCredentials", "true");
    }

    @Override
    public void initialize(Bootstrap<Configurations> bootstrap) {

    }

    @Override
    public void run(Configurations configurations, Environment environment) {
        RateLimiter rateLimiter;

//        rateLimiter = new TokenBucketAlgorithm(1, 10);
//        rateLimiter = new FixedCounterWindowAlgorithm(10, 20);
        rateLimiter = new SlidingCounterWindowAlgorithm(60, 60);

        IRateLimiterService rateLimiterService = new RateLimiterService(rateLimiter);

        RateLimiterResource rateLimiterResource = new RateLimiterResource(rateLimiterService);

        environment.jersey().register(rateLimiterResource);

        configureCors(environment);
    }
}