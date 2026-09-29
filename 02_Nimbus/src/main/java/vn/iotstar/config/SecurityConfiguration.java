package vn.iotstar.config;
import vn.iotstar.service.JwtService;
import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfiguration {
 @Bean SecurityFilterChain securityFilterChain(HttpSecurity http,JwtService jwt,UserDetailsService users)throws Exception {
  return http.csrf(c->c.disable()).sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
   .authorizeHttpRequests(a->a.requestMatchers("/","/login","/register","/user/profile","/auth/signup","/auth/login","/css/**","/js/**","/images/**","/error").permitAll().anyRequest().authenticated())
   .exceptionHandling(e->e.authenticationEntryPoint((q,r,x)->{r.setStatus(401);r.setContentType("application/json;charset=UTF-8");r.getWriter().write("{\"message\":\"Vui long dang nhap va gui Bearer token\"}");})
    .accessDeniedHandler((q,r,x)->{r.setStatus(403);r.setContentType("application/json;charset=UTF-8");r.getWriter().write("{\"message\":\"Khong co quyen truy cap\"}");}))
   .addFilterBefore(new JwtAuthenticationFilter(jwt,users),UsernamePasswordAuthenticationFilter.class).build();
 }
}
