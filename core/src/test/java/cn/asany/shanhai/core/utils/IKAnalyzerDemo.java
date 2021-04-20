package cn.asany.shanhai.core.utils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.store.Directory;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import org.wltea.analyzer.lucene.IKAnalyzer;


import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class IKAnalyzerDemo {
    public static void main(String[] args) throws IOException {
        //Lucene Document的域名
        Map<String, Integer> frequencies = new HashMap<>();
        //检索内容
        String content = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。 ";
        IKSegmenter ikSegmenter = new IKSegmenter(new StringReader(content), true);

        Lexeme lexeme;
        while ((lexeme = ikSegmenter.next()) != null) {
            final String text = lexeme.getLexemeText();

            if (text.length() > 1) {
                //递增
                if (frequencies.containsKey(text)) {
                    frequencies.put(text, frequencies.get(text) + 1);
                } else {//首次出现
                    frequencies.put(text, 1);
                }
            }
        }
        System.out.println(frequencies);

    }
}
