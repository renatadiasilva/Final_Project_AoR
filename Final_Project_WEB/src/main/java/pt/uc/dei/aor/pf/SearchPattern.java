package pt.uc.dei.aor.pf;

import java.text.Normalizer;

public class SearchPattern {

	// prepare pattern for searching in DB: 
	// word in upper case with no whitespaces, symbols, and accent letters
	public static String preparePattern(String searchWord) {

		// removes all whitespaces of the word
		String pattern = searchWord.replaceAll("\\s", "");

		// adds % because of database search
		pattern = "%"+pattern.toUpperCase()+"%";

		// separates all of the accent marks from the characters
		pattern = Normalizer.normalize(pattern, Normalizer.Form.NFD);

		// compares each character against being a letter and 
		// throw out the ones that aren't.
		pattern = pattern.replaceAll("\\p{M}", "");

		return pattern;
	}

}
