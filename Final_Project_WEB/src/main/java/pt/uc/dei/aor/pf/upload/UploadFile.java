package pt.uc.dei.aor.pf.upload;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;

import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequestScoped
public class UploadFile {
	
	private static final Logger log = LoggerFactory.getLogger(UploadFile.class);
	
	public static final String FOLDER_CUSTOM_LOGOS="customLogos";
	public static final String FOLDER_USER_CV="userCV";
	public static final String FOLDER_SUBMISSION_CV="submissionCV";
	public static final String FOLDER_SUBMISSION_MOTIVATION_LETTER="submissionMotivationLetter";
	public static final String FOLDER_INTERVIEW_RESULT="interviewResult";
	
	public static final String IMAGE_EXTENSION=".jpg";
	public static final String DOCUMENT_EXTENSION_PDF=".pdf";
	public static final String DOCUMENT_EXTENSION_WORD=".doc";
	
	public void uploadFile(UploadedFile file, String folder, Long id, String extension){
		Properties props = System.getProperties();
		
		try {
			// Ex.: 'contexto'/userCV/63.pdf
			file.write(props.getProperty("user.dir")+"\\"+folder+"\\"+id+extension);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Error writing "+file.getFileName()+" to "+folder+" with new name "+id+extension);
		}
	}
}
