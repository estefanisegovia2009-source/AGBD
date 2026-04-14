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

	