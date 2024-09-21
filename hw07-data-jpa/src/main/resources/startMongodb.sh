docker run -d --rm -p 27117:27017 --name mongo-test \
  -e MONGO_INITDB_ROOT_USERNAME=mongoadmin \
  -e MONGO_INITDB_ROOT_PASSWORD=secret \
  mongo

# mongodb://mongoadmin:secret@localhost:27117/