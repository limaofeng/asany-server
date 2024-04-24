package cn.asany.storage.data.service;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.UploadException;
import cn.asany.storage.api.UploadOptions;
import cn.asany.storage.api.UploadService;
import cn.asany.storage.core.engine.virtual.VirtualFileObject;
import cn.asany.storage.data.dao.ThumbnailDao;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.Thumbnail;
import cn.asany.storage.utils.UploadUtils;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import lombok.SneakyThrows;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.schedule.service.TaskScheduler;
import org.hibernate.Hibernate;
import org.quartz.*;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 缩略图服务
 *
 * @author limaofeng
 */
@Service
public class ThumbnailService {

  private static final String THUMBNAIL_CACHE_KEY = "thumbnail";
  private static final String TRIGGER_KEY_THUMBNAIL_GROUP = "thumbnail";
  public static final JobKey JOBKEY_GENERATE_THUMBNAIL = JobKey.jobKey("generate", "thumbnail");
  public static final String THUMBNAIL_STORAGE_SPACE_KEY = "wg1NcUDz";

  private final ThumbnailDao thumbnailDao;
  private final TaskScheduler taskScheduler;
  private final UploadService uploadService;

  public ThumbnailService(
      UploadService uploadService, ThumbnailDao thumbnailDao, TaskScheduler taskScheduler) {
    this.uploadService = uploadService;
    this.taskScheduler = taskScheduler;
    this.thumbnailDao = thumbnailDao;
  }

  @Transactional(readOnly = true)
  @Cacheable(
      key = "targetClass + '.' + methodName + '#' + #p0 + ',' + #p1 ",
      value = THUMBNAIL_CACHE_KEY)
  public Optional<Thumbnail> findBySize(String size, Long source) {
    Optional<Thumbnail> optional =
        this.thumbnailDao.findOne(
            PropertyFilter.newFilter().equal("size", size).equal("source.id", source));
    return optional.map(
        item -> {
          Hibernate.initialize(item.getFile());
          return Hibernate.unproxy(item, Thumbnail.class);
        });
  }

  @SneakyThrows({UploadException.class})
  @Transactional(rollbackFor = Exception.class)
  @CachePut(key = "targetClass + '.findBySize#' + #p0 + ',' + #p1", value = THUMBNAIL_CACHE_KEY)
  public Thumbnail save(String size, Long source, String name, File file) {
    FileObject object = UploadUtils.fileToObject(name, file);

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

  @CacheEvict(
      key = "targetClass + '.findBySize#' + #p0 + ',' + #p1",
      value = THUMBNAIL_CACHE_KEY,
      beforeInvocation = true)
  @SneakyThrows({Exception.class})
  public Thumbnail generate(String size, Long source) {
    TriggerKey taskId = generateTaskId(source, size);
    Future<Long> thumbnailFuture = waitForComplete(taskId);
    if (taskScheduler.getTriggerState(taskId) == Trigger.TriggerState.COMPLETE) {
      taskScheduler.removeTrigger(taskId);
    }
    if (!taskScheduler.checkExists(taskId)) {
      Map<String, String> data = new HashMap<>(2);
      data.put("source", source.toString());
      data.put("size", size);
      taskScheduler.scheduleTask(JOBKEY_GENERATE_THUMBNAIL, taskId, data);
    }
    Long thumbnailId = thumbnailFuture.get();
    Thumbnail thumbnail = this.thumbnailDao.getReferenceById(thumbnailId);
    Hibernate.initialize(thumbnail.getFile());
    return Hibernate.unproxy(thumbnail, Thumbnail.class);
  }

  private TriggerKey generateTaskId(Long id, String size) {
    return TriggerKey.triggerKey(id + "_" + size, TRIGGER_KEY_THUMBNAIL_GROUP);
  }

  private Future<Long> waitForComplete(TriggerKey taskId) throws SchedulerException {
    ListenerManager listenerManager = taskScheduler.getListenerManager();
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
