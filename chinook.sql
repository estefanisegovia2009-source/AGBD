8
SELECT c.FirstName, c.LastName, c.Address, i.InvoiceId
FROM customers c
JOIN invoices i
ON c.CustomerId=i.CustomerId;

9

SELECT g.Name,
COUNT(*) Cantidad
FROM genres g
JOIN tracks t
ON g.GenreId=t.GenreId
GROUP BY g.Name
ORDER BY Cantidad DESC;


10
SELECT DISTINCT c.FirstName, c.LastName, ar.Name
FROM customers c JOIN invoices i ON c.CustomerId=i.CustomerId
JOIN invoice_items ii ON i.InvoiceId=ii.InvoiceId
JOIN tracks t ON ii.TrackId=t.TrackId
JOIN albums al ON t.AlbumId=al.AlbumId
JOIN artists ar ON al.ArtistId=ar.ArtistId
ORDER BY c.LastName;

11
SELECT c.FirstName, c.LastName, c.City, t.Name, g.Name FROM customers c
JOIN invoices i ON c.CustomerId=i.CustomerId
JOIN invoice_items ii ON i.InvoiceId=ii.InvoiceId
JOIN tracks t ON ii.TrackId=t.TrackId
JOIN genres g ON t.GenreId=g.GenreId;

12
SELECT * FROM customers c
JOIN invoices i ON c.CustomerId = i.CustomerId
JOIN invoice_items ii ON i.InvoiceId = ii.InvoiceId
JOIN tracks t ON ii.TrackId = t.TrackId
JOIN albums a ON t.AlbumId = a.AlbumId
JOIN artists ar ON a.ArtistId = ar.ArtistId
JOIN genres g ON t.GenreId = g.GenreId
JOIN media_types mt ON t.MediaTypeId = mt.MediaTypeId
JOIN playlist_track pt ON t.TrackId = pt.TrackId
JOIN playlists p ON pt.PlaylistId = p.PlaylistId
JOIN employees e ON c.SupportRepId = e.EmployeeId;