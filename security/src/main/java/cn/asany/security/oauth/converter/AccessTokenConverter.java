package cn.asany.security.oauth.converter;

import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import cn.asany.security.oauth.vo.SessionAccessToken;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", builder = @Builder, unmappedTargetPolicy = ReportingPolicy.IGNORE, nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AccessTokenConverter {

    @Mappings({
        @Mapping(source = "id", target = "id"),
        @Mapping(source = "issuedAt", target = "loginTime"),
    })
    SessionAccessToken toSession(AccessToken accessTokens);

    @IterableMapping(elementTargetType = SessionAccessToken.class)
    List<SessionAccessToken> toSessions(List<AccessToken> accessTokens);

    PersonalAccessToken toPersonalAccessToken(AccessToken token);

    @IterableMapping(elementTargetType = PersonalAccessToken.class)
    List<PersonalAccessToken> toPersonalAccessTokens(List<AccessToken> accessTokens);
}
