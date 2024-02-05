# Bookstore

The service is a simple online bookstore application that allows users to browse books, view book details, and perform
basic CRUD operations.

## Build

As usually:

```bash
mvn clean verify -PIT
```

Please keep in mind, the command above must always succeed. There is no excuse otherwise.

## Running in Docker container

### Required property

The `auth.key` property serves as the secret key used for signing and verifying JSON Web Tokens which is used for
user authentication.

The `auth.token.expiration` property defines the duration in hours of validity for JSON Web Tokens generated by
the application.

The `mongo.host` property specifies the MongoDB host. The default value is localhost.

The `mongo.database` property specifies the MongoDB database name. The default value is bookstore.

The `postgresql.host` property is used to specify the host (or address) of the PostgreSQL database server. The
default value is localhost.

The `postgresql.port` property is used to specify the port of the PostgreSQL database server. The
default value is 5432.

The `postgresql.username` and `postgresql.password` are used to specify username and password for connecting do DB.
The default value for both username and password is test.

### Building docker image locally

Use the command below to build docker image.

```bash
docker build -t bookstore:local .
```

Use the command below to run bookstore application in docker container. Make sure that you have set up postgres and 
mongo databases locally or provide connection details via environment variables (host, port, database name etc.).

```bash
docker run -p 8080:8080 -e JAVA_OPTS='-Dauth.key=c2VjcmV0LWtleQ== -Dauth.token.expiration=4' bookstore:local
```

### Docker-compose for running locally
Simple way to run docker image with setting up databases.

```yaml
version: "2"
services:
  bookstore:
    image: bookstore:local
    ports:
      - 8080:8080
    depends_on:
      - postgres_bookstore
      - mongo_bookstore
    environment:
      JAVA_OPTS: "-Dauth.key=c2VjcmV0LWtleQ== -Dauth.token.expiration=4 -Dpostgresql.host=postgres_bookstore -Dmongo.host=mongo_bookstore"
  postgres_bookstore:
    image: postgres:14-alpine
    ports:
      - 5432:5432
    volumes:
      - ~/apps/postgres:/var/lib/postgresql/data
    environment:
      - POSTGRES_PASSWORD=test
      - POSTGRES_USER=test
      - POSTGRES_DB=bookstore
  mongo_bookstore:
    image: mongo:latest
    ports:
      - 27017:27017
    volumes:
      - ~/apps/mongo:/var/lib/postgresql/data
```

Check the application status as, for instance:

```bash
curl -s http://localhost:8080/api/books
```

The output should be something like:

```json
[]
```

## REST API

### POST /api/users/create
This endpoint is used to create a new user.

```json
{
  "username": "new_user",
  "password": "new_password"
}
```

### POST /api/login
This endpoint is used for user authentication. It validates the provided username and password, generates an access
token, and returns it if authentication is successful.

```json
{
  "username": "new_user",
  "password": "new_password"
}
```

### GET /api/book
Retrieve a list of all books. No access token is required.

### GET /api/books/{id}
Retrieve details of a specific book by ID. No access token is required.

### POST /api/book
Create a new book. No access token is required.

```json
{
  "title": "New Book",
  "author": "Alice Johnson",
  "price": 24.99,
  "publication-year": 2021
}
```

### DELETE /api/books/{id}
Delete a book by ID. No access token is required.

### PUT /api/books/{id}
Update details of a specific book by ID. No access token is required.

```json
{
  "title": "Updated Book",
  "author": "Alice Johnson",
  "price": 29.99,
  "publication-year": 2022
}
```

### GET /api/books/{book_id}/review
Retrieve a list of reviews for a specific book. Required `X-ACCESS-TOKEN` in header.

### POST /api/books/{book_id}/review
Submit a new review for a specific book. Required `X-ACCESS-TOKEN` in header.

```json
{
  "rating": 4,
  "comment": "Enjoyed reading this book."
}
```

### POST /api/users/activity-log
Log user activity to capture specific events or actions performed by users. Required `X-ACCESS-TOKEN` in header.

```json
{
  "activity-type": "Login",
  "details": "User logged in successfully."
}
```

### GET /api/users/activity-log
Retrieve a list of user activity logs. Required `X-ACCESS-TOKEN` in header.

### POST /api/users/orders
Place a new order for books. Required `X-ACCESS-TOKEN` in header.

```json
[
  "book_id_1",
  "book_id_2",
  "book_id_3"
]
```

### GET /api/users/orders
Retrieve a list of orders placed by the user. Required `X-ACCESS-TOKEN` in header.

## GraphQL API

### Query: bookById
Retrieve details of a specific book by ID.

```
query {
  bookById(id: "example_book_id") {
    id
    title
    author
    price
    publicationYear
  }
}
```

### Query: bookList
Retrieve a list of all books.

```
query {
  bookList {
    id
    title
    author
    price
    publicationYear
  }
}
```

### Mutation: addBook
Add a new book.

```
mutation {
  addBook(author: "New Author", title: "New Book", publicationYear: 2023, price: 14.99) {
    id
    title
    author
    price
    publicationYear
  }
}
```

### Mutation: updateBook
Update details of a specific book by ID.

```
mutation {
  updateBook(id: "existing_book_id", author: "Updated Author", title: "Updated Book", publicationYear: 2022, price: 19.99)
}
```

### Mutation: deleteBook
Delete a book by ID.

```
mutation {
  deleteBook(id: "book_to_delete_id")
}
```



## Have fun!