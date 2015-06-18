package mbreader.mrsmyx.com.mbreader.structs;

import java.util.HashMap;

/**
 * Created by Charlton on 6/11/2015.
 */
public class PDF {

    private String link;

    public String getLink() {
        return link;
    }

    public String getCreated() {
        return created;
    }

    public String getUploaded() {
        return uploaded;
    }

    public String getFile() {
        return file;
    }

    private String created;
    private String uploaded;
    private String file;
    private String type, job;
    public static PDF Builder(){
        return new PDF();
    }
    public PDF Override(String file, String link, String created, String uploaded, String type, String job){
        this.file = file;
        this.link = link;
        this.job = job;
        this.type = type;
        this.created = created;
        this.uploaded = uploaded;
        return this;
    }

    public String getJob() {
        return job;
    }

    public String getType() {
        return type;
    }
}
