package pt.uc.dei.aor.pf;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import java.io.Serializable;
import java.net.URL;
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

	private String countries;

	private String[]countriesArray;

	private ArrayList<String>countriesList;

	private boolean isInit=false;

	public CountryListCDI() {
		this.init();
	}

	public void init(){

//		if(!this.isInit){
//			this.countriesArray=new String[0];
//			
//			this.countries=new String();
//			
//			this.countries=readFile("countriesPT.txt");
//			this.countriesArray = this.countries.split("\n");
//			
//			this.countries=new String();
//
//			this.countriesList=new ArrayList<String>(Arrays.asList(this.countriesArray));
//			this.countriesArray=new String[0];
//
//			this.isInit=true;
//		}
//
//		for(String s:this.countriesList)System.out.println(s);
	}

	private String readFile(String filename){
		String content = null;
		File file = new File(filename);
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	public List<String> getCountries(String query){
		this.init();

		List<String> suggestedCountries=new ArrayList<>();

		for(String country:this.countriesList){
			if(country.toLowerCase().startsWith(query.toLowerCase())){
				suggestedCountries.add(country);
			}
		}

		return suggestedCountries;
	}

}
