WITH full_address AS (
	SELECT a.address_id,
		   a.address, 
		   a.zip_code, 
		   c.city, 
		   s.state, 
		   ct.country 
	  FROM address a 
	 INNER JOIN city c 
	 	ON a.city_id = c.city_id 
	 INNER JOIN state s 
	 	ON c.state_id = s.state_id 
	 INNER JOIN country ct 
	 	ON s.country_id = ct.country_id
) 
SELECT c.contact_name, 
	   c.contact_number, 
	   CONCAT(a.address, ', ', a.city, ', ', a.state, ' ', a.zip_code, ', ', a.country) AS address, 
	   r.relationship 
  FROM contact c 
 INNER JOIN relationship r 
 	ON c.relationship_id = r.relationship_id  
 INNER JOIN full_address a 
 	ON c.address_id = a.address_id 
 ORDER BY c.contact_id
;