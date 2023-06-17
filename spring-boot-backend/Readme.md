# Fulltext search across multiple data sources - Backend #


### Creating a complete docker image

```
docker build -t fulltext-accross-datasources .
```

### Creating a docker image in multiple stages

```
docker build -t fulltext-accross-datasources-dependency --target=dependency .
```

Now you can create either a dev container or a packaged image which is targeted for deployments.

#### Building packaged image
- Either:
  ```
  docker build -t fulltext-accross-datasources-build --target=build --no-cache --build-arg DEPENDENCY_IMAGE=fulltext-accross-datasources-dependency .
  docker build -t fulltext-accross-datasources-final --no-cache --build-arg BUILD_IMAGE=fulltext-accross-datasources-build .
  ```

- or:
  ```
  docker build -t fulltext-accross-datasources-final --no-cache --build-arg DEPENDENCY_IMAGE=fulltext-accross-datasources-dependency .
  ```

#### Building dev container
```
docker build -t fulltext-accross-datasources-final --target=dev --no-cache --build-arg DEPENDENCY_IMAGE=fulltext-accross-datasources-dependency .
```


### Running Image
- named container:
  ```
  docker run -p 8082:8080 --name srv-fulltext-accross-datasources fulltext-accross-datasources-final
  ```
- delete container after running:
  ```
  docker run -p 8082:8080 -it --rm fulltext-accross-datasources-final
  ```

Environment variables (set with ``-e VARABLE_NAME=VARIABLE_VALUE``):

| Variable                      | Description                                                    |
|-------------------------------|----------------------------------------------------------------|
| PATIENTS_DATASOURCE_URL       | Domain where Patient-DB resides                                |
| PATIENTS_DATASOURCE_USERNAME  | Port where Patient-DB listens on                               |
| PATIENTS_DATASOURCE_PASSWORD  | Name of Patient-DB                                             |
| ADDRESSES_DATASOURCE_URL      | Domain where Address-DB resides                                |
| ADDRESSES_DATASOURCE_USERNAME | Port where Address-DB listens on                               |
| ADDRESSES_DATASOURCE_PASSWORD | Name of Address-DB                                             |
| ELASTICSEARCH_HOST            | Domain where Elasticsearch resides                             |
| ELASTICSEARCH_PORT            | Port where Elasticsearch listens on REST-Request               |
| CORS_ORIGIN                   | Origin from which Cross-Origin-Requests to `/data` are allowed |


### Updating Dependency Image
```
docker build -t fulltext-accross-datasources-dependency --target=dependency --no-cache --build-arg SOURCE_IMAGE=fulltext-accross-datasources-dependency .
```