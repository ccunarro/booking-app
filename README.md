# Booking API v1

Quick notes:

- Authentication is basic and the api can be accessed with 'foo@gmail.com' and 'password'
- Schema is defined in schema.sql
- Sample data is located in data.sql

- H2 console available at: http://localhost:8080/h2-console/
- Api docs available at: http://localhost:8080/swagger-ui/index.html

Other comments:
- it was taken into account on the logic if the authenticated user 
was owner of the property to perform a block for example among other security checks.
- integration tests were implemented for blocks and bookings.
