package utils.query;

import java.util.ArrayList;

import utils.tables.AddressFields;
import utils.tables.CityFields;
import utils.tables.ContactFields;
import utils.tables.CountryFields;
import utils.tables.RelationshipFields;
import utils.tables.StateFields;

/**
 * @packageName : utils.query
 * @fileNmae	: QueryBuilder.java
 * @author		: mark
 * @date		: 2025.01.30
 * @description : 필요한 query를 반환하는 클래스
 * ===========================================================
 * DATE				AUTHOR				NOTE
 * -----------------------------------------------------------
 * 2025.01.30		MARK KIM		FIRST CREATED
 */
public class QueryBuilder {
	public static String insert() {
		return "INSERT INTO " + ContactFields.tableName() + " (" + ContactFields.NAME.toString() + ", 	      "
																 + ContactFields.NUMBER.toString() + ", 	  " 
																 + ContactFields.ADDRESS.toString() + ", 	  " 
																 + ContactFields.RELATIONSHIP.toString() + ") "
		     + "VALUES (?, ?, ?, ?)												   							  ";
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
		return "INSERT INTO " + CountryFields.tableName() +" (" + CountryFields.COUNTRY.toString() + ") "
			 + "VALUES (?)																			    ";
	}
	
	public static String insertRelationship() {
		return "INSERT INTO " + RelationshipFields.tableName() +" (" + RelationshipFields.RELATIONSHIP.toString() + ") "
			 + "VALUES (?)																				  			   ";
	}
	
	public static String select(SelectOption option) {
		String lastLine;
		switch (option) {
		case ALL:
			lastLine = "ORDER BY c." + ContactFields.ID.toString();
			break;
		case NAME:
			lastLine = "WHERE c." + ContactFields.NAME.toString() + " = ?";
			break;
		default:
			lastLine = "WHERE c." + ContactFields.NUMBER.toString() + " = ?";
		}
		return "WITH full_address AS (																	            "
		     + "	SELECT a." + AddressFields.ID.toString() + ",										 	        "
		     + "		   a." + AddressFields.ADDRESS.toString() + ",									 	        "
		     + "		   a." + AddressFields.ZIPCODE.toString() + ",									 	        "
		     + "		   c." + CityFields.CITY.toString() + ",										 	        "
		     + "		   s." + StateFields.STATE.toString() + ",										 	        "
		     + "		   ct." + CountryFields.COUNTRY.toString() + "									 	        "
		     + "	  FROM " + AddressFields.tableName() + " a												        "
		     + "	 INNER JOIN " + CityFields.tableName() + " c												    "
		     + "	 	ON a." + AddressFields.CITY.toString() + " = c." + CityFields.ID.toString() + "			    "
		     + "	 INNER JOIN " + StateFields.tableName() + " s											        "
		     + "	 	ON c." + CityFields.STATE.toString() + " = s." + StateFields.ID.toString() + "			    "
		     + "	 INNER JOIN " + CountryFields.tableName() + " ct											    "
		     + "	 	ON s." + StateFields.COUNTRY.toString() + " = ct." + CountryFields.ID.toString() + "	    "
		     + ")																							        "
		     + "SELECT c." + ContactFields.ID.toString() + ",													    "
		     + "       c." + ContactFields.NAME.toString() + ",													    "
		     + "	   c." + ContactFields.NUMBER.toString() + ",												    "
		     + "	   a." + AddressFields.ADDRESS.toString() + ", 								           			"
		     + "	   a." + CityFields.CITY.toString() + ",										           		"
		     + "	   a." + StateFields.STATE.toString() + ", 										           		"
		     + "	   a." + AddressFields.ZIPCODE.toString() + ", 								          			"
		     + "	   a." + CountryFields.COUNTRY.toString() + ",							           				"
		     + "	   r." + RelationshipFields.RELATIONSHIP.toString() + "										    "
		     + "  FROM " + ContactFields.tableName() + " c													        "
		     + " INNER JOIN " + RelationshipFields.tableName() + " r											    "
		     + "    ON c." + ContactFields.RELATIONSHIP.toString() + " = r." + RelationshipFields.ID.toString() + " "
		     + " INNER JOIN full_address a																	        "
		     + "    ON c." + ContactFields.ADDRESS.toString() + " = a." + AddressFields.ID.toString() + "		    "
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
			 + "   AND a." + AddressFields.CITY.toString() + " = ?	  ";
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
			 + " 	FROM " + RelationshipFields.tableName() + " r	   	   		"
			 + " WHERE r." + RelationshipFields.RELATIONSHIP.toString() + " = ? ";
	}

	public static String update(ArrayList<ContactFields> fields) {
		String query = "UPDATE " + ContactFields.tableName() + " c SET c.";
		boolean shouldAddComma;
		int listLength = fields.size();
		
		for(int i = 0; i < listLength; i++) {
			shouldAddComma = i + 1 != listLength;
			query += (fields.get(i).toString() + " = ?" + (shouldAddComma ? "," : " "));
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
		return "WITH address_to_delete AS (																"
			 + "	SELECT a." + AddressFields.ID.toString() + "										"
			 + "   	  FROM " + ContactFields.tableName() + " c											"
			 + "  	 RIGHT JOIN " + AddressFields.tableName() + " a										"
			 + "  		ON c." + ContactFields.ADDRESS.toString() + " = a." + AddressFields.ID.toString()
			 + "	 WHERE c." + ContactFields.ID.toString() + " IS NULL								"
			 + ")																						"
			 + "DELETE a																				"
			 + "  FROM " + AddressFields.tableName() + " a												"
			 + " INNER JOIN address_to_delete ad														"
			 + " 	ON a." + AddressFields.ID.toString() + " = ad." + AddressFields.ID.toString() + "   ";
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
		return "WITH country_to_delete AS (															  "
			 + "	SELECT c." + CountryFields.ID.toString() + "									  "
			 + "	  FROM " + StateFields.tableName() + " s										  "
			 + "	 RIGHT JOIN " + CountryFields.tableName() + " c								  	  "
			 + "	 	ON s." + StateFields.COUNTRY.toString() + " = c." + CountryFields.ID.toString()
			 + "	 WHERE s." + StateFields.ID.toString() + " IS NULL								  "
			 + ")																					  "
			 + "DELETE c																			  "
			 + "  FROM " + CountryFields.tableName() + " c										  	  "
			 + " INNER JOIN country_to_delete cd													  "
			 + " 	ON c." + CountryFields.ID.toString() + " = cd." + CountryFields.ID.toString() + " ";
	}
	
	public static String delete() {
		return "DELETE c"
			 + "  FROM " + ContactFields.tableName() + " c		"
			 + " WHERE c." + ContactFields.ID.toString() + " = ?";
	}
}
