package cn.asany.security.oauth.convert;

import cn.asany.security.oauth.bean.AccessToken;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import cn.asany.security.oauth.vo.SessionAccessToken;
import java.util.List;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface AccessTokenConverter {

  @Mappings({
    @Mapping(source = "id", target = "id"),
    @Mapping(source = "clientDetails.device", target = "device"),
    @Mapping(source = "clientDetails.ip", target = "ip"),
    @Mapping(source = "clientDetails.lastIp", target = "lastIp"),
    @Mapping(source = "clientDetails.location", target = "location"),
    @Mapping(source = "clientDetails.lastLocation", target = "lastLocation"),
    @Mapping(source = "issuedAt", target = "loginTime"),
    @Mapping(source = "lastUsedTime", target = "lastUsedTime"),
  })
  SessionAccessToken toSession(AccessToken accessTokens);

  @IterableMapping(elementTargetType = SessionAccessToken.class)
  List<SessionAccessToken> toSessions(List<AccessToken> accessTokens);

  PersonalAccessToken toPersonalAccessToken(AccessToken token);

  @IterableMapping(elementTargetType = PersonalAccessToken.class)
  List<PersonalAccessToken> toPersonalAccessTokens(List<AccessToken> accessTokens);
}
