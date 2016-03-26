package email;

/**
 * Created by manuel on 25/03/16.
 */

import com.sendgrid.*;
import play.Application;
import play.Logger;

public class STSendGridManager {

    public static final String SENDGRID_USERNAME = "sendgrid.username";
    public static final String SENDGRID_PASSWORD = "sendgrid.password";
    private final Application application;
    private final SendGrid sendgrid;

    private static STSendGridManager instance;

    private STSendGridManager(Application application){
        this.application = application;
        String username = application.configuration().getString(SENDGRID_USERNAME);
        String password = application.configuration().getString(SENDGRID_PASSWORD);

        if(username != null && password != null){
            sendgrid = new SendGrid(username, password);
            Logger.info("Sendgrid enabled");
        }
        else{
            sendgrid = null;
            Logger.info("Sendgrid not enabled");
        }
    }

    public static STSendGridManager initInstance(Application application){
        if(instance == null){
            instance = new STSendGridManager(application);
        }
        return instance;
    }

    public static STSendGridManager getInstance(){
        return instance;
    }

    public void sendEmail(String to, String from, String subject, String text){

        if(sendgrid == null){
            Logger.info("Sending mock email");
            return;
        }

        SendGrid.Email email = new SendGrid.Email();
        email.addTo(to);
        email.setFrom(from);
        email.setSubject(subject);
        email.setText(text);

        try{
            SendGrid.Response response = sendgrid.send(email);
        }
        catch (SendGridException e) {
            Logger.error("Eroor al intentar enviar el correo",e);
        }
    }
}
