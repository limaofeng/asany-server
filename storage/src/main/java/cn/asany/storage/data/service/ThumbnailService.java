package cn.asany.storage.data.service;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Thumbnail;
import cn.asany.storage.data.dao.ThumbnailDao;
import cn.asany.storage.utils.UploadUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.SneakyThrows;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.schedule.service.SchedulerUtil;
import org.quartz.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ThumbnailService {

  private static final String THUMBNAIL_CACHE_KEY = "thumbnail";
  private static final String TRIGGER_KEY_THUMBNAIL_GROUP = "thumbnail";
  public static final JobKey JOBKEY_GENERATE_THUMBNAIL = JobKey.jobKey("generate", "thumbnail");
  public static final String THUMBNAIL_STORAGE_SPACE_KEY = "thumbnail";

  private final ThumbnailDao thumbnailDao;
  private final SchedulerUtil schedulerUtil;
  private final UploadService uploadService;

  public ThumbnailService(
      UploadService uploadService, ThumbnailDao thumbnailDao, SchedulerUtil schedulerUtil) {
    this.uploadService = uploadService;
    this.schedulerUtil = schedulerUtil;
    this.thumbnailDao = thumbnailDao;
  }

  private Thumbnail getById(Long id) {
    return this.thumbnailDao.getById(id);
  }

  @Transactional(readOnly = true)
  @Cacheable(
      key = "targetClass + '.' + methodName + '#' + #p0 + ',' + #p1 ",
      value = THUMBNAIL_CACHE_KEY)
  public Optional<Thumbnail> findBySize(String size, Long source) {
    Optional<Thumbnail> optional =
        this.thumbnailDao.findOne(
            PropertyFilter.builder().equal("size", size).equal("source.id", source).build());
    return optional.map(
        item -> {
          Hibernate.initialize(item.getFile());
          return Hibernate.unproxy(item, Thumbnail.class);
        });
  }

  @Transactional
  @SneakyThrows
  @CachePut(key = "targetClass + '.findBySize#' + #p0 + ',' + #p1", value = THUMBNAIL_CACHE_KEY)
  public Thumbnail save(String size, Long source, File file) {
    FileObject object = UploadUtils.fileToObject(file);

    UploadOptions uploadOptions =
        UploadOptions.builder().space(THUMBNAIL_STORAGE_SPACE_KEY).build();

    VirtualFileObject fileObject = (VirtualFileObject) uploadService.upload(object, uploadOptions);

    return this.thumbnailDao.save(
        Thumbnail.builder()
            .source(FileDetail.builder().id(source).build())
            .size(size)
            .file(FileDetail.builder().id(fileObject.getId()).build())
            .build());
  }

  @SneakyThrows
  @CacheEvict(
      key = "targetClass + '.findBySize#' + #p0 + ',' + #p1",
      value = THUMBNAIL_CACHE_KEY,
      beforeInvocation = true)
  public Thumbnail generate(String size, Long source) {
    TriggerKey taskId = generateTaskId(source, size);
    Future<Long> thumbnailFuture = waitForComplete(taskId);
    if (schedulerUtil.getTriggerState(taskId) == Trigger.TriggerState.COMPLETE) {
      schedulerUtil.removeTrigdger(taskId);
    }
    if (!schedulerUtil.checkExists(taskId)) {
      Map<String, String> data = new HashMap<>();
      data.put("source", source.toString());
      data.put("size", size);
      schedulerUtil.addTrigger(JOBKEY_GENERATE_THUMBNAIL, taskId, data);
    }
    Long thumbnailId = thumbnailFuture.get();
    Thumbnail thumbnail = this.thumbnailDao.getById(thumbnailId);
    Hibernate.initialize(thumbnail.getFile());
    return Hibernate.unproxy(thumbnail, Thumbnail.class);
  }

  private TriggerKey generateTaskId(Long id, String size) {
    return TriggerKey.triggerKey(id + "_" + size, TRIGGER_KEY_THUMBNAIL_GROUP);
  }

  private Future<Long> waitForComplete(TriggerKey taskId) throws SchedulerException {
    ListenerManager listenerManager = schedulerUtil.getListenerManager();
    CompletableFuture<Long> future = new CompletableFuture<>();

    listenerManager.addTriggerListener(
        new WaitForCompleteTriggerListener(future) {}, item -> item.equals(taskId));

    return future;
  }

  public static class WaitForCompleteTriggerListener implements TriggerListener {
    private final CompletableFuture<Long> future;

    public WaitForCompleteTriggerListener(CompletableFuture<Long> future) {
      this.future = future;
    }

    @Override
    public String getName() {
      return "Thumbnail.WaitForComplete";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
      System.out.println(trigger);
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
      System.out.println(trigger);
      return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
      System.out.println(trigger);
    }

    @Override
    public void triggerComplete(
        Trigger trigger,
        JobExecutionContext context,
        Trigger.CompletedExecutionInstruction triggerInstructionCode) {
      Long id = (Long) context.getResult();
      if (id == null) {
        future.completeExceptionally(new Exception("生成缩略图失败"));
      }
      future.complete(id);
    }
  }
}
