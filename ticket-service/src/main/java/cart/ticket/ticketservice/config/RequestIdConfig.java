//package cart.ticket.ticketservice.config;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RequestIdConfig {
//    @Bean
//    public FilterRegistrationBean<RequestIdFilter> requestIdFilter() {
//        FilterRegistrationBean<RequestIdFilter> registrationBean
//                = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new RequestIdFilter());
//        registrationBean.addUrlPatterns("/*");
//        return registrationBean;
//    }
//}