package pt.uc.dei.aor.pf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Named
@SessionScoped
public class CountryListCDI implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6364983424586000332L;
	
	private static final Logger log = LoggerFactory.getLogger(CountryListCDI.class);

	private String countries;

	private String[]countriesArray;

	private ArrayList<String>countriesList;

	private boolean isInit=false;

	public CountryListCDI() {
		this.isInit=false;
		this.init();
	}

	public void init(){

		if(!this.isInit){
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
			ServletContext context = request.getServletContext();
			
			// Caminho do ficheiro do WAR
			String path = context.getRealPath("/WEB-INF/template/countriesPT.txt");
			
			this.countriesArray=new String[0];
			
			this.countries=new String();
			
			this.countries=this.readFile(path);
			
			this.countriesArray = this.countries.split("\n");
			
			this.countries=new String();

			this.countriesList=new ArrayList<String>(Arrays.asList(this.countriesArray));
			
			this.countriesArray=new String[0];

			this.isInit=true;
		}
		
	}

	private String readFile(String filename){
		String content = null;
		File file = new File(filename);		
		
		try {
			InputStreamReader reader=new InputStreamReader(new FileInputStream(filename), "UTF-8");
			
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			log.warn("Erro ao ler countriesPT.txt");
		}
		return content;
	}

	public List<String> getCountries(String query){
		
		List<String> suggestedCountries=new ArrayList<>();

		for(String country:this.countriesList){
			if(country.toLowerCase().startsWith(query.toLowerCase())){
				suggestedCountries.add(country);
			}
		}

		return suggestedCountries;
	}

}
