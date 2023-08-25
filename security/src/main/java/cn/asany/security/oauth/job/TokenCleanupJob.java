package cn.asany.security.oauth.job;

import cn.asany.security.oauth.service.AccessTokenService;
import java.util.Date;
import org.quartz.*;

public class TokenCleanupJob implements Job {

  public static final JobKey JOBKEY_TOKEN_CLEANUP = JobKey.jobKey("cleanup", "token");
  ;
  private final AccessTokenService accessTokenService;

  public TokenCleanupJob(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }

  public static TriggerKey triggerKey(String tokenValue) {
    return TriggerKey.triggerKey(tokenValue, JOBKEY_TOKEN_CLEANUP.getGroup());
  }

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap jobDataMap = context.getMergedJobDataMap();
    String tokenValue = jobDataMap.getString("tokenValue");

    accessTokenService.cleanupExpiredToken(tokenValue);
  }

  public static JobDataMap jobData(String tokenValue) {
    JobDataMap dataMap = new JobDataMap();
    dataMap.put("tokenValue", tokenValue);
    return dataMap;
  }

  public static String triggerDescription(String triggerKey, Date expiresAt) {
    return "triggerKey: " + triggerKey + ", 预计于: " + expiresAt + " 过期";
  }
}
