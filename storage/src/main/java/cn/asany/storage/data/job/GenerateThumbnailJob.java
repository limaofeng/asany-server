package cn.asany.storage.data.job;

import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.FileDetail;
import cn.asany.storage.data.domain.Thumbnail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.ThumbnailService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.FFmpeg;
import org.jfantasy.framework.util.Images;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.cache.CacheManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 生成缩略图
 * @author limaofeng
 */
@Slf4j
public class GenerateThumbnailJob implements Job {

  private final FileService fileService;
  private final  StorageResolver storageResolver;
  private final ThumbnailService thumbnailService;
  private final CacheManager cacheManager;

    public GenerateThumbnailJob(FileService fileService, StorageResolver storageResolver, ThumbnailService thumbnailService, CacheManager cacheManager) {
        this.fileService = fileService;
        this.storageResolver = storageResolver;
        this.thumbnailService = thumbnailService;
        this.cacheManager = cacheManager;
    }

    @SneakyThrows({Exception.class})
  @Override
  public void execute(JobExecutionContext context) {
    JobDataMap data = context.getMergedJobDataMap();
    Long source = Long.valueOf((String) data.get("source"));
    String size = (String) data.get("size");

    FileDetail fileDetail = fileService.getFileById(source);

    Storage storage = storageResolver.resolve(fileDetail.getStorageConfig().getId());

    List<Path> temps = new ArrayList<>();

    Path temp = FileUtil.tmp();

    temps.add(temp);

    try {

      String sourcePath = temp.toString();

      String thumbnailPath = null;
      if (fileDetail.getMimeType().startsWith("image/")) {
        storage.readFile(fileDetail.getStorePath(), Files.newOutputStream(temp));

        thumbnailPath = Images.resize(sourcePath, size);
        temps.add(Paths.get(thumbnailPath));
      } else if (fileDetail.getMimeType().startsWith("video/")) {

        InputStream input =
            storage.readFile(fileDetail.getStorePath(), new long[] {0, 1024 * 1024 * 30});
        StreamUtil.copyThenClose(input, Files.newOutputStream(temp));

        long length = FFmpeg.duration(sourcePath);
        log.debug(" 视频长度: " + length);

        long location = (length / 60) > 14 ? 60 : 30;

        String imagPath;
        do {
          imagPath = FFmpeg.image2(sourcePath, location);
          temps.add(Paths.get(imagPath));

          Images.ImageMetadata metadata = Images.identify(imagPath);

          int r = metadata.getChannelStatistics().getRed().getMean();
          int g = metadata.getChannelStatistics().getGreen().getMean();
          int b = metadata.getChannelStatistics().getBlue().getMean();

          double luma = 0.2126 * r + 0.7152 * g + 0.0722 * b;

          if (luma > 40) {
            break;
          }

          location += 60;
        } while (location < length);
        thumbnailPath = imagPath;
      }

      assert thumbnailPath != null;
      Thumbnail thumbnail =
          thumbnailService.save(
              size, fileDetail.getId(), fileDetail.getName(), new File(thumbnailPath));

      context.setResult(thumbnail.getId());

    } catch (IOException e) {
      throw new JobExecutionException(e.getMessage(), e);
    } finally {
        for(Path temp1 : temps){
            FileUtil.rm(temp1);
        }
    }
  }
}
