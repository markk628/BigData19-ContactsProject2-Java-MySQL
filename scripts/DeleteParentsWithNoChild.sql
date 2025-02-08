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

WITH contact_address AS (
	SELECT a.address_id
  	  FROM contact c
 	 RIGHT JOIN address a
  		ON c.address_id = a.address_id 
 	 WHERE c.contact_id IS NULL
),
contact_job_address AS (
	SELECT ca.address_id 
  	  FROM job j
	 RIGHT JOIN contact_address ca
 		ON j.work_address_id = ca.address_id
  	  LEFT JOIN contact c
 		ON c.job_id = j.job_id
	 WHERE c.contact_id IS NULL
)
DELETE a
  FROM address a
 INNER JOIN contact_job_address c
 	ON a.address_id = c.address_id
;

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

WITH country_to_delete AS (
	SELECT c.country_id 
  	  FROM state s
 	 RIGHT JOIN country c
		ON s.country_id = c.country_id
  	  LEFT JOIN contact ct
 		ON c.country_id = ct.nationality_id	
 	 WHERE s.state_id IS NULL
   	   AND ct.contact_id IS NULL
)
DELETE c
  FROM country c
 INNER JOIN country_to_delete cd
 	ON c.country_id = cd.country_id
;

WITH job_to_delete AS (
	SELECT j.job_id 
  	  FROM contact c 
 	 RIGHT JOIN job j
 		ON c.job_id = j.job_id 
 	 WHERE c.contact_id IS NULL
)
DELETE j
  FROM job j
 INNER JOIN job_to_delete jd
 	ON j.job_id = jd.job_id
;

WITH company_to_delete AS (
	SELECT c.company_id 
  	  FROM job j 
 	 RIGHT JOIN company c 
 		ON j.company_id = c.company_id 
 	 WHERE j.job_id IS NULL
)
DELETE c
  FROM company c
 INNER JOIN company_to_delete cd
 	ON c.company_id = cd.company_id
;

WITH profession_to_delete AS (
	SELECT p.profession_id 
  	  FROM job j 
 	 RIGHT JOIN profession p 
 		ON j.profession_id = p.profession_id
 	 WHERE j.job_id IS NULL
)
DELETE p
  FROM profession p
 INNER JOIN profession_to_delete pd
 	ON p.profession_id = pd.profession_id
;