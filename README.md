# Bài tập 10 JWT — Spring Boot 3 / Spring Security 6
Thái Nhựt Huy
24110227
## Hai yêu cầu
1. `01_JJWT`: thực hiện demo trang 15–34 của 04_JWT.pdf bằng JJWT 0.12.6.
2. `02_Nimbus`: giữ nguyên chức năng, thay JJWT bằng Nimbus JOSE + JWT 10.0.2.


## Chạy bằng IntelliJ IDEA
1. Giải nén ZIP. File > Open > chọn `02_Nimbus/pom.xml` > Open as Project.
2. Chọn Project SDK JDK 17 hoặc 21, Maven Reload để tải thư viện.
3. Dùng MySQL: chạy `database.sql` trong MySQL Workbench.
4. Sửa username/password trong `src/main/resources/application-mysql.properties`, hoặc đặt Environment variables trong Run Configuration: `DB_USERNAME=root;DB_PASSWORD=mat_khau_mysql_cua_ban`.
5. Run `vn.iotstar.JwtApplication`.
6. Mở http://localhost:8005/register để tạo tài khoản, rồi http://localhost:8005/login.



## API theo bài giảng
| Method | URL | Quyền | Kết quả |
|---|---|---|---|
| POST | /auth/signup | Public | Tạo người dùng, mật khẩu BCrypt |
| POST | /auth/login | Public | token, expiresIn (mili giây) |
| GET | /users/me | Bearer token | Hồ sơ hiện tại |
| GET | /users/ hoặc /users | Bearer token | Danh sách người dùng |

API được bảo vệ cần header `Authorization: Bearer <token>`.
Trang HTML `/user/profile` là khung công khai để JavaScript gọi API; dữ liệu `/users/me` vẫn yêu cầu token.

## Điểm thay đổi sang Nimbus
Chỉ logic JWT trong `service/JwtService.java` và dependency trong pom.xml cần thay đổi; phần còn lại giữ luồng tương đương.
| JJWT | Nimbus |
|---|---|
| Jwts.builder() | JWTClaimsSet.Builder + SignedJWT |
| signWith(key, HS256) | signedJWT.sign(new MACSigner(key)) |
| compact() | serialize() |
| parser().verifyWith().parseSignedClaims() | SignedJWT.parse() + verify(new MACVerifier(key)) |
| Claims | JWTClaimsSet |

Nimbus cần kiểm tra rõ HS256, chữ ký, issuer, subject, issued-at, expiration và not-before. Parse token đơn thuần không chứng minh token hợp lệ.
Key được đọc dạng UTF-8 ở cả hai bản, tối thiểu 32 byte; không trộn key dạng hex/base64 với key UTF-8.

## Cấu trúc
- entity/User, repository/UserRepository: JPA và MySQL.
- model/: dữ liệu request/response; không trả mật khẩu/hash qua API.
- service/: đăng ký, xác thực, truy vấn người dùng, ký/kiểm tra JWT.
- config/: PasswordEncoder, AuthenticationManager, SecurityFilterChain, JWT filter.
- controller/: REST APIs, điều hướng Thymeleaf, xử lý lỗi.
- templates/login.html, register.html, profile.html; static/js/mainjs.js: giao diện và AJAX fetch.
- src/test/: test API và chữ ký/issuer/hạn token với H2.


