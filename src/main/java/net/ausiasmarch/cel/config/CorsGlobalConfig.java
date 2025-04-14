package net.ausiasmarch.cel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsGlobalConfig {

    @Bean
    public CorsFilter corsFilter() {
        System.out.println("🟢 Configuración CORS aplicada correctamente");

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("https://cel-frontend.vercel.app")); // Permitir Angular
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
        config.setAllowedHeaders(Arrays.asList("*")); // Permitir todos los headers
        config.setExposedHeaders(Arrays.asList("Content-Disposition")); // Permitir headers en respuesta
        config.setAllowCredentials(true); // Permitir credenciales si es necesario
        config.setMaxAge(3600L); // Cachear configuración CORS por 1 hora

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
