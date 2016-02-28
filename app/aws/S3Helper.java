package aws;

import java.io.File;
import java.io.InputStream;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

import play.Application;
import play.Logger;

public class S3Helper {

    public static final String AWS_S3_BUCKET = "aws.s3.bucket";
    public static final String AWS_ACCESS_KEY = "aws.access.key";
    public static final String AWS_SECRET_KEY = "aws.secret.key";
    private final Application application;

    public static AmazonS3 amazonS3;

    public static String s3Bucket;

    private S3Helper(Application application) {
        this.application = application;
    }

    private static S3Helper instance;
    public static void initInstance(Application application){
        if(instance == null){
            instance = new S3Helper(application);
            instance.onStart();
        }
    }

    public static S3Helper getInstance(){
        return instance;
    }

    public void onStart() {
        String accessKey = application.configuration().getString(AWS_ACCESS_KEY);
        Logger.info("accessKey " + accessKey);
        String secretKey = application.configuration().getString(AWS_SECRET_KEY);
        Logger.info("secretKey " + secretKey);
        s3Bucket = application.configuration().getString(AWS_S3_BUCKET);
        Logger.info("s3Bucket " + s3Bucket);
        if ((accessKey != null) && (secretKey != null)) {
            AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
            amazonS3 = new AmazonS3Client(awsCredentials);
            Region usEast1 = Region.getRegion(Regions.US_EAST_1);//Virginia
            amazonS3.setRegion(usEast1);
            if(!bucketExists(s3Bucket)){
                Logger.info("Creating bucket");
                amazonS3.createBucket(s3Bucket);
            }
            Logger.info("Using S3 Bucket: " + s3Bucket);
        } else {
            Logger.info("No se encontraron las credenciales de aws: ");
        }
    }

    public void listBuckets(){
        for (Bucket bucket : amazonS3.listBuckets()) {
            Logger.info(" - " + bucket.getName());
        }
    }

    public boolean bucketExists(String bucketName){
        boolean exists = false;
        for (Bucket bucket : amazonS3.listBuckets()) {
            if(bucket.getName().equals(bucketName)){
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Metodo para guardar los objetos en s3
     * @param key, es el nombre que va a tomar el archivo en s3
     * @param file,
     * @return
     */
    public static boolean uploadObject(String key, File file){
        boolean uploadSuccess = true;
        checkIfAWSEnabled();

        Logger.info("Uploading a new object to S3 from a file\n");
        try{
            PutObjectRequest request = new PutObjectRequest(s3Bucket, key, file);
            //Make it publicly read accessible
            request.setCannedAcl(CannedAccessControlList.PublicRead);
            PutObjectResult res = S3Helper.amazonS3.putObject(request);

        }
        catch(Exception e){
            uploadSuccess = false;
        }
        return uploadSuccess;

    }

    public static InputStream getObject(String key){
        S3Object object = amazonS3.getObject(new GetObjectRequest(s3Bucket, key));
        Logger.info("Content-Type: "  + object.getObjectMetadata().getContentType());
        InputStream is = object.getObjectContent();
        return is;
    }

    public static void checkIfAWSEnabled(){
        if (S3Helper.amazonS3 == null) {
            Logger.error("Could not save because amazonS3 was null");
            throw new RuntimeException("Could not save");
        }
    }

    public static String getObjectUrl(String key){
        checkIfAWSEnabled();
        String bucketName = s3Bucket;
        return "https://" + bucketName + ".s3.amazonaws.com/" + key;
    }

}