package de.meisterfuu.animexx.Home;

public class HomeKontaktHelper {

	/*
	 * sb, geburtstag, autorneu, usernamechange, doujinshi, cosplayneu, fotografneu,
	 * autor, eventteilnahme, twitterconnect, stckvid, magdj, magwb,
	 * magfa, magbast, magab, magoe, magff, magfo, magfor, magev, magnews, magwtb, magart,
	 * magdvd, magmanga, magcd, magasia, maggame, magumf, magcos,
	 * magjfas, magfrt, magfr, magevvid, magfanvid, magshop, evmfgkd,
	 * evmfgknd, evmfgod, realk, projtxt, projwtb, projman, projvid, prjvid, prjrss,
	 * projnews, mb
	 */

	public static String getFullText(HomeKontaktObject obj) {

		String ret = "";
		if (obj.getEvent_typ().equals("mb")) {
			ret = obj.getKommentar();
		} else {
			if (obj.getItem_name() != null) {
				if(obj.getEvent_name().startsWith("Empfehlung:")) ret += obj.getVon().getUsername()+" empfiehlt:\n";
				ret += obj.getItem_name();
			} else {
				ret = obj.getEvent_name();
			}
		}
		
		return ret;
	}


	public static String getSideText(HomeKontaktObject obj) {

		if (obj.getEvent_typ().equals("mb")) {
			return "Blog";
		} else if (obj.getEvent_typ().equals("sb")) {
			return "User";
		} else if (obj.getEvent_typ().equals("cosplayneu")) {
			return "Cosplay";
		} else if (obj.getEvent_typ().equals("usernamechange")) {
			return "User";
		} else if (obj.getEvent_typ().equals("evmfgkd") || obj.getEvent_typ().equals("eventteilnahme")) {
			return "Event";
		} else {
			return obj.getEvent_name().replaceAll("Empfehlung: ", "");
		}
	}
}
