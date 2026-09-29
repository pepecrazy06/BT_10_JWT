package vn.iotstar.entity;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
@Entity @Table(name="users")
public class User implements UserDetails {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Integer id;
 @Column(nullable=false,length=100) private String fullName;
 @Column(nullable=false,unique=true,length=100) private String email;
 @Column(nullable=false) private String password;
 @Column(nullable=false,length=500) private String images="/images/avatar.svg";
 private Instant createdAt;
 private Instant updatedAt;
 public User() {}
 public User(String name,String email,String password) {this.fullName=name;this.email=email;this.password=password;}
 @PrePersist void create(){createdAt=updatedAt=Instant.now();}
 @PreUpdate void update(){updatedAt=Instant.now();}
 public Integer getId(){return id;}
 public String getFullName(){return fullName;}
 public String getEmail(){return email;}
 public String getImages(){return images;}
 public Instant getCreatedAt(){return createdAt;}
 public Instant getUpdatedAt(){return updatedAt;}
 public String getUsername(){return email;}
 public String getPassword(){return password;}
 public Collection<? extends GrantedAuthority> getAuthorities(){return List.of();}
 public boolean isAccountNonExpired(){return true;}
 public boolean isAccountNonLocked(){return true;}
 public boolean isCredentialsNonExpired(){return true;}
 public boolean isEnabled(){return true;}
}
