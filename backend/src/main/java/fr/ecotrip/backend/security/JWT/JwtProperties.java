package fr.ecotrip.backend.security.JWT;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Classe de configuration représentant les propriétés liées au JWT.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties("security.jwt")
public class JwtProperties {

    /**
     * Clé secrète utilisée pour signer et valider les tokens JWT.
     */
    private String secret;
}
