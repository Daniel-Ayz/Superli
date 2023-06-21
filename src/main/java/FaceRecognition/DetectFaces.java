package FaceRecognition;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetectFaces {

    public static void detectFaces() throws IOException {
        // TODO(developer): Replace these variables before running the sample.
        String filePath = "C:\\Users\\mafia\\OneDrive - post.bgu.ac.il\\שולחן העבודה\\תואר\\סמסטר ד\\ניתוץ\\Superli\\AttendanceProject\\images\\c1.png";
        detectFaces(filePath);
    }

    // Detects faces in the specified local image.
    public static List<List<Likelihood[]>> detectFaces(String filePath) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.FACE_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
        requests.add(request);

        List<List<Likelihood[]>> clientsLikelihoods = new ArrayList<>();
        // Initialize client that will be used to send requests. This client only needs to be created
        // once, and can be reused for multiple requests. After completing all of your requests, call
        // the "close" method on the client to safely clean up any remaining background resources.
        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                List<Likelihood[]>  likelihoods = new ArrayList<>();
                if (res.hasError()) {
                    System.out.format("Error: %s%n", res.getError().getMessage());
                    throw new IOException("Error in detect faces: " + res.getError().getMessage());
                }

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                for (FaceAnnotation annotation : res.getFaceAnnotationsList()) {
                    likelihoods.add(new Likelihood[]{annotation.getAngerLikelihood(), annotation.getJoyLikelihood(), annotation.getSurpriseLikelihood()});
                    System.out.format(
                            "anger: %s%njoy: %s%nsurprise: %s%nposition: %s",
                            annotation.getAngerLikelihood(),
                            annotation.getJoyLikelihood(),
                            annotation.getSurpriseLikelihood(),
                            annotation.getBoundingPoly());
                }
            }
        }
        return clientsLikelihoods;
    }
}