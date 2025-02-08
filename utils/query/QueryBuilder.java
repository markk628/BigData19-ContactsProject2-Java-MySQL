package utils.query;

import java.util.ArrayList;

import utils.tables.AddressFields;
import utils.tables.CityFields;
import utils.tables.CompanyFields;
import utils.tables.ContactFields;
import utils.tables.CountryFields;
import utils.tables.JobFields;
import utils.tables.ProfessionFields;
import utils.tables.RelationshipFields;
import utils.tables.StateFields;

/**
 * @packageName : utils.query
 * @fileNmae	: QueryBuilder.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : Returns the necessary query
 * 				  필요한 query를 반환하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class QueryBuilder {
	public static String insert(boolean withJob) {
		String lastLine = ") VALUES (?, ?, ?, ?, ?)";
		if (withJob) {
			lastLine = ", " + ContactFields.JOB.toString() + ") VALUES (?, ?, ?, ?, ?, ?)";
		}
		return "INSERT INTO " + ContactFields.tableName() + " (" + ContactFields.NAME.toString() + ", 	      "
																 + ContactFields.NUMBER.toString() + ", 	  " 
																 + ContactFields.ADDRESS.toString() + ", 	  " 
																 + ContactFields.RELATIONSHIP.toString() + ", "
																 + ContactFields.NATIONALITY.toString()
		+ lastLine;
	}
	
	public static String insertAddress() {
		return "INSERT INTO " + AddressFields.tableName() + " ("+ AddressFields.ADDRESS.toString() + ", "
																+ AddressFields.CITY.toString() + ",    " 
																+ AddressFields.ZIPCODE.toString() + ") " 
			 + "VALUES (?,?,?)																			";
	}
	
	public static String insertCity() {
		return "INSERT INTO " + CityFields.tableName() + " (" + CityFields.CITY.toString() + ",  " 
															  + CityFields.STATE.toString() + ") " 
			 + "VALUES (?,?)																	 ";
	}
	
	public static String insertState() {
		return "INSERT INTO " + StateFields.tableName() + " (" + StateFields.STATE.toString() + ",   " 
															   + StateFields.COUNTRY.toString() + ") "
			 + "VALUES (?,?)																		 ";
	}
	
	public static String insertCountry() {
		return "INSERT INTO " + CountryFields.tableName() + " (" + CountryFields.COUNTRY.toString() + ") "
			 + "VALUES (?)																			     ";
	}
	
	public static String insertRelationship() {
		return "INSERT INTO " + RelationshipFields.tableName() + " (" + RelationshipFields.RELATIONSHIP.toString() + ") "
			 + "VALUES (?)																				  			    ";
	}
	
	public static String insertCompany() {
		return "INSERT INTO " + CompanyFields.tableName() + " (" + CompanyFields.COMPANY.toString() + ") "
			 + "VALUES (?)																				 ";	
	}
	
	public static String insertProfession() {
		return "INSERT INTO " + ProfessionFields.tableName() + " (" + ProfessionFields.PROFESSION.toString() + ") "
			 + "VALUES (?)																				 		  ";	
	}
	
	public static String insertJob() {
		return "INSERT INTO " + JobFields.tableName() + " (" + JobFields.PROFESSION.toString() + ", "
															 + JobFields.COMPANY.toString() + ", 	"
															 + JobFields.ADDRESS.toString() + ")    "
             + "VALUES (?, ?, ?)																	";
	}
	
	public static String select(SelectOption option) {
		String lastLine;
		switch (option) {
		case ALL:
			lastLine = "ORDER BY c." + ContactFields.ID.toString();
			break;
		case NAME:
			lastLine = "WHERE c." + ContactFields.NAME.toString() + " LIKE ?";
			break;
		default:
			lastLine = "WHERE c." + ContactFields.NUMBER.toString() + " = ?";
		}
		return "WITH full_address AS (																				 "
			 + "	SELECT a." + AddressFields.ID.toString() + ", 													 "
			 + "		   a." + AddressFields.ADDRESS.toString() + ", 												 "
			 + "		   c." + CityFields.CITY.toString() + ", 													 "
			 + "		   s." + StateFields.STATE.toString() + ", 													 "
			 + "		   a." + AddressFields.ZIPCODE.toString() + ",												 "
			 + "		   ct." + CountryFields.COUNTRY.toString() + " 												 "
			 + "	  FROM " + AddressFields.tableName() + " a 														 "
			 + "	 INNER JOIN " + CityFields.tableName() + " c 													 "
			 + "	 	ON a." + AddressFields.CITY.toString() + " = c." + CityFields.ID.toString() + " 			 "
			 + "	 INNER JOIN " + StateFields.tableName() + " s 													 "
			 + "	 	ON c." + CityFields.STATE.toString() + " = s." + StateFields.ID.toString() + " 				 "
			 + "	 INNER JOIN " + CountryFields.tableName() + " ct 												 "
			 + "	 	ON s." + StateFields.COUNTRY.toString() + " = ct." + CountryFields.ID.toString() + " 		 "
			 + "),																									 "
			 + "nationality AS (																					 "
			 + "	SELECT c." + ContactFields.ID.toString() + ", 													 "
			 + "		   ct." + CountryFields.COUNTRY.toString() + " 												 "
			 + "	  FROM " + ContactFields.tableName() + " c 														 "
			 + "	 INNER JOIN " + CountryFields.tableName() + " ct 												 "
			 + "	 	ON c." + ContactFields.NATIONALITY.toString() + " = ct." + CountryFields.ID.toString() + "	 "
			 + "),																									 "
			 + "jobs AS (																							 "
			 + "	SELECT j." + JobFields.ID.toString() + ", 														 "
			 + "		   p." + ProfessionFields.PROFESSION.toString() + ", 										 "
			 + "		   c." + CompanyFields.COMPANY.toString() + ", 												 "
			 + "		   a." + AddressFields.ADDRESS.toString() + ", 												 "
			 + "		   a." + CityFields.CITY.toString() + ", 													 "
			 + "		   a." + StateFields.STATE.toString() + ", 													 "
			 + "		   a." + AddressFields.ZIPCODE.toString() + ", 												 "
			 + "		   a." + CountryFields.COUNTRY.toString() + " 												 "
			 + "	  FROM " + JobFields.tableName() + " j 															 "
			 + "	 INNER JOIN " + ProfessionFields.tableName() + " p 												 "
			 + "	 	ON j." + JobFields.PROFESSION.toString() + " = p." + ProfessionFields.ID.toString() + " 	 "
			 + "	 INNER JOIN " + CompanyFields.tableName() + " c 												 "
			 + "	 	ON j." + JobFields.COMPANY.toString() + " = c." + CompanyFields.ID.toString() + " 			 "
			 + "	  LEFT JOIN full_address a 																		 "
			 + "	 	ON j." + JobFields.ADDRESS.toString() + " = a." + AddressFields.ID.toString() + " 			 "
			 + ")																									 "
			 + "SELECT c." + ContactFields.ID.toString() + ", 													     "
			 + "       c." + ContactFields.NAME.toString() + ", 													 "
			 + "	   c." + ContactFields.NUMBER.toString() + ", 													 "
			 + "	   a." + AddressFields.ADDRESS.toString() + ", 													 "
			 + "	   a." + CityFields.CITY.toString() + ", 														 "
			 + "	   a." + StateFields.STATE.toString() + ", 														 "
			 + "	   a." + AddressFields.ZIPCODE.toString() + ", 													 "
			 + "	   a." + CountryFields.COUNTRY.toString() + ", 													 "
			 + "	   r." + RelationshipFields.RELATIONSHIP.toString() + ", 										 "
			 + "	   n." + CountryFields.COUNTRY.toString() + " AS " + ContactFields.nationality() + ",			 "
			 + "	   j." + ProfessionFields.PROFESSION.toString() + ", 											 "
			 + "	   j." + CompanyFields.COMPANY.toString() + ", 													 "
			 + "	   j." + AddressFields.ADDRESS.toString() + " AS " + AddressFields.ADDRESS.toStringForJob() + ", "
			 + "	   j." + CityFields.CITY.toString() + " AS " + CityFields.CITY.toStringForJob() + ", 			 "
			 + "	   j." + StateFields.STATE.toString() + " AS " + StateFields.STATE.toStringForJob() + ", 		 "
			 + "	   j." + AddressFields.ZIPCODE.toString() + " AS " + AddressFields.ZIPCODE.toStringForJob() + ", "
			 + "	   j." + CountryFields.COUNTRY.toString() + " AS " + CountryFields.COUNTRY.toStringForJob() + "  "
			 + "  FROM " + ContactFields.tableName() + " c 															 "
			 + " INNER JOIN " + RelationshipFields.tableName() +" r 												 "
			 + " 	ON c." + ContactFields.RELATIONSHIP.toString() +" = r." + RelationshipFields.ID.toString()  + "  "
			 + "  LEFT JOIN full_address a 																			 "
			 + " 	ON c." + ContactFields.ADDRESS.toString() + " = a." + AddressFields.ID.toString() + " 			 "
			 + " INNER JOIN nationality n 																			 "
			 + " 	ON c." + ContactFields.ID.toString() + " = n." + ContactFields.ID.toString() + " 				 "
			 + "  LEFT JOIN jobs j 																					 "
			 + " 	ON c." + ContactFields.JOB.toString() + " = j." + JobFields.ID.toString() + " 					 "
			 + lastLine;
	}
	
	public static String selectFromContacs() {
		return "SELECT c." + ContactFields.ID.toString() + "		 "
			 + "  FROM " + ContactFields.tableName() + " c	    	 "
			 + " WHERE c." + ContactFields.NUMBER.toString() + " = ? ";
	}
	
	public static String getLastInsertedAddressID() {
		return "SELECT a." + AddressFields.ID.toString() + " 	   	 "
			 + "  FROM " + AddressFields.tableName() + " a	  		 "
			 + " ORDER BY a." + AddressFields.ID.toString() + " DESC "
			 + " LIMIT 1			  	   							 ";
	}
	
	public static String getAddressIfAddressInAddressTable() {
		return "SELECT a." + AddressFields.ID.toString() + "		  "
			 + " 	FROM " + AddressFields.tableName() + " a	  	  "
			 + " WHERE a." + AddressFields.ADDRESS.toString() + " = ? "
			 + "   AND a." + AddressFields.CITY.toString() + " = ?	  "
			 + "   AND a." + AddressFields.ZIPCODE.toString() + " = ? ";
	}
	
	public static String getLastInsertedCityID() {
		return "SELECT c." + CityFields.ID.toString() + " 		  "
			 + "  FROM " + CityFields.tableName() + " c	 	 	  "
			 + " ORDER BY c." + CityFields.ID.toString() + " DESC "
		     + " LIMIT 1			  	  						  ";
	}
	
	public static String getCityIfCityInCityTable() {
		return "SELECT " + CityFields.ID.toString() + "		  	 "
			 + "  FROM " + CityFields.tableName() + " c	    	 "
			 + " WHERE c." + CityFields.CITY.toString() + " = ?  "
			 + "   AND c." + CityFields.STATE.toString() + " = ? ";
	}
	
	public static String getLastInsertedStateID() {
		return "SELECT s." + StateFields.ID.toString() + " 	   	   "
			 + "  FROM " + StateFields.tableName() + " s	  	   "
			 + " ORDER BY s." + StateFields.ID.toString() + " DESC "
			 + " LIMIT 1			  	   						   ";
	}
	
	public static String getStateIfStateInStateTable() {
		return "SELECT " + StateFields.ID.toString() + "		    "
			 + "  FROM " + StateFields.tableName() + " s	        "
			 + " WHERE s." + StateFields.STATE.toString() + " = ?   "
			 + "   AND s." + StateFields.COUNTRY.toString() + " = ? ";
	}
	
	public static String getLastInsertedCountryID() {
		return "SELECT c." + CountryFields.ID.toString() + " 		 "
			 + "  FROM " + CountryFields.tableName() + " c	    	 "
			 + " ORDER BY c." + CountryFields.ID.toString() + " DESC "
			 + " LIMIT 1			  		 						 ";
	}
	
	public static String getCountryIfCountryInCountryTable() {
		return "SELECT " + CountryFields.ID.toString() + "            "
			 + "  FROM " + CountryFields.tableName() + " c	     	  "
			 + " WHERE c." + CountryFields.COUNTRY.toString() + " = ? ";
	}
	
	public static String getLastInsertedRelationshipID() {
		return "SELECT r." + RelationshipFields.ID.toString() + " 		  "
			 + "  FROM " + RelationshipFields.tableName() + " r	  	  	  "
			 + " ORDER BY r." + RelationshipFields.ID.toString() + " DESC "
			 + " LIMIT 1			  		 	  						  ";
	}
	
	public static String getRelationshipIfRelationshipInRelationshipTable() {
		return "SELECT " + RelationshipFields.ID.toString() + "    			    "
			 + "  FROM " + RelationshipFields.tableName() + " r	   	   			"
			 + " WHERE r." + RelationshipFields.RELATIONSHIP.toString() + " = ? ";
	}
	
	public static String getLastInsertedCompanyID() {
		return "SELECT c." + CompanyFields.ID.toString() + " 		 "
			 + "  FROM " + CompanyFields.tableName() + " c	  	  	 "
			 + " ORDER BY c." + CompanyFields.ID.toString() + " DESC "
			 + " LIMIT 1			  		 	  					 ";
	}
	
	public static String getCompanyIfCompanyInCompanyTable() {
		return "SELECT " + CompanyFields.ID.toString() + "    		  "
			 + "  FROM " + CompanyFields.tableName() + " c	   	   	  "
			 + " WHERE c." + CompanyFields.COMPANY.toString() + " = ? ";
	}
	
	public static String getLastInsertedProfessionID() {
		return "SELECT p." + ProfessionFields.ID.toString() + " 		"
			 + "  FROM " + ProfessionFields.tableName() + " p	  	  	"
			 + " ORDER BY p." + ProfessionFields.ID.toString() + " DESC "
			 + " LIMIT 1			  		 	  					 	";
	}
	
	public static String getProfessionIfProfessionInProfessionTable() {
		return "SELECT " + ProfessionFields.ID.toString() + "    		  	"
			 + "  FROM " + ProfessionFields.tableName() + " p	   	   	  	"
			 + " WHERE p." + ProfessionFields.PROFESSION.toString() + " = ? ";
	}
	
	public static String getLastInsertedJobID() {
		return "SELECT j." + JobFields.ID.toString() + " 		 "
			 + "  FROM " + JobFields.tableName() + " j	  	  	 "
			 + " ORDER BY j." + JobFields.ID.toString() + " DESC "
			 + " LIMIT 1			  		 	  				 ";
	}
	
	public static String getJobIfJobInJobTable() {
		return "SELECT " + JobFields.ID.toString() + "    		  	 "
			 + "  FROM " + JobFields.tableName() + " p	   	   	  	 "
			 + " WHERE p." + JobFields.PROFESSION.toString() + " = ? "
			 + "   AND p." + JobFields.COMPANY.toString() + " = ? 	 "
			 + "   AND p." + JobFields.ADDRESS.toString() + " = ? 	 ";
	}
	
	public static String update(ArrayList<ContactFields> fields) {
		String query = "UPDATE " + ContactFields.tableName() + " c SET ";
		boolean shouldAddComma;
		int listLength = fields.size();
		
		for(int i = 0; i < listLength; i++) {
			shouldAddComma = i + 1 != listLength;
			query += "c." + (fields.get(i).toString() + " = ?" + (shouldAddComma ? "," : " "));
		}
		return query + "WHERE c." + ContactFields.ID.toString() + " = ?";
	}
	
	public static String deleteRelationshipWithNoChild() {
		return "WITH relationship_to_delete AS (		    													  "
			 + "	SELECT r." + RelationshipFields.ID.toString() + "											  "
			 + "  	  FROM " + ContactFields.tableName() + " c												  	  "
			 + " 	 RIGHT JOIN " + RelationshipFields.tableName() + " r										  "
			 + " 		ON c." + ContactFields.RELATIONSHIP.toString() + " = r." + RelationshipFields.ID.toString()
			 + " 	 WHERE c."+ ContactFields.ID.toString() +" IS NULL											  "
			 + ")																								  "
			 + "DELETE r																						  "
			 + "  FROM " + RelationshipFields.tableName() + " r												  	  "
			 + " INNER JOIN relationship_to_delete rd															  "
			 + " 	ON r." + RelationshipFields.ID.toString() + " = rd." + RelationshipFields.ID.toString() + "	  ";
	}
	
	public static String deleteAddressWithNoChild() {
		return "WITH contact_address AS ("
			 + "	SELECT a." + AddressFields.ID.toString() + "											  "
			 + "  	  FROM " + ContactFields.tableName() + " c												  "
			 + " 	 RIGHT JOIN " + AddressFields.tableName() + " a											  "
			 + "  		ON c." + ContactFields.ADDRESS.toString() + " = a." + AddressFields.ID.toString() + " "
			 + " 	 WHERE c." + ContactFields.ID.toString() + " IS NULL									  "
			 + "),																							  "
			 + "contact_job_address AS (																	  "
			 + "	SELECT ca." + AddressFields.ID.toString() + "											  "
			 + "  	  FROM " + JobFields.tableName() + " j													  "
			 + " 	 RIGHT JOIN contact_address ca															  "
			 + " 		ON j." + JobFields.ADDRESS.toString() + " = ca." + AddressFields.ID.toString() + "	  "
			 + "  	  LEFT JOIN " + ContactFields.tableName() + " c											  "
			 + " 		ON c." + ContactFields.JOB.toString() + " = j." + JobFields.ID.toString() + "		  "
			 + " 	 WHERE c." + ContactFields.ID.toString() + " IS NULL									  "
			 + ")																							  "
			 + "DELETE a																					  "
			 + "  FROM " + AddressFields.tableName() + " a													  "
			 + " INNER JOIN contact_job_address c															  "
			 + " 	ON a." + AddressFields.ID.toString() + " = c." + AddressFields.ID.toString() + "		  ";
	}
	
	public static String deleteCityWithNoChild() {
		return "WITH city_to_delete AS (														  "
			 + "	SELECT c." + CityFields.ID.toString() + "									  "
			 + "  	  FROM " + AddressFields.tableName() + " a								  	  "
			 + "	 RIGHT JOIN " + CityFields.tableName() + " c								  "
			 + " 		ON a." + AddressFields.CITY.toString() + " = c." + CityFields.ID.toString()
			 + "	 WHERE a." + AddressFields.ID.toString() + " IS NULL						  "
			 + ")																				  "
			 + "DELETE c																		  "
			 + "  FROM " + CityFields.tableName() + " c										 	  "
			 + " INNER JOIN city_to_delete cd													  "
			 + " 	ON c." + CityFields.ID.toString() + " = cd." + CityFields.ID.toString() + "   ";
	}
	
	public static String deleteStateWithNoChild() {
		return "WITH state_to_delete AS (														 "
			 + "	SELECT s." + StateFields.ID.toString() + "									 "
			 + "	  FROM " + CityFields.tableName() + " c									 	 "
			 + "	 RIGHT JOIN " + StateFields.tableName() + " s							 	 "
			 + "	    ON s." + StateFields.ID.toString() + " = c." + CityFields.STATE.toString()
			 + "	 WHERE c." + CityFields.ID.toString() + " IS NULL							 "
			 + ")																				 "
			 + "DELETE s																		 "
			 + "  FROM " + StateFields.tableName() + " s										 "
			 + " INNER JOIN state_to_delete sd													 "
			 + " 	ON s." + StateFields.ID.toString() + " = sd." + StateFields.ID.toString() + "";
	}
	
	public static String deleteCountryWithNoChild() {
		return "WITH country_to_delete AS (															  			   "
			 + "	SELECT c." + CountryFields.ID.toString() + "									  			   "
			 + "	  FROM " + StateFields.tableName() + " s										  			   "
			 + "	 RIGHT JOIN " + CountryFields.tableName() + " c								  	  			   "
			 + "	 	ON s." + StateFields.COUNTRY.toString() + " = c." + CountryFields.ID.toString() + "		   "
			 + "	  LEFT JOIN	" + ContactFields.tableName() + " ct								  			   "
			 + "		ON c." + CountryFields.ID.toString() + " = ct." + ContactFields.NATIONALITY.toString() + " "
			 + "	 WHERE s." + StateFields.ID.toString() + " IS NULL								  			   "
			 + "	   AND ct." + ContactFields.ID.toString() + " IS NULL										   "
			 + ")																					  			   "
			 + "DELETE c																			  			   "
			 + "  FROM " + CountryFields.tableName() + " c										  	  			   "
			 + " INNER JOIN country_to_delete cd													  			   "
			 + " 	ON c." + CountryFields.ID.toString() + " = cd." + CountryFields.ID.toString() + " 			   ";
	}
	
	public static String deleteJobWithNoChild() {
		return "WITH job_to_delete AS (																  "
			 + "	SELECT j." + JobFields.ID.toString() + " 										  "
			 + "  	  FROM " + ContactFields.tableName() + " c 										  "
			 + " 	 RIGHT JOIN " + JobFields.tableName() + " j 									  "
			 + " 		ON c." + ContactFields.JOB.toString() + " = j." + JobFields.ID.toString() + " "
			 + " 	 WHERE c." + ContactFields.ID.toString() + " IS NULL 							  "
			 + ") 																					  "
			 + "DELETE j 																			  "
			 + "  FROM " + JobFields.tableName() + " j 												  "
			 + " INNER JOIN job_to_delete jd 														  "
			 + " 	ON j." + JobFields.ID.toString() + " = jd." + JobFields.ID.toString() + " 		  ";
	}
	
	public static String deleteProfessionWithNoChild() {
		return "WITH profession_to_delete AS (																	"
			 + "	SELECT p." + ProfessionFields.ID.toString() + " 											"
			 + "  	  FROM " + JobFields.tableName() + " j 														"
			 + " 	 RIGHT JOIN " + ProfessionFields.tableName() + " p 											"
			 + " 		ON j." + JobFields.PROFESSION.toString() + " = p." + ProfessionFields.ID.toString() + " "
			 + " 	 WHERE j." + JobFields.ID.toString() + " IS NULL											"
			 + ")																								"
			 + "DELETE p																						"
			 + "  FROM " + ProfessionFields.tableName() + " p													"
			 + " INNER JOIN profession_to_delete pd																"
			 + " 	ON p." + ProfessionFields.ID.toString() + " = pd." + ProfessionFields.ID.toString() + "		";
	}
	
	public static String deleteCompanyWithNoChild() {
		return "WITH company_to_delete AS (																  "
			 + "	SELECT c." + CompanyFields.ID.toString() + "										  "
			 + "  	  FROM " + JobFields.tableName() + " j 												  "
			 + " 	 RIGHT JOIN " + CompanyFields.tableName() + " c 									  "
			 + " 		ON j." + JobFields.COMPANY.toString() + " = c." + CompanyFields.ID.toString() + " "
			 + " 	 WHERE j." + JobFields.ID.toString() + " IS NULL									  "
			 + ")																						  "
			 + "DELETE c																				  "
			 + "  FROM " + CompanyFields.tableName() + " c												  "
			 + " INNER JOIN company_to_delete cd														  "
			 + " 	ON c." + CompanyFields.ID.toString() + " = cd." + CompanyFields.ID.toString() + "	  ";
	}
	
	public static String delete() {
		return "DELETE c"
			 + "  FROM " + ContactFields.tableName() + " c		"
			 + " WHERE c." + ContactFields.ID.toString() + " = ?";
	}
}
