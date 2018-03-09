# FinraTest

This is the application which can use API to upload a file with a few meta-data fields. Persist meta-data in persistence store in local file system.
use API to get file meta-data
use API to download content stream 
use API to search for file IDs with a search criterion 
Write a scheduler in the same app to poll for new items in the last hour and send an email.

### Upload File
@Post /metadatas/
Use postman to include the file you want to upload

### load all the meta-data
@Get /metadatas
use postman to retrieve all the meta-data

### load meta-data by id
@Get /metadatas/{id}
use postman to retrieve the metadata by its id

### load recent 1 hour meta-data
@Get /metadatas/recent
retrive the most recent 1 hour added file meta-data

### download the file by its id
@Post /metadatas/downloads/{id}
download the file by its meta-data id
