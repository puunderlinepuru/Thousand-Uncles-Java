package com.thousand_uncles.discord_bot;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

public class YamlReader {
    static File file;

    public YamlReader(String fileName) {
        try {
            file = new File( fileName );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @SuppressWarnings("unused")
    public Map<String, Object> yamlRead() {
        Yaml yaml = new Yaml();

        try {
            FileInputStream fileInputStream = new FileInputStream( file );
           return yaml.load(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public void yamlWrite(Map<String, Object> data) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        try{
            FileWriter writer = new FileWriter( file );
            yaml.dump(data, writer);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, ArrayList<String>> yamlDictionaryRead() {
        Yaml yaml = new Yaml();
        try {
            FileInputStream fileInputStream = new FileInputStream( file );
            return yaml.load(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void yamlDictionaryWrite(Map<String, ArrayList<String>> dictionary) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        try{
            FileWriter writer = new FileWriter( file );
            yaml.dump(dictionary, writer);
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
