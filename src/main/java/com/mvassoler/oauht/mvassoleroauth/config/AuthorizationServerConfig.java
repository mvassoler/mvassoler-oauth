package com.mvassoler.oauht.mvassoleroauth.config;

import com.mvassoler.oauht.mvassoleroauth.core.JwtCustomClaimsTokenEnhancer;
import com.mvassoler.oauht.mvassoleroauth.granters.PkceAuthorizationCodeTokenGranter;
import java.util.Arrays;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    //bean no WebSecurityConfig
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;
/*
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;*/

    //configurando em memória as credenciais do authorization-server
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.jdbc(dataSource);

               /* .inMemory()
                .withClient("mvassoler-web")
                    .secret(passwordEncoder.encode("mv@1973"))
                    .authorizedGrantTypes("password", "refresh_token")
                    .scopes("write", "read")
                    .accessTokenValiditySeconds(60)
                    .refreshTokenValiditySeconds(120)
                .and()
                .withClient("app-mobile")
                    .secret(passwordEncoder.encode("mvmob@1973"))
                    .authorizedGrantTypes("outrograndtype", "password")
                    .scopes("write", "read")
                    .accessTokenValiditySeconds(60)
                .and()
                .withClient("faturamento")
                    .secret(passwordEncoder.encode("fat@2022"))
                    .authorizedGrantTypes("client_credentials")
                    .scopes("write", "read")
                .and()
                    .withClient("escritorio_fulano")
                        .secret(passwordEncoder.encode("esc@2022"))
                        .authorizedGrantTypes("authorization_code")
                        .scopes("write", "read")
                        .redirectUris("http://localhost:8989/login")
                .and()
                //implicit não pode ter refresh token e não possui senha
                    .withClient("webadmin")
                        .authorizedGrantTypes("implicit")
                        .scopes("write", "read")
                        .redirectUris("http://localhost:9000/home");*/
    }

    //permite o recurso check-token
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//		security.checkTokenAccess("isAuthenticated()");
        security.checkTokenAccess("permitAll()") //permitinido o acesso aos recursos check token
                .tokenKeyAccess("permitAll()") //permitindo o acesso aos recursos da chave
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        var enhancerChaim = new TokenEnhancerChain();  //cadeia de instâncias de enhancer
        enhancerChaim.setTokenEnhancers(Arrays.asList(new JwtCustomClaimsTokenEnhancer(), jwtAccessTokenConverter())); //adicionando os enhancer, incluíndo a customização
        endpoints
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService)
                .reuseRefreshTokens(false) //define que o refresh token pode ser utilizado uma única vez, e no próximo access_token um novo refresh é gerado
                //.tokenStore(this.redisTokenStore())
                .accessTokenConverter(this.jwtAccessTokenConverter())
                .tokenEnhancer(enhancerChaim) //adicionando a cadeia de enhancer
                .approvalStore(approvalStore(endpoints.getTokenStore())) //ajuste dos aprove de scopo depois de configurar o jwt
                .tokenGranter(tokenGranter(endpoints)); //instancia o tokengranter com PKCE;
    }

    private TokenGranter tokenGranter(AuthorizationServerEndpointsConfigurer endpoints) {
        var pkceAuthorizationCodeTokenGranter = new PkceAuthorizationCodeTokenGranter(endpoints.getTokenServices(),
                endpoints.getAuthorizationCodeServices(), endpoints.getClientDetailsService(),
                endpoints.getOAuth2RequestFactory());
        var granters = Arrays.asList(
                pkceAuthorizationCodeTokenGranter, endpoints.getTokenGranter());
        return new CompositeTokenGranter(granters);
    }

  /*  private TokenStore redisTokenStore(){
        return new RedisTokenStore(this.redisConnectionFactory);
    }
*/

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(){
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //jwtAccessTokenConverter.setSigningKey("algaworks");
        //comando para criar o jks
        //keytool -genkeypair -alias mvassoler-auth -keyalg RSA -keypass 123456 -keystore mvassoler.jks -storepass 12345678
        var jksResource = new ClassPathResource("keystores/mvassoler.jks"); //local do arquivo
        var keyStorePass = "12345678"; //senha de acesso ao arquivo criado no keystore
        var keyPairAlias = "mvassoler-auth"; //nome do alias
        var keyStoreKeyFactory = new KeyStoreKeyFactory(jksResource, keyStorePass.toCharArray());
        var keyPair = keyStoreKeyFactory.getKeyPair(keyPairAlias); //pegando a chave privada
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return  jwtAccessTokenConverter;
    }

    private ApprovalStore approvalStore(TokenStore tokenStore){
        var aprovalStore = new TokenApprovalStore();
        aprovalStore.setTokenStore(tokenStore);
        return aprovalStore;
    }
}
