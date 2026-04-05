package com.thousand_uncles.discord_bot;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class YamlReader {
    static File file;
    String fileName;

    public YamlReader(String fileName) {
        try {
//            this.fileName = fileName;
//            file = new File(fileName);
            file = new File( fileName );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public Map<String, Object> yamlRead() {
        Yaml yaml = new Yaml();

        try {
            FileInputStream fileInputStream = new FileInputStream( file );
//            InputStream fileInputStream = getClass().getResourceAsStream("/" + fileName);
           return yaml.load(fileInputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void yamlWrite(HashMap<String, Object> data) {
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
