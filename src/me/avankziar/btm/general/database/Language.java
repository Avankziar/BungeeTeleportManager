package me.avankziar.btm.general.database;

import java.util.LinkedHashMap;

public class Language
{
	//See https://en.wikipedia.org/wiki/List_of_ISO_639-2_codes, and if a * appears, than use this.
	public enum ISO639_2B
	{
		AAR, ABK, ACE, ACH, ADA, ADY, AFA, AFH, AFR, AIN, AKA, AKK, ALB, ALE, ALG, ALT, AMH, ANG, ANP, APA,
		ARA, ARC, ARG, ARM, ARN, ARP, ART, ARW, ASM, AST, ATH, AUS, AVA, AVE, AWA, AYM, AZE, BAD, BAI, BAK,
		BAL, BAM, BAN, BAQ, BAS, BAT, BEJ, BEL, BEM, BEN, BER, BHO, BIH, BIK, BIN, BIS, BLA, BNT, BOS, BRA,
		BRE, BTK, BUA, BUG, BUL, BUR, BYN, CAD, CAI, CAR, CAT, CAU, CEB, CEL, CES, CHA, CHB, CHE, CHG, CHI,
		CHK, CHM, CHN, CHO, CHP, CHR, CHU, CHV, CHY, CMC, CNR, COP, COR, COS, CPE, CPF, CPP, CRE, CRH, CRP,
		CSB, CUS, CYM, CZE, DAK, DAN, DAR, DAY, DEL, DEN, DGR, DIN, DIV, DOL, DRA, DSB, DUA, DUM, DUT, DYU,
		DZO, EFI, EGY, EKA, ELX, ENG, ENM, EPO, EST, EWE, EWO, FAN, FAO, FAT, FIJ, FIL, FIN, FIU, FON, FRE,
		FRM, FRO, FRR, FRS, FRY, FUL, FUR, GAA, GAY, GBA, GEM, GEO, GER, GEZ, GIL, GLA, GLE, GLG, GLV, GMH,
		GOH, GON, GOR, GRB, GRC, GRE, GRN, GSW, GUJ, GWI, HAI, HAT, HAU, HAW, HEB, HER, HIL, HIM, HIN, HIT,
		HMN, HMO, HRV, HSB, HUN, HUP, IBA, IBO, ICE, IDO, III, IJO, IKU, ILE, ILO, INO, INC, IND, INE, INH,
		IPK, IRA, IRO, ITA, JAV, JBO, JPN, JPR, JRB, KAA, KAB, KAC, KAL, KAM, KAN, KAS, KAU, KAW, KAZ, KBD,
		KHA, KHI, KHM, KHO, KIK, KIN, KIR, KMB, KOK, KOM, KON, KOR, KOS, KPE, KRC, KRL, KRO, KRU, KUA, KUM,
		KUR, KUT, LAD, LAH, LAM, LAO, LAT, LAV, LEZ, LIM, LIN, LIT, LIL, LOZ, LTZ, LUA, LUB, LUG, LUI, LUN,
		LUO, LUS, MAC, MAD, MAG, MAH, MAI, MAK, MAL, MAN, MAO, MAP, MAR, MAS, MAY, MDF, MDR, MEN, MGA, MIC,
		MIN, MIS, MKH, MLG, MIT, MNC, MNI, MNO, MOH, MON, MOS, MUL, MUN, MUS, MWL, MWR, MYN, MYV, NAH, NAI,
		NAP, NAU, NAV, NBI, NDE, NDO, NDS, NEP, NEW, NIA, NIC, NIU, NNO, NOB, NOG, NON, NOR, NQO, NSO, NUB,
		NWC, NYA, NYM, NYN, NYO, NZI, OCI, OJI, ORI, ORM, OSA, OSS, OTA, OTO, PAA, PAG, PAL, PAM, PAN, PAP,
		PAU, PEO, PER, PHI, PHN, PLI, POL, PON, POR, PRA, PRO, PUS, QUE, RAJ, RAP, RAR, ROA, ROH, RUM, RUN,
		RUP, RUS, SAD, SAG, SAH, SAI, SAL, SAM, SAN, SAS, SAT, SCN, SCO, SEL, SEM, SGA, SGN, SHN, SID, SIN,
		SIO, SIT, SLA, SLO, SLV, SMA, SME, SMI, SMJ, SMN, SMO, SMS, SNA, SND, SNK, SOG, SOM, SON, SOT, SPA,
		SRD, SRN, SRP, SRR, SAA, SSW, SUK, SUN, SUS, SUX, SWA, SWE, SYC, SYR, TAH, TAI, TAM, TAT, TEL, TEM,
		TER, TET, TGK, TGL, THA, TIB, TIG, TIR, TIV, TKI, TLH, TLI, TMH, TOG, TON, TPI, TSI, TSN, TSO, TUK,
		TUM, TUP, TUR, TUT, TVL, TWL, TYV, UDM, UGA, UIG, UKR, UMB, UND, URD, UZB, VAI, VEN, VIE, VOL, VOT, 
		WAK, WAL, WAR, WAS, WEL, WEN, WLN, WOL, XAL, XHO, YAO, YAP, YID, YOR, YPK, ZAP, UBL, ZEN, ZGH, ZHA,
		ZHO, ZND, ZUL, ZUN, ZZA;
	}
	
	public LinkedHashMap<ISO639_2B, Object[]> languageValues = new LinkedHashMap<>();
	//Mit der GER, wird dann "&cDer Spieler ist nicht online" herausgegeben.	
	
	public Language(ISO639_2B[] languages, Object[] values)
	{
		if(languages.length == values.length)
		{
			for(int i = 0; i < languages.length; i++)
			{
				if(languages[i] != null && values[i] != null)
				{
					languageValues.put(languages[i], new Object[] {values[i]});
				}				
			}
		} else if(values.length % languages.length == 0) //List
		{
			int multiply = values.length / languages.length;
			for(int i = 0; i < languages.length; i++)
			{
				Object[] valuesArray = new String[multiply];
				for(int j = 0; j < multiply; j++)
				{
					valuesArray[j] = values[i*multiply+j];
				}
				languageValues.put(languages[i], valuesArray);
			}
		}
	}

}
