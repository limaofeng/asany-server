package cn.asany.shanhai.gateway.service;

import cn.asany.shanhai.core.dao.ModelFieldDao;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.gateway.dao.ModelGroupDao;
import cn.asany.shanhai.gateway.domain.ModelGroup;
import com.hankcs.hanlp.corpus.document.sentence.Sentence;
import com.hankcs.hanlp.mining.cluster.ClusterAnalyzer;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import java.util.*;
import lombok.Builder;
import lombok.SneakyThrows;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ModelGroupService {

  @Autowired private ModelGroupDao modelGroupDao;
  @Autowired private ModelFieldDao modelFieldDao;

  public List<ModelGroup> groups() {
    return this.modelGroupDao.findAllWithItems();
  }

  @SneakyThrows
  public void autoGroup() {
    Map<String, ModelFieldState> state = new LinkedHashMap<>();

    List<ModelField> fields = modelFieldDao.findByUngrouped();

    CRFLexicalAnalyzer analyzer = new CRFLexicalAnalyzer();
    ClusterAnalyzer<String> clusterAnalyzer = new ClusterAnalyzer<>();

    for (ModelField field : fields) {
      ModelFieldState.ModelFieldStateBuilder builder = ModelFieldState.builder().current(field);
      if (StringUtil.isNotBlank(field.getName())) {
        builder.words(analyzer.analyze(field.getName()));
      }
      String key = field.getModel().getCode() + "." + field.getCode();
      state.put(key, builder.build());
    }

    for (Map.Entry<String, ModelFieldState> entry : state.entrySet()) {
      clusterAnalyzer.addDocument(
          entry.getKey(),
          entry.getValue().current.getName() + ", " + entry.getValue().current.getCode());
    }

    List<Set<String>> list = clusterAnalyzer.repeatedBisection(1.0);
    for (Set<String> set : list) {
      Map<String, Integer> names = new HashMap<>();

      set.forEach(
          item -> {
            if (state.get(item).words == null) {
              return;
            }
            state
                .get(item)
                .words
                .wordList
                .forEach(
                    word -> {
                      if (word.getValue().length() <= 2) {
                        return;
                      }
                      if (ObjectUtil.exists(
                          new String[] {"Query", "yml", "创建", "查询", "更新", "新增", "修改", "删除"},
                          word.getValue())) {
                        return;
                      }
                      if (!names.containsKey(word.getValue())) {
                        names.put(word.getValue(), 1);
                      } else {
                        names.put(word.getValue(), names.get(word.getValue()) + 1);
                      }
                    });
          });

      Optional<Map.Entry<String, Integer>> optional =
          names.entrySet().stream().max((a, b) -> a.getValue() > b.getValue() ? 1 : -1);

      if (!optional.isPresent()) {
        continue;
      }
      System.out.println(
          optional.get().getKey() + "(" + optional.get().getValue() + ")" + "\t" + set);
    }

    // 1394	118
    System.out.println(state.size() + "\t" + list.size());
  }

  @Builder
  static class ModelFieldState {
    ModelField current;
    Sentence words;
  }
}
