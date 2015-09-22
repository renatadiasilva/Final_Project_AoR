package pt.uc.dei.aor.pf.pdf;

import javax.annotation.ManagedBean;
import javax.inject.Inject;
import javax.inject.Named;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.uc.dei.aor.pf.reports.ReportsCDI;

import java.io.Serializable;

@Named
@ManagedBean
public class PreProcessPDFCDI implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6173527334567536154L;
	
	private static final Logger log = LoggerFactory.getLogger(PreProcessPDFCDI.class);
	
	@Inject
	ReportsCDI reports;

	public void preProcessPDF(Object document) {
		
		Document pdf = (Document) document;
		pdf.open();
		pdf.setPageSize(PageSize.A4.rotate());
		pdf.addHeader(this.reports.getTableHeader(), this.reports.getTableHeader());
		pdf.addTitle(this.reports.getTableHeader());
		
		log.info("Pre processed pdf.");

//		ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
//		String logo = servletContext.getRealPath("") + File.separator + "resources" + File.separator + "demo" + File.separator + "images" + File.separator + "prime_logo.png";
//
//		try {
//			pdf.add(Image.getInstance(logo));
//		} catch (BadElementException e) {
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
