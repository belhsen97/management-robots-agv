package tn.enova.Models.Entitys;


import tn.enova.Enums.Gender;
import tn.enova.Enums.Roles;
import tn.enova.Models.Commons.Attachment;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Document(collection = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable, UserDetails {
    @Id
    String id;
    Date createdAt;
    String username;
    String password;
    Roles role;
    boolean enabled;
    String code;
    String firstname;
    String lastname;
    String matricule;
    int phone;
    String email;
    Gender gender;
    Attachment photo;
    Set<Token> tokens = new HashSet<Token>();


    public void addToken(Token token) {
        if (this.getTokens() == null) {
            this.setTokens(new HashSet<Token>());
        }
        this.tokens.add(token);
    }

    public void removeToken(Token token) {
        if (this.getTokens() == null) {
            return;
        }
        tokens.remove(token);
    }

    public boolean isTokenValid(String token) {
        return this.tokens.stream()
                .anyMatch(userToken -> userToken.getToken().equals(token)
                        && !userToken.isRevoked()
                        && !userToken.isExpired());
    }

    public boolean updateToken(String tokenString, Token tokenUpdate) {
        Optional<Token> optionalToken = tokens.stream()
                .filter(token -> token.getToken().equals(tokenString))
                .findFirst();
        if (optionalToken.isPresent()) {
            Token existingToken = optionalToken.get();
            existingToken.setToken(tokenUpdate.getToken());
            existingToken.setTokenType(tokenUpdate.getTokenType());
            existingToken.setRevoked(tokenUpdate.isRevoked());
            existingToken.setExpired(tokenUpdate.isExpired());
            return true;
        }
        return false;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(this.role.name());
        authorities.add(authority);
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
