package com.placement.expo.dto;

import lombok.Builder;
import lombok.Data;
import java.util.Set;

@Data
@Builder
public class AuthResponse {
    private String appwriteId;
    private String email;
    private String name;
    private Set<String> roles;
    private String token;
}
