WITH full_address AS (
	SELECT a.address_id,
		   a.address,
		   c.city,
		   s.state,
		   a.zip_code,
		   ct.country
	  FROM address a 
	 INNER JOIN city c 
	 	ON a.city_id = c.city_id 
	 INNER JOIN state s 
	 	ON c.state_id = s.state_id 
	 INNER JOIN country ct 
	 	ON s.country_id = ct.country_id
),
nationality AS (
	SELECT c.contact_id,
		   ct.country
	  FROM contact c
	 INNER JOIN country ct
	 	ON c.nationality_id = ct.country_id
),
jobs AS (
	SELECT j.job_id,
		   j.work_address_id,
		   p.profession, 
		   c.company,
		   a.address,
		   a.city,
		   a.state,
		   a.zip_code,
		   a.country
	  FROM job j
	 INNER JOIN profession p
	 	ON j.profession_id = p.profession_id
	 INNER JOIN company c
	 	ON j.company_id = c.company_id
	  LEFT JOIN full_address a
	 	ON j.work_address_id = a.address_id
)
SELECT c.contact_id,
	   c.contact_name, 
	   c.contact_number, 
	   a.address,
	   a.city,
	   a.state,
	   a.zip_code,
	   a.country,
	   r.relationship,
	   n.country AS nationality,
	   j.job_id,
	   j.work_address_id,
	   j.profession,
	   j.company,
	   j.address AS job_street_address,
	   j.city AS job_city,
	   j.state AS job_state,
	   j.zip_code AS job_zip_code,
	   j.country AS job_country
  FROM contact c 
 INNER JOIN relationship r 
 	ON c.relationship_id = r.relationship_id  
  LEFT JOIN full_address a 
 	ON c.address_id = a.address_id
 INNER JOIN nationality n
 	ON c.contact_id = n.contact_id
  LEFT JOIN jobs j
 	ON c.job_id = j.job_id
;