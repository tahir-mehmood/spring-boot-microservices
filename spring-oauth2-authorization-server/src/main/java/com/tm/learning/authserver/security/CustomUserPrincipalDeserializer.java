package com.tm.learning.authserver.security;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.MissingNode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomUserPrincipalDeserializer extends JsonDeserializer<CustomUserPrincipal> {

    @Override
    public CustomUserPrincipal deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        // ObjectMapper
        ObjectMapper mapper = (ObjectMapper) jsonParser.getCodec();


        JsonNode jsonNode = mapper.readTree(jsonParser);


        Long id = readJsonNode(jsonNode, "id").asLong();
        Boolean enable = readJsonNode(jsonNode, "enable").asBoolean();
        String username = readJsonNode(jsonNode, "username").asText();
        String password = readJsonNode(jsonNode, "password").asText();

        List<GrantedAuthority> authorities = new ArrayList<>();
        JsonNode authoritiesNode = jsonNode.at("/java.security.Principal/authorities/1");
        if (authoritiesNode.isArray()) {
            for (JsonNode authorityNode : authoritiesNode) {
                authorities.add(new SimpleGrantedAuthority(authorityNode.get("authority").asText()));
            }
        }


        return new CustomUserPrincipal(id, username, password, enable, authorities);
    }

    private JsonNode readJsonNode(JsonNode jsonNode, String field) {
        return jsonNode.has(field) ? jsonNode.get(field) : MissingNode.getInstance();
    }
}
