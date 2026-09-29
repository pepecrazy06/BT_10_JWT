package vn.iotstar.config;
import vn.iotstar.service.JwtService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
public class JwtAuthenticationFilter extends OncePerRequestFilter {
 private final JwtService jwt;private final UserDetailsService users;
 public JwtAuthenticationFilter(JwtService j,UserDetailsService u){jwt=j;users=u;}
 protected void doFilterInternal(HttpServletRequest req,HttpServletResponse res,FilterChain chain)throws ServletException,IOException {
  String header=req.getHeader("Authorization");
  if(header!=null && header.startsWith("Bearer ")){
   try{
    UserDetails user=users.loadUserByUsername(jwt.extractUsername(header.substring(7)));
    new AccountStatusUserDetailsChecker().check(user);
    SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities()));
   }catch(AuthenticationException e){
    SecurityContextHolder.clearContext();res.setStatus(e instanceof AccountStatusException?403:401);
    res.setContentType("application/json;charset=UTF-8");res.getWriter().write("{\"message\":\"Token khong hop le, het han hoac tai khoan khong kha dung\"}");return;
   }
  }
  chain.doFilter(req,res);
 }
}
