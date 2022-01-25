package userservice.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import userservice.entity.Role;
import userservice.repository.UsersRepository;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final UsersRepository usersRepository;

    @Value("${security.jwt.token.secret-key:secretKey}")
    private String secretKey;

    @Value("${token.life-time}")
    private long validityInMilliseconds;

    @Autowired
    private UserDetailsService userDetailsService;

    private String base64EncodedSecretKey;

    private Key key;

    public JwtTokenProvider(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @PostConstruct
    protected void init() {
        base64EncodedSecretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createToken(String username, List<Role> roles, Long id) {
        List<String> roleList = roles.stream().map(Role::getName).collect(Collectors.toList());
        Claims claims = Jwts.claims().setSubject(username);
        claims.put("roles", roleList);
        claims.put("id", id.toString());

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        return Jwts.builder()//
                .setClaims(claims)//
                .setIssuedAt(now)//
                .setExpiration(validity)//
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(getUsername(token));
        return new UsernamePasswordAuthenticationToken(userDetails,
                "", userDetails.getAuthorities());
    }

    public String getUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(base64EncodedSecretKey)
                .build().parseClaimsJws(token).getBody().getSubject();
    }

    public Long getUserId(String token) {
        return Long.parseLong(Jwts.parserBuilder().setSigningKey(base64EncodedSecretKey)
                .build().parseClaimsJws(token).getBody().get("id").toString());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(base64EncodedSecretKey)
                    .build()
                    .parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String refreshToken(String token) {
        final Date createdDate = new Date();
        final Date expirationDate = calculateExpirationDate(createdDate);

        final Claims claims = getAllClaimsFromToken(token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder().setClaims(claims).signWith(key, SignatureAlgorithm.HS256).compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(base64EncodedSecretKey)
                .build()
                .parseClaimsJws(token).getBody();
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + validityInMilliseconds * 1000);
    }

    public Boolean isUserLoggedIn(String token) {
        String result = usersRepository.getToken(token);
        return result != null && result.equals(token);
    }

    public void clearToken(String token) {
        usersRepository.deleteToken(token);
    }

    public Boolean validateUsersData(HttpServletRequest request, Long userId) {
        String token = resolveToken(request);
        long currentUserId = getUserId(token);
        return currentUserId == userId;
    }
}
