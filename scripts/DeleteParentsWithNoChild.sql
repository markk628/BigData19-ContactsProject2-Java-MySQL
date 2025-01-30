WITH relationship_to_delete AS (
	SELECT r.relationship_id
  	  FROM contact c
 	 RIGHT JOIN relationship r
 		ON c.relationship_id = r.relationship_id
 	 WHERE c.contact_id IS NULL
)
DELETE r 
  FROM relationship r
 INNER JOIN relationship_to_delete rd
 	ON r.relationship_id = rd.relationship_id
;

WITH country_to_delete AS (
	SELECT c.country_id
	  FROM state s
	 RIGHT JOIN country c
	 	ON s.country_id = c.country_id
	 WHERE s.state_id IS NULL 
)
DELETE c
  FROM country c
 INNER JOIN country_to_delete cd
 	ON c.country_id = cd.country_id
;

WITH state_to_delete AS (
	SELECT s.state_id
	  FROM city c
	 RIGHT JOIN state s
	   ON s.state_id = c.state_id
	 WHERE c.city_id IS NULL
)
DELETE s
  FROM state s
 INNER JOIN state_to_delete sd
 	ON s.state_id = sd.state_id
;
 	
-- delete city with no child
WITH city_to_delete AS (
	SELECT c.city_id 
  	  FROM address a
	 RIGHT JOIN city c
 		ON a.city_id = c.city_id
	 WHERE a.address_id IS NULL 
)
DELETE c
  FROM city c
 INNER JOIN city_to_delete cd
 	ON c.city_id = cd.city_id
;
 	
 -- delete address with no child
WITH address_to_delete AS (
	SELECT a.address_id
  	  FROM contact c
 	 RIGHT JOIN address a
  		ON c.address_id = a.address_id 
	 WHERE c.contact_id IS NULL
)
DELETE a
  FROM address a
 INNER JOIN address_to_delete ad
 	ON a.address_id = ad.address_id
;