package com.thousand_uncles.data;
import com.thousand_uncles.data.util.RecognizedMapsChecker;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DataApplication {
    static void main() {
        List<String> missingMaps = RecognizedMapsChecker.checkMapListValidity();
        assert missingMaps != null;
        if (!missingMaps.isEmpty()){
            StringBuilder complaint = new StringBuilder("tell pumpum that");
            for ( String map : missingMaps){
                complaint.append("\n").append(map);
            }
            complaint.append("\nare missing and are not tracked");
            IO.println(complaint);
        }
    }
}
