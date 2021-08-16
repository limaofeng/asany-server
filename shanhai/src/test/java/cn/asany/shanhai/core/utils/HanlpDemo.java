package cn.asany.shanhai.core.utils;

import com.hankcs.hanlp.mining.cluster.ClusterAnalyzer;
import java.io.IOException;

public class HanlpDemo {

  public static void main(String[] args) throws IOException {

    ClusterAnalyzer<String> analyzer = new ClusterAnalyzer<String>();
    analyzer.addDocument(
        "赵一", "流行, 流行, 流行, 流行, 流行, 流行, 流行, 流行, 流行, 流行, 蓝调, 蓝调, 蓝调, 蓝调, 蓝调, 蓝调, 摇滚, 摇滚, 摇滚, 摇滚");
    analyzer.addDocument(
        "钱二", "爵士, 爵士, 爵士, 爵士, 爵士, 爵士, 爵士, 爵士, 舞曲, 舞曲, 舞曲, 舞曲, 舞曲, 舞曲, 舞曲, 舞曲, 舞曲");
    analyzer.addDocument("张三", "古典, 古典, 古典, 古典, 民谣, 民谣, 民谣, 民谣");
    analyzer.addDocument(
        "李四", "爵士, 爵士, 爵士, 爵士, 爵士, 爵士, 爵士, 爵士, 爵士, 金属, 金属, 舞曲, 舞曲, 舞曲, 舞曲, 舞曲, 舞曲");
    analyzer.addDocument("王五", "流行, 流行, 流行, 流行, 摇滚, 摇滚, 摇滚, 嘻哈, 嘻哈, 嘻哈");
    analyzer.addDocument("马六", "古典, 古典, 古典, 古典, 古典, 古典, 古典, 古典, 摇滚");

    System.out.println(analyzer.kmeans(3));
    System.out.println(analyzer.repeatedBisection(3));
    System.out.println(analyzer.repeatedBisection(1.0)); // 自动判断聚类数量k
  }
}
