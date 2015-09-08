package pt.uc.dei.aor.pf.beans;

//import java.util.ArrayList;
//import java.util.Iterator;

//import pt.uc.dei.aor.pf.entities.Utilizador;


public class UsersCount {

//	private static int num = 0;
//	// Todos os utilizadores activos sem repetição
//	private static ArrayList<Utilizador> loggedUsers= new ArrayList<Utilizador>();
//	// Todos as sessões
//	private static ArrayList<Utilizador> allSessions=new ArrayList<Utilizador>();
//
//
//	public UsersCount() {
//		num = 0;
//		loggedUsers = new ArrayList<Utilizador>();
//
//	}
//
//	public static void numLogout(Utilizador user){
//		Utilizador uTemp= new Utilizador();
//		System.out.println(user.getNome());
//		int count=0;
//		Iterator<Utilizador> iter = allSessions.iterator();
//
//		while (iter.hasNext()) {
//			Utilizador str = iter.next();
//
//
//			if (str.getMail().equals(user.getMail())){
//				count++;
//				uTemp=str;
//			}
//
//		}
//		allSessions.remove(uTemp);
//		System.out.println("A contagem é " + count);
//		
//		//vai ser log
//		if (count==1){
//			Iterator<Utilizador> iter2 = loggedUsers.iterator();
//
//			while (iter2.hasNext()) {
//				Utilizador str2 = iter2.next();
//
//				if (str2.getMail().equals(user.getMail()))
//					iter2.remove();
//				num--;
//			}
//			//vai ser log
//			System.out.println("No fim ficam "+ num + "logados");
//		}
//		
//	}
//
//	public static void increment(Utilizador user){
//		boolean islogged=false;
//
//		for(Utilizador u:loggedUsers){
//			if (u.getMail().equals(user.getMail()))
//				islogged=true;
//		}
//		System.out.println("IS ;LOGGED" + islogged);
//		Utilizador user2=new Utilizador();
//		user2.setId(user.getId());
//		user2.setNome(user.getNome());
//		user2.setMail(user.getMail());
//		user2.setPassword("");
//
//		if (!islogged){
//			num++;
//			loggedUsers.add(user2);
//			System.out.println(num + "adicionou");
//		}
//		allSessions.add(user2);
//	}
//
//	public static int getNumLogged() {
//		return num;
//	}
//	public static ArrayList<Utilizador> getLogged(){
//		return loggedUsers;
//	}
}