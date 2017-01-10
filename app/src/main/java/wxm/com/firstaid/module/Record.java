package wxm.com.firstaid.module;

/**
 * Created by Zero on 12/9/2016.
 */

public class Record {
    String disease_id;
    String content;

    public Record(String content) {
        this.content = content;
    }

    public String getDisease_id() {
        return disease_id;
    }

    public void setDisease_id(String disease_id) {
        this.disease_id = disease_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
