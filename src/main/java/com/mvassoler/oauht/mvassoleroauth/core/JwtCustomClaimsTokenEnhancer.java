package com.mvassoler.oauht.mvassoleroauth.core;

import java.util.HashMap;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

//classe para adicionar claims no token
//executado antes da emissão do token
public class JwtCustomClaimsTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken oAuth2AccessToken, OAuth2Authentication oAuth2Authentication) {
        if(oAuth2Authentication.getPrincipal() instanceof AuthUser) { //teste garante se é a instância de AuthUser na autenticação
            var authUser = (AuthUser) oAuth2Authentication.getPrincipal(); //pegar o authUser da autenticação

            var info = new HashMap<String, Object>(); //mapa para novos claims
            info.put("name", authUser.getFullName());
            info.put("user_id", authUser.getUserId());

            var novoAuth2AccessToken = (DefaultOAuth2AccessToken) oAuth2AccessToken;
            novoAuth2AccessToken.setAdditionalInformation(info); //acrescentando os claims
        }
        return oAuth2AccessToken;
    }
}
