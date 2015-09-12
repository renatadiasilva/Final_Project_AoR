package pt.uc.dei.aor.pf;

import java.text.Normalizer;

public class SearchPattern {

	// prepare pattern for searching in DB: 
	// word in upper case with no whitespaces, symbols, and accent letters
	public static String preparePattern(String searchWord) {

		// removes all non-word characters of the word
		String pattern = searchWord.replaceAll("\\W", "");
		// removes all whitespaces of the word
		//		String pattern = searchWord.replaceAll("\\s", "");
		System.out.println(pattern);

		// adds % because of database search
		pattern = "%"+pattern.toUpperCase()+"%";
		System.out.println(pattern);

		// separates all of the accent marks from the characters
		pattern = Normalizer.normalize(pattern, Normalizer.Form.NFD);
		System.out.println(pattern);

		// compares each character against being a letter and 
		// throw out the ones that aren't.
		pattern = pattern.replaceAll("\\p{M}", "");
//		string = string.replaceAll("[^\\p{ASCII}]", "");
		System.out.println(pattern);

		return pattern;
	}

}
