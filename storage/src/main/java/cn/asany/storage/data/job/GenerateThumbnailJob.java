package cn.asany.storage.data.job;

import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Thumbnail;
import cn.asany.storage.data.service.FileService;
import cn.asany.storage.data.service.ThumbnailService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.util.FFmpegUtil;
import org.jfantasy.framework.util.ImageUtil;
import org.jfantasy.framework.util.common.StreamUtil;
import org.jfantasy.framework.util.common.file.FileUtil;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;

@Slf4j
public class GenerateThumbnailJob implements Job {

  @Autowired private FileService fileService;
  @Autowired private StorageResolver storageResolver;
  @Autowired private ThumbnailService thumbnailService;
  @Autowired private CacheManager cacheManager;

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    JobDataMap data = context.getMergedJobDataMap();
    Long source = Long.valueOf((String) data.get("source"));
    String size = (String) data.get("size");

    FileDetail fileDetail = fileService.getFileById(source);

    Storage storage = storageResolver.resolve(fileDetail.getStorageConfig().getId());

    List<File> temps = new ArrayList<>();

    File temp = FileUtil.tmp();

    temps.add(temp);

    try {

      String sourcePath = temp.getAbsolutePath();

      String thumbnailPath = null;
      if (fileDetail.getMimeType().startsWith("image/")) {
        storage.readFile(fileDetail.getStorePath(), new FileOutputStream(temp));

        thumbnailPath = ImageUtil.resize(sourcePath, size);
        temps.add(new File(thumbnailPath));
      } else if (fileDetail.getMimeType().startsWith("video/")) {

        InputStream input =
            storage.readFile(fileDetail.getStorePath(), new long[] {0, 1024 * 1024 * 30});
        StreamUtil.copyThenClose(input, new FileOutputStream(temp));

        long length = FFmpegUtil.duration(sourcePath);
        log.debug(" 视频长度: " + length);

        long location = (length / 60) > 14 ? 60 : 30;

        String imagPath;
        do {
          imagPath = FFmpegUtil.image2(sourcePath, location);
          temps.add(new File(imagPath));

          ImageUtil.ImageMetadata metadata = ImageUtil.identify(imagPath);

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
          thumbnailService.save(size, fileDetail.getId(), new File(thumbnailPath));

      context.setResult(thumbnail.getId());

    } catch (IOException e) {
      throw new JobExecutionException(e.getMessage(), e);
    } finally {
      temps.forEach(FileUtil::delFile);
    }
  }
}
