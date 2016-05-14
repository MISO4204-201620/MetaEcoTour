import aws.S3Helper;
import email.STSendGridManager;
import play.GlobalSettings;
import play.Logger;

import java.io.File;

/**
 * Created by manuel on 28/02/16.
 */
public class Global extends GlobalSettings {

    @Override
    public void onStart(play.Application application) {

        System.out.println("Starting S3");
        //Start S3
       // S3Helper.initInstance(application);
        //linea que permite cargar un arhivo en s3
        //S3Helper.uploadObject("20160130101046-m1.jpg", new File("/tmp/20160130101046.jpg"));
        //Start SendGrid
        try {
            STSendGridManager.initInstance(application);

        } catch (Exception e) {
            Logger.error("Ocurrio un error enviando el mensaje", e.fillInStackTrace());
        }
    }

}
