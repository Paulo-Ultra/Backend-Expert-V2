package models.reponses;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token,
        String type
) {
}
