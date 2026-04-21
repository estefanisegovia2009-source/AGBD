1
SELECT  FirstName, LastName FROM employees
	ORDER by LastName, FirstName ASC

2

 SELECT tracks.Name, tracks.Milliseconds FROM tracks
JOIN albums ON tracks.AlbumId = albums.AlbumId
WHERE albums.AlbumId = 5
ORDER BY tracks.Milliseconds DESC;

3 

SELECT UnitPrice, name FROM tracks
ORDER by UnitPrice ASC
LIMIT 10;

4
SELECT * FROM artists
JOIN albums ON artists.ArtistId = albums.ArtistId
JOIN tracks ON albums.AlbumId = tracks.AlbumId
JOIN genres ON tracks.GenreId = genres.GenreId
WHERE UnitPrice = 0.99;


5
SELECT tracks.name, tracks.Milliseconds, albums.Title, artists.name FROM tracks
JOIN albums ON artists.ArtistId = albums.ArtistId
JOIN artists ON albums.ArtistId = albums.AlbumId
JOIN genres ON tracks.GenreId = genres.GenreId
ORDER BY tracks.Milliseconds ASC
LIMIT 20;



6
SELECT e.LastName AS "apellido_empleado", j.LastName AS "ape_jefe", e.Title, COUNT(c.CustomerId)  FROM  employees e 
JOIN employees j ON e.ReportsTo = j.EmployeeId
JOIN customers c  ON e.EmployeeId = c.SupportRepId
GROUP BY e.EmployeeId

	
