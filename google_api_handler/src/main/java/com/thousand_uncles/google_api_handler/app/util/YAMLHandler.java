package com.thousand_uncles.google_api_handler.app.util;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

public class YAMLHandler {
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
    public static void yamlWriteList(String fileName, Map<String, List<String>> data) {
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
}
