package com.thousand_uncles.discord_bot.bot;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Map;

public class YamlReader {
    static File file;

    @SuppressWarnings("unused")
    public static Map<String, Object> yamlRead(String fileName) {
        file = new File( fileName );
        Yaml yaml = new Yaml();

        try {
            FileInputStream fileInputStream = new FileInputStream( file );
           return yaml.load(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unused")
    public static void yamlWrite(String fileName, Map<String, Object> data) {
        file = new File( fileName );
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

    public static Map<String, ArrayList<String>> yamlDictionaryRead(String fileName) {
        file = new File( fileName );
        Yaml yaml = new Yaml();
        try {
            FileInputStream fileInputStream = new FileInputStream( file );
            return yaml.load(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void yamlDictionaryWrite(String fileName, Map<String, ArrayList<String>> dictionary) {
        file = new File( fileName );
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
