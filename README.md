# Rent-connect

RentConnect is a backend service designed to facilitate the sharing of various items among users. The platform allows users to share their belongings, find items they need, and rent them for a specific period. By enabling item reservations on specific dates, RentConnect ensures exclusive access to the reserved item for the renting user. In cases where a desired item is not available, users can submit requests for specific items, which can be added to the sharing inventory.

The service implements essential CRUD operations for users and items, along with the capability to make item reservations. Additionally, users can leave reviews for items they have rented, contributing to a reliable and transparent sharing community. ShareIt also allows users to create requests for adding new items they would like to rent. Users can browse and view each other's requests, fostering interaction and expanding the sharing options.

Project has two modules:

- Gateway. Listens to requests from users. Distributes the load, makes initial verification and send requests further to main-service
- Main service. Server-side of an application. Recieves requests, performs operations, send data to client

ShareIt's backend service provides a solid foundation for the sharing economy, focusing on seamless item sharing, user engagement, and feedback-driven improvements. The platform aims to create a user-friendly and efficient ecosystem that promotes sustainable resource utilization and community collaboration.

**Here is its database structure:**

![db schema rent-connect](https://github.com/RoketV/rent-connect/assets/104717438/21aba273-463e-44f0-8dfc-db3e65b07b5f)

