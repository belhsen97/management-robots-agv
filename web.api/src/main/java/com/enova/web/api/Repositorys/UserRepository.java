package com.enova.web.api.Repositorys;

import com.enova.web.api.Enums.Roles;
import com.enova.web.api.Models.Entitys.Token;
import com.enova.web.api.Models.Entitys.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
      Optional<User>  findByUsername(String username);
      boolean existsByUsername(String username);
      Optional<User> findByCodeAndUsername(String code,String username);
      Optional<User> findByUsernameAndEmail (String username,String email);
      Optional<User> findUserByCode(String code);
      List<User> findUsersByRole(Roles role);
      @Query(value = "{ 'tokens.token' : ?0 }"/*, fields = "{'tokens': 1}"*/)
      Optional<User> findUserByTokenValue(String token);


      @Query(value = "{'id': ?0, 'tokens': { $elemMatch: { $or: [ { 'revoked': false }, { 'expired': false } ] } } }")
      Optional<User> findTokensValidByUserId(String userId);


      default boolean isExistsValidToken(String token) {
           Optional<User> user =  findUserByTokenValue( token);
           if ( user.isEmpty() ){ return false;}
            return user.get().isTokenValid(token);
      }



      default Optional<Token> findTokenByTokenValue(String token) {
            Optional<User> userOptional =  findUserByTokenValue( token);
            return userOptional.flatMap(user -> user.getTokens().stream()
                  .filter(userToken -> userToken.getToken().equals(token))
                  .findFirst());
      }


      default boolean updateToken(String tokenValue , Token token) {
          Optional<User> userOptional =  findUserByTokenValue(token.getToken());
          if ( userOptional.isEmpty()){return false;}
          userOptional.get().updateToken(tokenValue , token);
           save(userOptional.get());
           return true;
          //          userOptional.ifPresent(user -> user.getTokens().stream()
//                  .filter(userToken -> userToken.getToken().equals(token.getToken()))
//                  .findFirst()
//                  .ifPresent(foundToken -> {
//                      foundToken.setExpired(token.isExpired());
//                      foundToken.setRevoked(token.isRevoked());
//                      foundToken.setTokenType(token.setTokenType());
//                      save(user);
//                  }));
      }

}
